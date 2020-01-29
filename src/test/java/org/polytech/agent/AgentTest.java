package org.polytech.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentTest {

    private static final int t = 3;
    private static final double k = 0.3;
    private static final double kPlus = 0.1;

    private Agent agent;

    @BeforeEach
    public void initializeAgent() {
        agent = new Agent(t, kPlus, k);
    }

    // MEMORY ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Memory is a FIFO stack.")
    public void memoryIsFIFO() {
        agent.visit(BlockValue.A);
        agent.visit(BlockValue.B);
        agent.visit(BlockValue.A);
        agent.visit(BlockValue.B);
        assertEquals(agent.getMemory().stream().findFirst().orElse(BlockValue.ZERO), BlockValue.B,
                "Last element in the memory is not B.");
        assertEquals(agent.getMemory().peek(), BlockValue.B,
                "First element in the memory is not B.");
    }

//    @Test
//    @DisplayName("Proba is 1.")
//    public void shouldTakeIt() {
//        Block block = new Block(BlockValue.A);
//
//        for (int i = 0; i < t ; i++) {
//            agent.getMemory().add(BlockValue.B);
//        }
//
//        double proba = agent.computeFPickUp(block);
//
//        assertEquals(1, proba);
//    }
//
//    @Test
//    @DisplayName("Proba is 1.")
//    public void shouldNTakeIt() {
//        Block block = new Block(BlockValue.A);
//
//        for (int i = 0; i < t ; i++) {
//            agent.getMemory().add(BlockValue.B);
//        }
//
//        double proba = agent.computeFPickUp(block);
//
//        assertEquals(1, proba);
//    }
}
