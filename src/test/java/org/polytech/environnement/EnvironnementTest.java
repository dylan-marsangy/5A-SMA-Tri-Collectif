package org.polytech.environnement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.agent.strategies.StrategyMove;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environnement Tests")
public class EnvironnementTest {

    private final int N = 5;
    private final int M = 5;
    private final int NB_AGENTS = 5;
    private final int NB_BLOCKS = 10;

    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;

    private Environnement environnement;
    private Agent agent;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(N, M, NB_AGENTS, NB_BLOCKS);

        Agent.cleanID();
        agent = new Agent(T, K_MINUS, K_PLUS);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Initialize Grid")
    public void hasCorrectDimensions() {
        assertTrue(environnement.isInside(0, 0));
        assertTrue(environnement.isInside(0, M - 1));
        assertTrue(environnement.isInside(N - 1, 0));
        assertTrue(environnement.isInside(N - 1, M - 1));

        assertFalse(environnement.isInside(N, M));
    }

    @Test
    @DisplayName("Display Grid")
    public void displayGrid() {
        environnement.insert(new Agent(T, K_PLUS, K_MINUS), 0, 0);
        environnement.insert(new Agent(T, K_PLUS, K_MINUS), 1, 0);
        environnement.insert(new Agent(T, K_PLUS, K_MINUS), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 3);
        environnement.insert(new Block(BlockValue.B), 4, 2);

        String expected =
                " X | 0 | 0 | 0 | 0 |\n" +
                        " X | 0 | 0 | 0 | 0 |\n" +
                        " X | 0 | 0 | A | 0 |\n" +
                        " 0 | 0 | 0 | 0 | 0 |\n" +
                        " 0 | 0 | B | 0 | 0 |\n";

        assertEquals(expected, environnement.toString());
    }

    // INSERT ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Insert Entity Inside Grid")
    public void insertEntity_insideGrid() {
        environnement.insert(agent, 1, 1);
        assertEquals(agent, environnement.getEntity(1 ,1));
    }

    @Test
    @DisplayName("Avoid Collisions While Inserting")
    public void avoidCollisions_whenInserting() {
        environnement.insert(agent, 1, 1);
        assertThrows(CollisionException.class, () -> environnement.insert(new Agent(T, K_PLUS, K_MINUS), 1, 1));
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Remove Entity From Grid")
    public void removeEntity() {
        environnement.insert(agent, 1, 1);
        environnement.remove(1 ,1);
        assertNull(environnement.getEntity(1 ,1));
    }

    // MOVE ------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Move Entity Inside Grid")
    public void moveEntity() {
        environnement.insert(agent, 1,1);
        environnement.move(agent, Direction.NORTH, 1);
        assertNull(environnement.getEntity(1, 1));
        assertEquals(agent, environnement.getEntity(0, 1));
    }

    @Test
    @DisplayName("Avoid Moves Outside Grid")
    public void avoidMoving_outsideGrid() {
        environnement.insert(agent, 0, 0);
        assertFalse(environnement.move(agent, Direction.NORTH, 1));
    }

    @Test
    @DisplayName("Avoid Collisions While Moving")
    public void avoidCollisions_whenMoving() {
        Agent a2 = new Agent(T, K_PLUS, K_MINUS);
        environnement.insert(agent, 0, 1);
        environnement.insert(a2, 1, 1);
        assertThrows(CollisionException.class, () -> environnement.move(a2, Direction.NORTH, 1));
    }

    // PERCEPTION ------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Entity Perception For All Directions")
    public void getPerception_allDirections() {
        Agent a2 = new Agent(T, K_PLUS, K_MINUS);
        Agent a3 = new Agent(T, K_PLUS, K_MINUS);
        Agent a4 = new Agent(T, K_PLUS, K_MINUS);

        environnement.insert(agent, 0, 2);
        environnement.insert(a2, 2, 2);
        environnement.insert(a3, 0, 0);
        environnement.insert(a4, 4, 2);

        Map<Direction, Movable> expected = new HashMap<>();
        expected.put(Direction.SOUTH, a2);
        expected.put(Direction.EAST, null);
        expected.put(Direction.WEST, a3);

        assertEquals(expected, environnement.perception(agent, 2));
    }

    // RANDOM INSERTS --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Count elements on grid")
    public void hasCorrectElements() {
        environnement.insertAgents(T, K_PLUS, K_MINUS);
        environnement.insertBlocks();

        int agents = 0;
        int objects = 0;

        for (int i = 0; i < environnement.getGrid().length; i++) {
            for (int j = 0; j < environnement.getGrid()[i].length; j++) {
                if (environnement.getEntity(i, j) instanceof Agent) {
                    ++agents;
                } else if (environnement.getEntity(i, j) instanceof Block) {
                    ++objects;
                }
            }
        }

        assertEquals(agents, NB_AGENTS);
        assertEquals(objects, NB_BLOCKS);
    }

    // RUNNING ---------------------------------------------------------------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("Pick Random Agent")
    public void pickRandomAgent() {
        Agent.cleanID();
        environnement.insertAgents(T, K_PLUS, K_MINUS);

        Set<Long> picked = new HashSet<>();
        IntStream.rangeClosed(1, 20).forEach(input -> {
            Long id = environnement.pickRandomAgent().getID();
            System.out.println(id);
            picked.add(id);
        });

        assertNotEquals(1, picked.size());
    }

    // RUNNING --- MOVE ------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent should move when surrounded by other entities")
    public void agentDoesntMove_whenSurroundedBy_Others() {
        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.A), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        assertEquals(agent, environnement.getEntity(2,2));
        assertFalse(environnement.isEmpty(4, 2));
        assertFalse(environnement.isEmpty(0, 2));
        assertFalse(environnement.isEmpty(2, 0));
        assertFalse(environnement.isEmpty(2, 4));

        Direction result = agent.execute(new StrategyMove(), environnement.perception(agent, 2));
        assertNull(result);
    }

