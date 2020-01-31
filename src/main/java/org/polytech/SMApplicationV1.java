package org.polytech;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;

public class SMApplicationV1 {

    private static final int NUMBER_BLOCKS_A = 50;
    private static final int NUMBER_BLOCS_B = 50;
    private static final int NUMBER_AGENTS = 50;
    private static final int GRID_ROWS = 20; // N
    private static final int GRID_COLUMNS = 20; // M
    private static final int MEMORY_SIZE = 10; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    public static void main(String[] args) {
        Environnement environnement = new RandomEnvironnement(
                GRID_ROWS, GRID_COLUMNS, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                NUMBER_AGENTS, SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR,
                NUMBER_BLOCKS_A, NUMBER_BLOCS_B);

        new Thread(environnement).start();

    }
}
