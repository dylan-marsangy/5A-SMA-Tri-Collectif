package org.polytech.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private static final int MEMORY_SIZE = 15; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    @Test
    @DisplayName("Run Application")
    public void runApplication() {
        Environnement environnement = new RandomEnvironnement(
                GRID_ROWS, GRID_COLUMNS, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID,
                NUMBER_AGENTS, SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR,
                NUMBER_BLOCKS_A, NUMBER_BLOCKS_B);

        System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

        System.out.println();
//        new Thread(environnement).start();
        environnement.run();
    }
}
