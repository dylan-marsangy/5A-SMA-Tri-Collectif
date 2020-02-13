package org.polytech;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;
import org.polytech.statistiques.Evaluation;
import org.polytech.statistiques.excel.ExcelGenerator;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SMApplicationV1 implements Callable<Integer> {

    private static final int NUMBER_BLOCKS_A = 50;
    private static final int NUMBER_BLOCKS_B = 50;
    private static final int NUMBER_AGENTS = 50;
    private static final int GRID_ROWS = 20; // N
    private static final int GRID_COLUMNS = 20; // M
    private static final int MEMORY_SIZE = 10; // t
    private static final int SUCCESSIVE_MOVEMENTS = 1; // i
    private static final double K_MINUS = 0.3; // k-
    private static final double K_PLUS = 0.1; // k+
    private static final double ERROR = 0d; // e

    @Option(names = {"-f", "--frequency"},
            description = "fréquence d'affichage de l'environnement (default : ${DEFAULT-VALUE})",
            defaultValue = "0.25"
    )
    private double frequency;

    @Option(names = {"-i", "--iteration"},
            description = "nombre d'itérations de l'algorithme de tri (default : ${DEFAULT-VALUE})",
            defaultValue = "570000"
    )
    private Integer iterations;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SMApplicationV1()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        try {
            List<Evaluation> evaluations = new ArrayList<>();
            ExcelGenerator excelGenerator = ExcelGenerator.getInstance();
            ExecutionParameters executionParameters = new ExecutionParameters(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                    GRID_ROWS, GRID_COLUMNS, MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);

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

            excelGenerator.fillExcel(evaluations, executionParameters, "SMApplicationV1");
            return 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
