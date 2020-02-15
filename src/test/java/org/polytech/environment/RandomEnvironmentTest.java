package org.polytech.environment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Random Environment Tests")
public class RandomEnvironmentTest {

    private final int N = 5;
    private final int M = 5;
    private final int NB_BLOCKS_A = 5;
    private final int NB_BLOCKS_B = 5;

    private RandomEnvironment environment;

    @BeforeEach
    public void initializeEnvironment() {
        environment = new RandomEnvironment(N, M, NB_BLOCKS_A, NB_BLOCKS_B);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Count Elements on grid")
    public void hasCorrectNumbersOfEntities() {
        int agents = 0;
        int blocksA = 0;
        int blocksB = 0;

        for (int i = 0; i < environment.getGrid().length; i++) {
            for (int j = 0; j < environment.getGrid()[i].length; j++) {
                if (environment.getEntity(i, j) instanceof Agent) {
                    ++agents;
                } else if (environment.getEntity(i, j) instanceof Block) {
                    if (((Block) environment.getEntity(i, j)).getValue() == BlockValue.A) ++blocksA;
                    else ++blocksB;
                }
            }
        }

        assertEquals(0, agents);
        assertEquals(NB_BLOCKS_A, blocksA);
        assertEquals(NB_BLOCKS_B, blocksB);
    }

}
