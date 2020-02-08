package org.polytech.statistiques;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Evaluation test")
public class EvaluationTest {
    private final int N = 5;
    private final int M = 5;
    private final int NB_AGENTS = 0;
    private final int NB_BLOCKS_A = 0;
    private final int NB_BLOCKS_B = 0;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environnement environnement;
    private Evaluation evaluation;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(N, M, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                NB_AGENTS, I, T, K_PLUS, K_MINUS, ERROR,
                NB_BLOCKS_A, NB_BLOCKS_B);

        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Block(BlockValue.ZERO), 0, 1);
        environnement.insert(new Block(BlockValue.B), 0, 2);

        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 1);
        environnement.insert(new Block(BlockValue.ZERO), 2, 2);

        environnement.insert(new Block(BlockValue.A), 0, 4);
        environnement.insert(new Block(BlockValue.A), 1, 4);
        environnement.insert(new Block(BlockValue.B), 3, 4);
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environnement);

        initializeEvaluation();
    }

    public void initializeEvaluation() {
        this.evaluation = new Evaluation(this.environnement);
    }

    @Test
    @DisplayName("Check if the numbers of blocks in the environment are correct")
    public void getNumberOfBlocksPerValue() {
        assertEquals(3, evaluation.getTotalBlockWithValue(BlockValue.B));
        assertEquals(3, evaluation.getTotalBlockWithValue(BlockValue.A));
    }

    @Test
    @DisplayName("Check if the number of colonies is correct")
    public void getNumberOfColonies() {
        assertEquals(4, evaluation.getNumberOfColonies());
    }

    @Test
    @DisplayName("Check if the percentages of neighbours are correct")
    public void getPercentagesOfComputedNeighbours() {
        assertEquals(2.0/4.0, evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.A));
        assertEquals(2.0/4.0, evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.B));
        assertEquals(2.0/4.0, evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.A));
        assertEquals(0, evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.B));
    }
}
