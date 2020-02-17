package org.polytech;

import org.polytech.statistiques.Evaluation;
import org.polytech.statistiques.excel.ExcelGenerator;
import org.polytech.statistiques.excel.ExecutionParameters;
import org.polytech.system.SystemMA;
import org.polytech.system.SystemMAFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static org.polytech.SMAConstants.*;

@Command(name = "sma-tri-collectif",
        version = "SMA Tri Collectif 1.0",
        description = "Effectue un tri collectif par un SMA de blocs de 2 types de valeur.",
        mixinStandardHelpOptions = true)
public class SMApplicationV1 implements Callable<Integer> {

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
            ExcelGenerator excelGenerator = ExcelGenerator.getInstance();
            ExecutionParameters executionParameters = new ExecutionParameters(
                    1,
                    NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                    GRID_ROWS, GRID_COLUMNS,
                    MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);

            // Génération du système
            SystemMA system = SystemMAFactory.instantiateRandom(
                    GRID_ROWS, GRID_COLUMNS, NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                    SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR);

            System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                    (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                    (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                    (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

            System.out.println();
            system.run(iterations, frequency); // Exécution du système

            Evaluation evaluation = new Evaluation(system.getEnvironment(), SMAConstants.NEIGHBOURHOOD_SIZE);
            List<Evaluation> evaluations = Collections.singletonList(evaluation);

            excelGenerator.save(evaluations, executionParameters, "SMApplicationV1");
            return 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
