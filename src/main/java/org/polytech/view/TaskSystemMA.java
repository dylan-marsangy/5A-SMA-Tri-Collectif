package org.polytech.view;

import javafx.concurrent.Task;
import org.polytech.agent.Agent;
import org.polytech.system.SystemMA;
import org.polytech.system.SystemMAFactory;

import static org.polytech.SMApplicationV1.*;

public class TaskSystemMA extends Task<SystemMA> {

    private SystemMA system;

    public TaskSystemMA() {
        Agent.cleanID();

        // Génération du système
        this.system = SystemMAFactory.instantiateRandom(
                GRID_ROWS, GRID_COLUMNS, NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR);
    }

    @Override
    protected SystemMA call() throws Exception {
        int frequency = (int) (system.getNbIterations() * system.getFrequencyDiplayGrid());
        int count = 0;

        displayProgress(count);

        while (count < system.getNbIterations()) {
            if (isCancelled()) return null;
            count++;

            system.execute();
            updateProgress(count, system.getNbIterations());

            // Affichage de la grille résultante si nécessaire.
            if (frequency != 0d && count % frequency == 0) {
                displayProgress(count);
                updateValue(system.copy());
            }
        }

        if (frequency == 0 || count % frequency != 0) {
            displayProgress(count);
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
        System.out.println(String.format("%d / %d (%.0f%%)", workDone, system.getNbIterations(), (double) workDone / system.getNbIterations() * 100));
        System.out.println();
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        System.out.println("Algorithme terminé !");
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

    public SystemMA getSystem() {
        return system.copy();
    }

}
