package org.polytech.environment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.agent.strategies.StrategyMove;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;
import org.polytech.environment.exceptions.CollisionException;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environment Tests")
public class EnvironmentTest {

    private final int N = 5;
    private final int M = 5;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environment environment;
    private Agent agent;

    @BeforeEach
    public void initializeEnvironment() {
        environment = new Environment(N, M);

        Agent.cleanID();
        agent = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Initialize Grid")
    public void hasCorrectDimensions() {
        assertTrue(environment.isInside(0, 0));
        assertTrue(environment.isInside(0, M - 1));
        assertTrue(environment.isInside(N - 1, 0));
        assertTrue(environment.isInside(N - 1, M - 1));

        assertFalse(environment.isInside(N, M));
    }

    @Test
    @DisplayName("Display Grid")
    public void displayGrid() {
        agent.pickUp(new Block(BlockValue.A));
        Agent a2 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        a2.pickUp(new Block(BlockValue.B));
        environment.insert(agent, 0, 0);
        environment.insert(a2, 1, 0);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 3);
        environment.insert(new Block(BlockValue.B), 4, 2);

        System.out.println(environment);
    }

    // INSERT ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Insert Entity Inside Grid")
    public void insertEntity_insideGrid() {
        environment.insert(agent, 1, 1);
        assertEquals(agent, environment.getEntity(1, 1));
    }

    @Test
    @DisplayName("Avoid Collisions While Inserting")
    public void avoidCollisions_whenInserting() {
        environment.insert(agent, 1, 1);
        assertThrows(CollisionException.class, () -> environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 1, 1));
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Remove Entity From Grid")
    public void removeEntity() {
        environment.insert(agent, 1, 1);
        environment.remove(1, 1);
        assertNull(environment.getEntity(1, 1));
    }

    // MOVE ------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Move Entity Inside Grid")
    public void moveEntity() {
        environment.insert(agent, 1, 1);
        environment.move(agent, Direction.NORTH, 1);
        assertNull(environment.getEntity(1, 1));
        assertEquals(agent, environment.getEntity(0, 1));
    }

    @Test
    @DisplayName("Avoid Moves Outside Grid")
    public void avoidMoving_outsideGrid() {
        environment.insert(agent, 0, 0);
        assertFalse(environment.move(agent, Direction.NORTH, 1));
    }

    @Test
    @DisplayName("Avoid Collisions While Moving")
    public void avoidCollisions_whenMoving() {
        Agent a2 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        environment.insert(agent, 0, 1);
        environment.insert(a2, 1, 1);
        assertThrows(CollisionException.class, () -> environment.move(a2, Direction.NORTH, 1));
    }

    // PERCEPTION ------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Entity Perception For All Directions")
    public void getPerception_allDirections() {
        Agent a2 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        Agent a3 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
        Agent a4 = new Agent(I, T, K_PLUS, K_MINUS, ERROR);

        environment.insert(agent, 0, 2);
        environment.insert(a2, 2, 2);
        environment.insert(a3, 0, 0);
        environment.insert(a4, 4, 2);

        Map<Direction, Movable> expected = new HashMap<>();
        expected.put(Direction.SOUTH, a2);
        expected.put(Direction.EAST, null);
        expected.put(Direction.WEST, a3);

        assertEquals(expected, environment.perception(agent, 2));
    }

    // RUNNING --- MOVE ------------------------------------------------------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("agent should randomly move except towards another Agent")
    public void agentRandomWalk() {
        // Agent entouré des blocs A.
        environment.insert(agent, 2, 2);
        environment.insert(new Block(BlockValue.A), 0, 2);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 2, 0);
        environment.insert(null, 2, 4);
        environment.insert(new Block(BlockValue.B), 4, 2);

        assertEquals(agent, environment.getEntity(2, 2));
        assertFalse(environment.isEmpty(4, 2));
        assertFalse(environment.isEmpty(0, 2));
        assertFalse(environment.isEmpty(2, 0));
        assertTrue(environment.isEmpty(2, 4));

