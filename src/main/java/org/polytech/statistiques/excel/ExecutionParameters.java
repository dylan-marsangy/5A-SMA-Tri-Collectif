package org.polytech.statistiques.excel;


/**
 * Classe spécifiant les paramètres d'exécution utilisés pendant une simulation.
 */
public class ExecutionParameters {

    private final int NUMBER_RUNS;

    private final int NUMBER_BLOCKS_A;
    private final int NUMBER_BLOCKS_B;
    private final int NUMBER_AGENTS;
    private final int GRID_ROWS; // N
    private final int GRID_COLUMNS; // M
    private final int MEMORY_SIZE; // t
    private final int SUCCESSIVE_MOVEMENTS; // i
    private final double K_MINUS; // k-
    private final double K_PLUS; // k+
    private final double ERROR; // e

    public ExecutionParameters(int numberRuns,
                               int numberBlocksA, int numberBlocksB, int numberAgents,
                               int gridRows, int gridColumns,
                               int memorySize, int successiveMovements, double kMinus, double kPlus, double error) {
        NUMBER_RUNS = numberRuns;
        NUMBER_BLOCKS_A = numberBlocksA;
        NUMBER_BLOCKS_B = numberBlocksB;
        NUMBER_AGENTS = numberAgents;
        GRID_ROWS = gridRows;
        GRID_COLUMNS = gridColumns;
        MEMORY_SIZE = memorySize;
        SUCCESSIVE_MOVEMENTS = successiveMovements;
        K_MINUS = kMinus;
        K_PLUS = kPlus;
        ERROR = error;
    }

    public int getNumberRuns() {
        return NUMBER_RUNS;
    }

    public int getNumberBlocksA() {
        return NUMBER_BLOCKS_A;
    }

    public int getNumberBlocksB() {
        return NUMBER_BLOCKS_B;
    }

    public int getNumberAgents() {
        return NUMBER_AGENTS;
    }

    public int getGridRows() {
        return GRID_ROWS;
    }

    public int getGridColumns() {
        return GRID_COLUMNS;
    }

    public int getMemorySize() {
        return MEMORY_SIZE;
    }

    public int getSuccessiveMovements() {
        return SUCCESSIVE_MOVEMENTS;
    }

    public double getkMinus() {
        return K_MINUS;
    }

    public double getkPlus() {
        return K_PLUS;
    }

    public double getError() {
        return ERROR;
    }
}
