package org.polytech.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.statistiques.Evaluation;
import org.polytech.statistiques.excel.ExcelGenerator;
import org.polytech.statistiques.excel.ExecutionParameters;
import org.polytech.system.SystemMA;
import org.polytech.system.SystemMAFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.polytech.SMAConstants.*;

public class ServiceSystemMA extends Service<SystemMA> {

    /**
     * Système exécuté par la tâche actuelle.
     */
    private SystemMA system;

    /**
     * Fréquence de la mise à jour de l'UI.
     */
    private DoubleProperty frequencyUpdateUI = new SimpleDoubleProperty(1d);

    /**
     * Nombre maximal d'itérations d'un algorithme.
     */
    private IntegerProperty maxIterations = new SimpleIntegerProperty(SMAConstants.ITERATION_LOOPS);

    /**
     * Réinitialise les inputs de l'UI.
     */
    private void initInputs() {
        frequencyUpdateUI.set(1d);
        maxIterations.set(SMAConstants.ITERATION_LOOPS);
    }

    @Override
    protected Task<SystemMA> createTask() {
        initInputs();
        return new TaskSystemMA();
    }

    /**
     * Renvoie une copie du système actuellement exécuté par la tâche.
     * @return {@link #system}
     */
    public SystemMA getSystem() {
        return system.copy();
    }

    public DoubleProperty frequencyUpdateUIProperty() {
        return this.frequencyUpdateUI;
    }

    public IntegerProperty maxIterationsProperty() {
        return this.maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations.set(maxIterations);
    }

    /**
     * Tâche exécutant un algorithme de tri.
     */
    public class TaskSystemMA extends Task<SystemMA> {

        /**
         * Initialise un système aléatoire.
         * @return Système généré.
         */
        private SystemMA initSystem() {
            // Réinitialisation à 0 de l'ID des agents entre deux exécutions de l'algorithme
            Agent.cleanID();

            // Génération du système
            return SystemMAFactory.instantiateRandom(
                    GRID_ROWS, GRID_COLUMNS, NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                    SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR);
        }

        @Override
        protected SystemMA call() throws Exception {
            system = initSystem();

            int count = 0;
            displayProgress(count);

            Random random = new Random();
            while (count < maxIterations.get()) {
                if (isCancelled()) return null;
                count++;

                system.execute();
                updateProgress(count, maxIterations.get());

                // Mise à jour de l'UI si nécessaire.
                if (random.nextDouble() < frequencyUpdateUI.get()) {
                    updateValue(system.copy());
                }

                // Affichage de la grille résultante en console si nécessaire.
                if (count % (SMAConstants.FREQUENCY_DISPLAY_GRID * maxIterations.get()) == 0) {
                    displayProgress(count);
                }
            }

            return system;
        }

        /**
         * Affiche en console l'avancement de l'exécution de l'algorithme.
         *
         * @param workDone Étape actuelle de l'algorithme
         */
        private void displayProgress(long workDone) {
            System.out.print(system.getEnvironment());
            System.out.println(String.format("%d / %d (%.0f%%)", workDone, maxIterations.get(), (double) workDone / maxIterations.get() * 100));
            System.out.println();
        }

        @Override
        protected void succeeded() {
            super.succeeded();

            System.out.println("Algorithme terminé !");
            System.out.println("État final de l'environnement :");
            displayProgress(maxIterations.get());

            System.out.println("Enregistrement des statistiques...");
            ExcelGenerator excelGenerator = ExcelGenerator.getInstance();
            ExecutionParameters executionParameters = new ExecutionParameters(
                    1,
                    NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                    GRID_ROWS, GRID_COLUMNS,
                    MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, ERROR);
            List<Evaluation> evaluations = Collections.singletonList(new Evaluation(system.getEnvironment()));
            excelGenerator.save(evaluations, executionParameters, "SMApplicationV1");
        }

        @Override
        protected void cancelled() {
            super.cancelled();

            updateMessage("Algorithme interrompu !");
        }

        @Override
        protected void failed() {
            super.failed();

            updateMessage("Une erreur imprévue est survenue !");
        }
    }
}