        // L'agent doit choisir une direction aléatoire ne contenant pas d'agent.
        Direction result = agent.execute(new StrategyMove(), environment.perception(agent, 2));
        assertNotEquals(Direction.WEST, result);
        assertTrue(Arrays.asList(Direction.values()).contains(result));
    }

    // RUNNING --- PICK UP ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("agent shouldn't pick up A when visited full A")
    public void doNotPickUp_A_whenFull_A() {
        assertFalse(agent.isHolding());

        environment.insert(agent, 2, 2);
        environment.insert(new Block(BlockValue.A), 0, 2);

        IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.A)));

        assertNull(agent.execute(new StrategyPickUp(Direction.NORTH), environment.perception(agent, 2)));
        assertEquals(agent, environment.getEntity(2, 2));
        assertFalse(agent.isHolding());
        assertNotNull(environment.getEntity(0, 2));
    }

    @RepeatedTest(10)
    @DisplayName("agent should maintain picking up A when visited full B")
    public void pickUp_A_whenMostlyBlocks_B() {
        assertFalse(agent.isHolding());

        environment.insert(agent, 2, 2);
        environment.insert(new Block(BlockValue.A), 4, 2);

        final List<Direction> results = new ArrayList<>();
        IntStream.rangeClosed(1, 20).forEach(i -> {
            IntStream.rangeClosed(1, T).forEach(index -> agent.visit(new Block(BlockValue.B)));
            Direction result = agent.execute(new StrategyPickUp(Direction.SOUTH), environment.perception(agent, 2));
            results.add(result);
        });

        assertFalse(results.contains(Direction.NORTH));
        assertFalse(results.contains(Direction.WEST));
        assertFalse(results.contains(Direction.EAST));

        // En moyenne, le résultat devrait être très souvent SOUTH et rarement null.
        long countSouth = new ArrayList<>(results).stream()
                .filter(direction -> direction == Direction.SOUTH)
                .count();
        long countNull = new ArrayList<>(results).stream()
                .filter(Objects::isNull)
                .count();
        assertTrue(countSouth > countNull);
    }

    // RUNNING --- PUT DOWN --------------------------------------------------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("agent shouldn't put down B when surrounded by A in most cases")
    public void doNotPutDown_B_whenMostly_A() {
        agent.pickUp(new Block(BlockValue.B));

        // Agent entouré de blocs de type contraire : ne devrait pas pouvoir déposer son bloc.
        environment.insert(agent, 2, 2);
        environment.insert(new Block(BlockValue.A), 0, 2);
        environment.insert(new Block(BlockValue.A), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 4);
        environment.insert(null, 4, 2);

        assertEquals(agent, environment.getEntity(2, 2));
        assertNotNull(agent.getHolding());

        final List<Direction> results = new ArrayList<>();
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Direction result = agent.execute(new StrategyPutDown(Direction.SOUTH), environment.perception(agent, 2));
            results.add(result);
        });

        assertFalse(results.contains(Direction.NORTH));
        assertFalse(results.contains(Direction.WEST));
        assertFalse(results.contains(Direction.EAST));

        // En moyenne, le résultat devrait être très souvent null et rarement SOUTH.
        long countSouth = results.stream()
                .filter(direction -> direction == Direction.SOUTH)
                .count();
        long countNull = results.stream()
                .filter(Objects::isNull)
                .count();
        assertTrue(countSouth < countNull);
    }

    @RepeatedTest(10)
    @DisplayName("agent should put down block A on EAST on most cases")
    public void putDown_A_whenMostlyBlocks_A() {
        agent.pickUp(new Block(BlockValue.A));

        environment.insert(agent, 2, 2);
        environment.insert(new Block(BlockValue.A), 0, 2);
        environment.insert(new Block(BlockValue.A), 2, 0);
        environment.insert(null, 2, 4);
        environment.insert(new Block(BlockValue.A), 4, 2);

        assertEquals(agent, environment.getEntity(2, 2));
        assertNotNull(agent.getHolding());

        final List<Direction> results = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Direction result = agent.execute(new StrategyPutDown(Direction.EAST), environment.perception(agent, 2));
            results.add(result);
        });

        assertFalse(results.contains(Direction.NORTH));
        assertFalse(results.contains(Direction.WEST));
        assertFalse(results.contains(Direction.SOUTH));
    }

}
