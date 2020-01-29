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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentTest {

    private static final int t = 10;
    private static final double k = 0.3;
    private static final double kPlus = 0.1;

    private Agent agent;

    @BeforeEach
    public void initializeAgent() {
        Agent.cleanID();
        agent = new Agent(t, kPlus, k);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Identification By Auto-Incremented Long")
    public void agentsIdentification() {
        assertEquals(2L, new Agent(t, kPlus, k).getID());
    }

    // MEMORY ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Memory is a FIFO stack.")
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
    @DisplayName("Compute Proba Pick Up - Full Block A")
    public void computeProba_pickUp_whenOnlyBlock_A() {
        // On remplit la mémoire de l'agent de bloc A.
        IntStream.rangeClosed(1, t).forEach(index -> agent.visit(BlockValue.A));

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        assertEquals(BlockValue.B, result.getKey());
        assertEquals(1d, result.getValue());
    }

    @Test
    @DisplayName("Compute Proba Pick Up - 50/50")
    public void computeProba_pickUp_whenBlocks_equalProportion() {
        // On remplit la mémoire de l'agent d'autant de blocs A et B.
        IntStream.rangeClosed(1, t / 2).forEach(index -> agent.visit(BlockValue.A));
        IntStream.rangeClosed(1, t / 2).forEach(index -> agent.visit(BlockValue.B));

        double expected = Math.pow(kPlus / (kPlus + 0.5), 2);
        System.out.println(expected);

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        System.out.println(result.getKey());
        assertEquals(expected, result.getValue());
    }

    @Test
    @DisplayName("Compute Proba Pick Up - 30/70")
    public void computeProba_pickUp_whenBlocks_mostlyB() {
        // On remplit la mémoire de l'agent de bloc B.
        IntStream.rangeClosed(1, t / 3).forEach(index -> agent.visit(BlockValue.A));
        IntStream.rangeClosed(1, 2 * t / 3).forEach(index -> agent.visit(BlockValue.B));

        double expected = Math.pow(kPlus / (kPlus + ((double) 3 / 10)), 2);
        System.out.println(expected);

        Map.Entry<BlockValue, Double> result = new StrategyPickUp().computeProba(agent);
        System.out.println(result.getKey());
        assertEquals(BlockValue.A, result.getKey());
        assertEquals(expected, result.getValue());
    }

    // STRATEGY --- PUT DOWN -------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Compute Proba Put Down - Full Block A")
    public void computeProba_putDown_whenOnlyBlock_A() {
        agent.setHolding(new Block(BlockValue.A));

        Map<Direction, Movable> perception = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(direction -> perception.put(direction, new Block(BlockValue.A)));

        double expected = Math.pow(1 / (k + 1), 2);
        assertEquals(expected, new StrategyPutDown().computeProba(agent, perception));
    }

    @Test
    @DisplayName("Compute Proba Put Down - 75% A")
    public void computeProba_putDown_whenBlocks_mostlyA() {
        agent.setHolding(new Block(BlockValue.A));

        Map<Direction, Movable> perception = new HashMap<>();
        perception.put(Direction.NORTH, new Block(BlockValue.A));
        perception.put(Direction.EAST, new Block(BlockValue.A));
        perception.put(Direction.WEST, new Block(BlockValue.A));
        perception.put(Direction.SOUTH, null);

        double expected = Math.pow(0.75 / (k + 0.75), 2);
        assertEquals(expected, new StrategyPutDown().computeProba(agent, perception));
    }

}
