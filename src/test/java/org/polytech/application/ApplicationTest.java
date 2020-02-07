package org.polytech.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.polytech.SMAConstants;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;


@DisplayName("Application Tests")
public class ApplicationTest {

    private static final int NUMBER_BLOCKS_A = 100;
    private static final int NUMBER_BLOCKS_B = 100;
    private static final int NUMBER_AGENTS = 20;
    private static final int GRID_ROWS = 40; // N
    private static final int GRID_COLUMNS = 40; // M
    private static final int MEMORY_SIZE = 10; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    @ParameterizedTest
    @DisplayName("Run Application - Error (e)")
    @ValueSource(doubles = {ERROR, 0.1, 0.5, 0.9})
    public void runApplication_numberBlocksA(double error) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, error);
    }

    @ParameterizedTest
    @DisplayName("Run Application - K+")
    @ValueSource(doubles = {K_PLUS, 0.3, 0.5, 0.9})
    public void runApplication_kPlus(double kPlus) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, kPlus, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - K-")
    @ValueSource(doubles = {0.1, K_MINUS, 0.9})
    public void runApplication_kMinus(double kMinus) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, kMinus, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Successive Movements (i)")
    @ValueSource(ints = {SUCCESSIVE_MOVEMENTS, 2, 10})
    public void runApplication_successiveMovements(int successiveMovements) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, successiveMovements, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Memory Size (t)")
    @ValueSource(ints = {5, MEMORY_SIZE, 50})
    public void runApplication_memorySize(int memorySize) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                memorySize, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Grid Columns (M)")
    @ValueSource(ints = {50, GRID_COLUMNS, 100})
    public void runApplication_gridColumns(int gridColumns) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, gridColumns,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Grid Rows (N)")
    @ValueSource(ints = {50, GRID_ROWS, 100})
    public void runApplication_gridRows(int gridRows) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                gridRows, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Agents")
    @ValueSource(ints = {5, NUMBER_AGENTS, 100})
    public void runApplication_numberAgents(int numberAgents) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, numberAgents,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Block B")
    @ValueSource(ints = {10, NUMBER_BLOCKS_B, 300})
    public void runApplication_numberBlocksB(int numberBlocksB) {
        runSimulation(NUMBER_BLOCKS_A, numberBlocksB, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @ParameterizedTest
    @DisplayName("Run Application - Block A")
    @ValueSource(ints = {10, NUMBER_BLOCKS_A, 300})
    public void runApplication_numberBlocksA(int numberBlocksA) {
        runSimulation(numberBlocksA, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    @Test
    @DisplayName("Run Application - Default")
    public void runApplication() {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
    }

    private void runSimulation(int numberBlocksA, int numberBlocksB, int numberAgents,
                               int gridRows, int gridColumns,
                               int memorySize, int successiveMovements, double kMinus, double kPlus, double error) {
        Environnement environnement = new RandomEnvironnement(
                gridRows, gridColumns, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                numberAgents, successiveMovements, memorySize, kPlus, kMinus, error,
                numberBlocksA, numberBlocksB);

        System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                (double) (numberBlocksA + numberBlocksB + numberAgents) / (gridRows * gridColumns) * 100,
                (double) (numberAgents) / (gridRows * gridColumns) * 100,
                (double) (numberBlocksA + numberBlocksB) / (gridRows * gridColumns) * 100));

        System.out.println();
        environnement.run(); // Non exécuté en mode Thread, sinon le programme se termine directement.
    }
}
