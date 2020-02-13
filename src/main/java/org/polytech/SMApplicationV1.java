package org.polytech;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;
import org.polytech.statistiques.Evaluation;
import org.polytech.statistiques.excel.ExcelGenerator;
import org.polytech.utils.Color;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

@Command(name = "sma-tri-collectif",
        version = "SMA Tri Collectif 1.0",
        description = "Effectue un tri collectif par un SMA de blocs de 2 types de valeur.",
        mixinStandardHelpOptions = true)
public class SMApplicationV1 implements Callable<Integer> {

    private static final int NUMBER_BLOCKS_A = 200;
    private static final int NUMBER_BLOCKS_B = 200;
    private static final int NUMBER_AGENTS = 20;
    private static final int GRID_ROWS = 50; // N
    private static final int GRID_COLUMNS = 50; // M
    private static final int MEMORY_SIZE = 10; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    @Option(names = {"-f", "--frequency"},
            description = "frequence d'affichage de l'environnement (default : ${DEFAULT-VALUE})",
            defaultValue = "0.25"
    )
    private double frequency;

    @Option(names = {"-i", "--iteration"},
            description = "nombre d'iterations de l'algorithme de tri (default : ${DEFAULT-VALUE})",
            defaultValue = "1600000"
    )
    private Integer iterations;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SMApplicationV1()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        if (iterations <= 0) {
            System.err.println("Le nombre d'iterations doit etre strictement positif.");
            return 1;
        }

        if (frequency < 0 || frequency > 1) {
            System.err.println("La frequence doit etre comprise entre 0 et 1.");
            return 1;
        }

        try {
            List<Evaluation> evaluations = new ArrayList<>();

            ExcelGenerator excelGenerator = ExcelGenerator.getInstance();
            ExecutionParameters executionParameters = new ExecutionParameters(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                    GRID_ROWS, GRID_COLUMNS, MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);

            for (int i = 0; i < SMAConstants.NB_RUN; i++) {
                // Affichage console pour différencier les différentes itérations.
                IntStream.rangeClosed(1, 3).forEach(index ->
                        System.out.println(
                                Color.CYAN +
                                        "===============================================================================" +
                                        "===============================================================================" +
                                        "===============================================================================" +
                                        Color.RESET));
                System.out.println(Color.CYAN + String.format("Execution n°%d", i + 1) + Color.RESET);
                System.out.println(Color.CYAN + "-----------------------" + Color.RESET);

                Environnement environnement = new RandomEnvironnement(
                        GRID_ROWS, GRID_COLUMNS, iterations, frequency,
                        NUMBER_AGENTS, SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR,
                        NUMBER_BLOCKS_A, NUMBER_BLOCKS_B);

                System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                        (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                        (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                        (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

                System.out.println();
                environnement.run();

                Evaluation evaluation = new Evaluation(environnement, SMAConstants.NEIGHBOURHOOD_SIZE);
                evaluations.add(evaluation);
            }

            excelGenerator.fillExcel(evaluations, executionParameters, "SMApplicationV1");
            return 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
