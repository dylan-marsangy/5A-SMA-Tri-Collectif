package org.polytech.environnement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.agent.strategies.StrategyMove;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environnement Tests")
public class EnvironnementTest {

    private final int N = 5;
    private final int M = 5;
    private final int NB_AGENTS = 5;
    private final int NB_BLOCKS_A = 5;
    private final int NB_BLOCKS_B = 5;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environnement environnement;
    private Agent agent;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(N, M, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                NB_AGENTS, I, T, K_PLUS, K_MINUS, ERROR,
                NB_BLOCKS_A, NB_BLOCKS_B);

        Agent.cleanID();
        agent = new Agent(I, T, K_MINUS, K_PLUS, ERROR);
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
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 1, 0);
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 3);
        environnement.insert(new Block(BlockValue.B), 4, 2);

        System.out.println(environnement);
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
        assertThrows(CollisionException.class, () -> environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 1, 1));
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
        Agent a2 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        environnement.insert(agent, 0, 1);
        environnement.insert(a2, 1, 1);
        assertThrows(CollisionException.class, () -> environnement.move(a2, Direction.NORTH, 1));
    }

    // PERCEPTION ------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Entity Perception For All Directions")
    public void getPerception_allDirections() {
        Agent a2 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        Agent a3 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        Agent a4 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);

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

    // RUNNING --- MOVE ------------------------------------------------------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("agent should randomly move when no holding")
    public void agentRandomWalk_noAgents() {
        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.B), 4, 2);

        assertEquals(agent, environnement.getEntity(2,2));
        assertFalse(environnement.isEmpty(4, 2));
        assertFalse(environnement.isEmpty(0, 2));
        assertFalse(environnement.isEmpty(2, 0));
        assertFalse(environnement.isEmpty(2, 4));

        Direction result = agent.execute(new StrategyMove(), environnement.perception(agent, 2));
        assertTrue(Arrays.asList(Direction.values()).contains(result));
    }


    @RepeatedTest(10)
    @DisplayName("agent shouldn't move towards other agents")
    public void agentRandomWalk_anyAgents() {
        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Agent(I, T, K_MINUS, K_PLUS, ERROR), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        assertEquals(agent, environnement.getEntity(2,2));
        assertFalse(environnement.isEmpty(4, 2));
        assertFalse(environnement.isEmpty(0, 2));
        assertFalse(environnement.isEmpty(2, 0));
        assertFalse(environnement.isEmpty(2, 4));

        Direction result = agent.execute(new StrategyMove(), environnement.perception(agent, 2));
        assertNotEquals(Direction.WEST, result);
    }

    @RepeatedTest(10)
    @DisplayName("agent should randomly move towards empty cases only when holding")
    public void agentRandomWalk_holding() {
        agent.setHolding(new Block(BlockValue.B));

        // Agent entouré des blocs A.
        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 4);

        assertEquals(agent, environnement.getEntity(2,2));
        assertTrue(environnement.isEmpty(4, 2));
        assertFalse(environnement.isEmpty(0, 2));
        assertFalse(environnement.isEmpty(2, 0));
        assertFalse(environnement.isEmpty(2, 4));

        Direction result = agent.execute(new StrategyMove(), environnement.perception(agent, 2));
        assertEquals(Direction.SOUTH, result);
    }

    // RUNNING --- PICK UP ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent shouldn't pick up A when visited full A")
    public void doNotPickUp_A_whenFull_A() {
        assertFalse(agent.isHolding());

        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 0, 2);

        IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.A)));

        assertNull(agent.execute(new StrategyPickUp(Direction.NORTH), environnement.perception(agent, 2)));
        assertEquals(agent, environnement.getEntity(2,2));
        assertFalse(agent.isHolding());
        assertNotNull(environnement.getEntity(0, 2));
    }

    @RepeatedTest(10)
    @DisplayName("agent should maintain picking up A when visited full B")
    public void pickUp_A_whenMostlyBlocks_B() {
        assertFalse(agent.isHolding());

        environnement.insert(agent, 2, 2);
        environnement.insert(new Block(BlockValue.A), 4, 2);

        IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.B)));

        Direction result = agent.execute(new StrategyPickUp(Direction.SOUTH), environnement.perception(agent, 2));
        assertEquals(Direction.SOUTH, result);

        environnement.pickUpBlock(agent, result, 2);
        assertEquals(agent, environnement.getEntity(2,2));
        assertTrue(agent.isHolding());
        assertTrue(environnement.isEmpty(4, 2));
    }

    // RUNNING --- PUT DOWN --------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent should do nothing")
    public void doNotPutDown_A_whenFull_Movable() {
        agent.pickUp(new Block(BlockValue.A));

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
        agent.pickUp(new Block(BlockValue.A));
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
