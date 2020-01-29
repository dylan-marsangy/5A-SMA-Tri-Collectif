package org.polytech.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environnement.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentTest {

    private static final int t = 3;

    private Agent agent;

    @BeforeEach
    public void initializeAgent() {
        agent = new Agent(t);
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
}
