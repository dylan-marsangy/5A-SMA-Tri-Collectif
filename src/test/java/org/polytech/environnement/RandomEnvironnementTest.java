package org.polytech.environnement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Random Environnement Tests")
public class RandomEnvironnementTest {

    private final int N = 5;
    private final int M = 5;
    private final int NB_BLOCKS_A = 5;
    private final int NB_BLOCKS_B = 5;

    private RandomEnvironnement environnement;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new RandomEnvironnement(N, M, NB_BLOCKS_A, NB_BLOCKS_B);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Count Elements on grid")
    public void hasCorrectNumbersOfEntities() {
        int agents = 0;
        int blocksA = 0;
        int blocksB = 0;

        for (int i = 0; i < environnement.getGrid().length; i++) {
            for (int j = 0; j < environnement.getGrid()[i].length; j++) {
                if (environnement.getEntity(i, j) instanceof Agent) {
                    ++agents;
                } else if (environnement.getEntity(i, j) instanceof Block) {
                    if (((Block) environnement.getEntity(i, j)).getValue() == BlockValue.A) ++blocksA;
                    else ++blocksB;
                }
            }
        }

        assertEquals(0, agents);
        assertEquals(NB_BLOCKS_A, blocksA);
        assertEquals(NB_BLOCKS_B, blocksB);
    }

}
