import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;

import java.util.stream.IntStream;

public class SMApplicationV1 {

    private static final int NUMBER_BLOCKS_A = 100;
    private static final int NUMBER_BLOCS_B = 100;
    private static final int NUMBER_AGENTS = 50;
    private static final int GRID_ROWS = 40; // N
    private static final int GRID_COLUMNS = 40; // M
    private static final int MEMORY_SIZE = 10; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+

    public static void main(String[] args) {
        Environnement environnement = new RandomEnvironnement(
                GRID_ROWS, GRID_COLUMNS,
                NUMBER_AGENTS, SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS,
                NUMBER_BLOCKS_A, NUMBER_BLOCS_B);

        new Thread(environnement).start();

    }
}
