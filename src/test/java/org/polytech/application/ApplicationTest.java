package org.polytech.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.polytech.ExecutionParameters;
import org.polytech.SMAConstants;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;
import org.polytech.statistiques.Evaluation;
<<<<<<< HEAD
import org.polytech.statistiques.excel.ExcelGenerator;
=======
import org.polytech.statistiques.ExcelGenerator;
import org.polytech.utils.Color;
>>>>>>> :lipstick: Outpout console entre itérations amélioré + :truck: Output sim déplacé

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@DisplayName("Application Tests")
public class ApplicationTest {

    private static final int NUMBER_BLOCKS_A = 100;
    private static final int NUMBER_BLOCKS_B = 100;
    private static final int NUMBER_AGENTS = 20;
    private static final int GRID_ROWS = 40; // N
    private static final int GRID_COLUMNS = 40; // M
    private static final int MEMORY_SIZE = 15; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    private static final String RUN_ERROR = "Error (e)";
    private static final String RUN_KPLUS = "K+";
    private static final String RUN_KMINUS = "K-";
    private static final String RUN_SUCCESSIVE_MOVEMENTS = "Moves (i)";
    private static final String RUN_MEMORY_SIZE = "Memory (t)";
    private static final String RUN_GRID_COLUMNS = "Grid Cols (M)";
    private static final String RUN_GRID_ROWS = "Grid Rows (N)";
    private static final String RUN_AGENTS = "Agents";
    private static final String RUN_BLOCK_B = "Block B";
    private static final String RUN_BLOCK_A = "Block A";
    private static final String RUN_DEFAULT = "Default";
    private ExcelGenerator excelGenerator = ExcelGenerator.getInstance();

    @ParameterizedTest
    @DisplayName(RUN_ERROR)
    @ValueSource(doubles = {ERROR, 0.1, 0.5, 0.9})
    public void runApplication_numberBlocksA(double error) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, error, RUN_ERROR);
    }

    @ParameterizedTest
    @DisplayName(RUN_KPLUS)
    @ValueSource(doubles = {K_PLUS, 0.3, 0.5, 0.9})
    public void runApplication_kPlus(double kPlus) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, kPlus, ERROR, RUN_KPLUS);
    }

    @ParameterizedTest
    @DisplayName(RUN_KMINUS)
    @ValueSource(doubles = {0.1, K_MINUS, 0.9})
    public void runApplication_kMinus(double kMinus) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, kMinus, K_PLUS, ERROR, RUN_KMINUS);
    }

    @ParameterizedTest
    @DisplayName(RUN_SUCCESSIVE_MOVEMENTS)
    @ValueSource(ints = {SUCCESSIVE_MOVEMENTS, 2, 10})
    public void runApplication_successiveMovements(int successiveMovements) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, successiveMovements, K_MINUS, K_PLUS, ERROR, RUN_SUCCESSIVE_MOVEMENTS);
    }

    @ParameterizedTest
    @DisplayName(RUN_MEMORY_SIZE)
    @ValueSource(ints = {5, MEMORY_SIZE, 50})
    public void runApplication_memorySize(int memorySize) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                memorySize, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_MEMORY_SIZE);
    }

    @ParameterizedTest
    @DisplayName(RUN_GRID_COLUMNS)
    @ValueSource(ints = {50, GRID_COLUMNS, 100})
    public void runApplication_gridColumns(int gridColumns) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, gridColumns,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_GRID_COLUMNS);
    }

    @ParameterizedTest
    @DisplayName(RUN_GRID_ROWS)
    @ValueSource(ints = {50, GRID_ROWS, 100})
    public void runApplication_gridRows(int gridRows) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                gridRows, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_GRID_ROWS);
    }

    @ParameterizedTest
    @DisplayName(RUN_AGENTS)
    @ValueSource(ints = {5, NUMBER_AGENTS, 100})
    public void runApplication_numberAgents(int numberAgents) {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, numberAgents,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_AGENTS);
    }

    @ParameterizedTest
    @DisplayName(RUN_BLOCK_B)
    @ValueSource(ints = {10, NUMBER_BLOCKS_B, 300})
    public void runApplication_numberBlocksB(int numberBlocksB) {
        runSimulation(NUMBER_BLOCKS_A, numberBlocksB, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_BLOCK_B);
    }

    @ParameterizedTest
    @DisplayName(RUN_BLOCK_A)
    @ValueSource(ints = {10, NUMBER_BLOCKS_A, 300})
    public void runApplication_numberBlocksA(int numberBlocksA) {
        runSimulation(numberBlocksA, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_BLOCK_A);
    }

    @Test
    @DisplayName(RUN_DEFAULT)
    public void runApplication() {
        runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                GRID_ROWS, GRID_COLUMNS,
                MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR, RUN_DEFAULT);
    }

    private void runSimulation(int numberBlocksA, int numberBlocksB, int numberAgents,
                               int gridRows, int gridColumns,
                               int memorySize, int successiveMovements, double kMinus, double kPlus, double error, String executionName) {
        List<Evaluation> evaluations = new ArrayList<>();
        ExecutionParameters executionParameters = new ExecutionParameters(numberBlocksA, numberBlocksB, numberAgents,
         gridRows, gridColumns, memorySize, successiveMovements, kMinus, kPlus, error);

<<<<<<< HEAD
        for (int i = 0 ; i < SMAConstants.NB_RUN ; i++) {
=======
        for (int i = 0 ; i < NB_RUN ; i++) {
            // Affichage console pour différencier les différentes itérations.
            IntStream.rangeClosed(1, 3).forEach(index ->
                    System.out.println(
                            Color.CYAN +
                            "===============================================================================" +
                            "===============================================================================" +
                            "===============================================================================" +
                            Color.RESET));
            System.out.println(Color.CYAN + String.format("Itération n°%d", i+1) + Color.RESET);
            System.out.println(Color.CYAN + "-----------------------" + Color.RESET);

            // Instantiation de l'environnement
>>>>>>> :lipstick: Outpout console entre itérations amélioré + :truck: Output sim déplacé
            Environnement environnement = new RandomEnvironnement(
                    gridRows, gridColumns, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                    numberAgents, successiveMovements, memorySize, kPlus, kMinus, error,
                    numberBlocksA, numberBlocksB);

            // Quelques stats simples sur le remplissage de la grille
            System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                    (double) (numberBlocksA + numberBlocksB + numberAgents) / (gridRows * gridColumns) * 100,
                    (double) (numberAgents) / (gridRows * gridColumns) * 100,
                    (double) (numberBlocksA + numberBlocksB) / (gridRows * gridColumns) * 100));

            System.out.println();

            // Lancement de la simulation
            environnement.run(); // Non exécuté en mode Thread, sinon le programme se termine directement.

<<<<<<< HEAD
            Evaluation evaluation = new Evaluation(environnement, SMAConstants.NEIGHBOURHOOD_SIZE);
=======
            // Evaluation de l'environnement à la fin de la simulation
            Evaluation evaluation = new Evaluation(environnement);
>>>>>>> :lipstick: Outpout console entre itérations amélioré + :truck: Output sim déplacé
            evaluations.add(evaluation);
        }

        // Remplissage de la feuille Excel
        excelGenerator.fillExcel(evaluations, executionParameters, executionName);
    }
}
