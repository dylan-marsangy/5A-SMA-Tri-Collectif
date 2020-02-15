package org.polytech.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Agent Tests")
public class AgentTest {

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0d;

    private Agent agent;

    @BeforeEach
    public void initializeAgent() {
        Agent.cleanID();
        agent = new Agent(I, T, K_PLUS, K_MINUS, ERROR);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Identification By Auto-Incremented Long")
    public void agentsIdentification() {
        assertEquals(2L, new Agent(I, T, K_PLUS, K_MINUS, ERROR).getID());
    }

    // MEMORY ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("memory should behave like a FIFO stack")
    public void memoryIsFIFO() {
        agent.setMemorySize(3);
        agent.visit(new Block(BlockValue.A));
        agent.visit(new Block(BlockValue.B));
        agent.visit(new Block(BlockValue.B));
        agent.visit(new Block(BlockValue.A));

        assertEquals(agent.getMemory().stream().findFirst().orElse(BlockValue.ZERO), BlockValue.B,
                "Last element in the memory is not B.");
        assertEquals(agent.getMemory().peek(), BlockValue.B,
                "First element in the memory is not B.");
    }

}