    @RepeatedTest(10)
    @DisplayName("agent should randomly move")
    public void agentRandomWalk() {
        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        assertEquals(agent, environnement.getEntity(2,2));
        assertTrue(environnement.isEmpty(4, 2));
        assertTrue(environnement.isEmpty(0, 2));
        assertTrue(environnement.isEmpty(2, 0));
        assertTrue(environnement.isEmpty(2, 4));

        Direction result = agent.execute(new StrategyMove(), environnement.perception(agent, 2));
        assertNotNull(result);
        System.out.println("Move towards : " + result);

        environnement.move(agent, result, 2);
        assertNull(environnement.getEntity(2,2));

        switch (result) {
            case SOUTH:
                assertFalse(environnement.isEmpty(4, 2));
                break;
            case NORTH:
                assertFalse(environnement.isEmpty(0, 2));
                break;
            case WEST:
                assertFalse(environnement.isEmpty(2, 0));
                break;
            case EAST:
                assertFalse(environnement.isEmpty(2, 4));
                break;
        }
    }

    // RUNNING --- PICK UP ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent shouldn't pick up A when visited full A")
    public void doNotPickUp_A_whenFull_A() {
        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.A), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.A)));

        assertNull(agent.execute(new StrategyPickUp(), environnement.perception(agent, 2)));
        assertEquals(agent, environnement.getEntity(2,2));
        assertFalse(agent.isHolding());
        assertNotNull(environnement.getEntity(0, 2));
        assertNotNull(environnement.getEntity(2, 2));
        assertNotNull(environnement.getEntity(2, 4));
        assertNotNull(environnement.getEntity(4, 2));
    }

    @RepeatedTest(10)
    @DisplayName("agent should pick up any block A when visited full B")
    public void pickUp_A_whenMostlyBlocks_B() {
        assertFalse(agent.isHolding());

        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.A), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.B)));

        Set<Direction> expected = Arrays.stream(Direction.values()).collect(Collectors.toSet());
        Direction result = agent.execute(new StrategyPickUp(), environnement.perception(agent, 2));
        System.out.println("Pick up towards : " + result);

        environnement.pickUpBlock(agent, result, 2);
        assertEquals(agent, environnement.getEntity(2,2));
        assertTrue(agent.isHolding());
        assertTrue(expected.contains(result));

        switch (result) {
            case SOUTH:
                assertTrue(environnement.isEmpty(4, 2));
                break;
            case NORTH:
                assertTrue(environnement.isEmpty(0, 2));
                break;
            case WEST:
                assertTrue(environnement.isEmpty(2, 0));
                break;
            case EAST:
                assertTrue(environnement.isEmpty(2, 4));
                break;
        }
    }

    // RUNNING --- PUT DOWN --------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent should do nothing")
    public void doNotPutDown_A_whenFull_Movable() {
        agent.setHolding(new Block(BlockValue.A));

        // Agent entouré d'autres entités : ne devrait pas pouvoir déposer son bloc.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.A), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        assertEquals(agent, environnement.getEntity(2,2));
        assertNotNull(agent.getHolding());

        assertNull(agent.execute(new StrategyPutDown(), environnement.perception(agent, 2)));
        assertNotNull(agent.getHolding());
        assertEquals(agent, environnement.getEntity(2,2));
    }

    @RepeatedTest(10)
    @DisplayName("agent should put down block A on SOUTH or nothing")
    public void putDown_A_whenMostlyBlocks_A() {
        agent.setHolding(new Block(BlockValue.A));
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.A), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);

        assertEquals(agent, environnement.getEntity(2,2));
        assertNotNull(agent.getHolding());
        assertTrue(environnement.isEmpty(4, 2));

        Set<Movable> expected = Stream.of(agent.getHolding(), null).collect(Collectors.toSet());

        Movable result;
        Direction direction = agent.execute(new StrategyPutDown(), environnement.perception(agent, 2));
        if (direction != null) {
            environnement.putDownBlock(agent, direction, 2);
            result = environnement.getEntity(4, 2);
            assertNull(agent.getHolding());
        } else {
            result = null;
            assertNotNull(agent.getHolding());
        }

        System.out.println("Bloc en (4,2) est : " + result);
        assertEquals(agent, environnement.getEntity(2,2));
        assertTrue(expected.contains(result));
    }

}
