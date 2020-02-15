package org.polytech.statistiques;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.environment.Environment;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Evaluation test")
public class EvaluationTest {
    private final int N = 5;
    private final int M = 5;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environment environment;
    private Evaluation evaluation;

    @BeforeEach
    public void initializeEnvironment() {
        environment = new Environment(N, M);

        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        initializeEvaluation();
    }

    public void initializeEvaluation() {
        this.evaluation = new Evaluation(this.environment);
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
        assertEquals(2.0 / 4.0, evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.A));
        assertEquals(2.0 / 4.0, evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.B));
        assertEquals(2.0 / 4.0, evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.A));
        assertEquals(0, evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.B));
    }

    @Test
    @DisplayName("Check if the averages of blocks in the colonies are correct")
    public void getAveragesOfBlocksInColonies() {
        assertEquals(15.0 / 40.0, evaluation.getAverageColoniesBlockWithValue(BlockValue.A));
        assertEquals(25.0 / 40.0, evaluation.getAverageColoniesBlockWithValue(BlockValue.B));
    }

    @Test
    @DisplayName("Check if the average size of the colonies is correct")
    public void getAverageSizeOfColonies() {
        assertEquals(1.5, evaluation.getAverageSizeOfColonies());
    }
}
