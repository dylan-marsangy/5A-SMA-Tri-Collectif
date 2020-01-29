package org.polytech.agent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environnement.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentTest {

    @Test
    @DisplayName("Memory is a FIFO stack.")
    public void memoryIsFIFO() {
        Agent agent = new Agent(3);
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
