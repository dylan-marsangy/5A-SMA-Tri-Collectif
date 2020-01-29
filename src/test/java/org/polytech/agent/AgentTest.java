package org.polytech.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {

    private final int t = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;

    private Agent agent;

    @BeforeEach
    public void initializeAgent() {
        Agent.cleanID();
        agent = new Agent(t, K_PLUS, K_MINUS);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Identification By Auto-Incremented Long")
    public void agentsIdentification() {
        assertEquals(2L, new Agent(t, K_PLUS, K_MINUS).getID());
    }

    // MEMORY ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("memory should behave like a FIFO stack")
    public void memoryIsFIFO() {
        agent.setMemorySize(3);
        agent.visit(BlockValue.A);
        agent.visit(BlockValue.B);
        agent.visit(BlockValue.B);
        agent.visit(BlockValue.A);
        assertEquals(agent.getMemory().stream().findFirst().orElse(BlockValue.ZERO), BlockValue.B,
                "Last element in the memory is not B.");
        assertEquals(agent.getMemory().peek(), BlockValue.B,
                "First element in the memory is not B.");
    }

    // STRATEGY --------------------------------------------------------------------------------------------------------

    // STRATEGY --- PICK UP --------------------------------------------------------------------------------------------

    @Test
    @DisplayName("should return 1")
    public void computeProba_pickUp_whenOnlyBlock_A() {
        // On remplit la mémoire de l'agent de bloc A.
        IntStream.rangeClosed(1, t).forEach(index -> agent.visit(BlockValue.A));

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        assertEquals(BlockValue.B, result.getKey());
        assertEquals(1d, result.getValue());
    }

    @Test
    @DisplayName("compute proba for pick up when 50/50")
    public void computeProba_pickUp_whenBlocks_equalProportion() {
        // On remplit la mémoire de l'agent d'autant de blocs A et B.
        IntStream.rangeClosed(1, t / 2).forEach(index -> agent.visit(BlockValue.A));
        IntStream.rangeClosed(1, t / 2).forEach(index -> agent.visit(BlockValue.B));

        double expected = Math.pow(K_PLUS / (K_PLUS + 0.5), 2);
        System.out.println(expected);

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        System.out.println(result.getKey());
        assertEquals(expected, result.getValue());
    }

    @Test
    @DisplayName("compute proba for pick up when 30/70")
    public void computeProba_pickUp_whenBlocks_mostlyB() {
        // On remplit la mémoire de l'agent de bloc B.
        IntStream.rangeClosed(1, t / 3).forEach(index -> agent.visit(BlockValue.A));
        IntStream.rangeClosed(1, 2 * t / 3).forEach(index -> agent.visit(BlockValue.B));

        double expected = Math.pow(K_PLUS / (K_PLUS + ((double) 3 / 10)), 2);

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        assertEquals(BlockValue.A, result.getKey());
        assertEquals(expected, result.getValue());
    }

    // STRATEGY --- PUT DOWN -------------------------------------------------------------------------------------------

    @Test
    @DisplayName("should return null")
    public void computeProba_putDown_A_whenOnlyBlock_A() {
        agent.setHolding(new Block(BlockValue.A));

        Map<Direction, Movable> perception = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(direction -> perception.put(direction, new Block(BlockValue.A)));

        assertNull(new StrategyPutDown().execute(agent, new HashMap<>(perception)));
    }

    @Test
    @DisplayName("should return null")
    public void computeProba_putDown_B_whenOnlyBlock_A() {
        agent.setHolding(new Block(BlockValue.B));

        Map<Direction, Movable> perception = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(direction -> perception.put(direction, new Block(BlockValue.A)));

        assertNull(new StrategyPutDown().execute(agent, perception));
    }

    @Test
    @DisplayName("compute proba for put down A when 75% A")
    public void computeProba_putDown_A_whenBlocks_mostlyA() {
        agent.setHolding(new Block(BlockValue.A));

        Map<Direction, Movable> perception = new HashMap<>();
        perception.put(Direction.NORTH, new Block(BlockValue.A));
        perception.put(Direction.EAST, new Block(BlockValue.A));
        perception.put(Direction.WEST, new Block(BlockValue.A));
        perception.put(Direction.SOUTH, null);

        Set<Direction> expected = Stream.of(Direction.SOUTH, null).collect(Collectors.toSet());

        Set<Direction> result = new HashSet<>();
        IntStream.rangeClosed(1, 20).forEach(index ->
                result.add(new StrategyPutDown().execute(agent, new HashMap<>(perception))));

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("should return WEST, SOUTH or null")
    public void computeProba_putDown_A_whenBlocks_halfA() {
        agent.setHolding(new Block(BlockValue.A));

        Map<Direction, Movable> perception = new HashMap<>();
        perception.put(Direction.NORTH, new Block(BlockValue.A));
        perception.put(Direction.EAST, new Block(BlockValue.A));
        perception.put(Direction.WEST, null);
        perception.put(Direction.SOUTH, null);

        Set<Direction> expected = Stream.of(Direction.SOUTH, Direction.WEST, null).collect(Collectors.toSet());

        Set<Direction> result = new HashSet<>();
        IntStream.rangeClosed(1, 20).forEach(index ->
                result.add(new StrategyPutDown().execute(agent, new HashMap<>(perception))));

        assertEquals(expected, result);
    }

}
