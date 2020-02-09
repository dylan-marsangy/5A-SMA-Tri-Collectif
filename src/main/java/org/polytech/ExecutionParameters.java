package org.polytech;

public class ExecutionParameters {
    private int NUMBER_BLOCKS_A;
    private int NUMBER_BLOCKS_B;
    private int NUMBER_AGENTS;
    private int GRID_ROWS; // N
    private int GRID_COLUMNS; // M
    private int MEMORY_SIZE; // t
    private int SUCCESSIVE_MOVEMENTS; // i
    private double K_MINUS; // k-
    private double K_PLUS; // k+
    private double ERROR; // e

    public ExecutionParameters(int numberBlocksA, int numberBlocksB, int numberAgents, int gridRows, int gridColumns, int memorySize,
                               int successiveMovements, double kMinus, double kPlus, double error) {
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
