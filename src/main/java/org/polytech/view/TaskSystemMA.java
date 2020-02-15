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
        java.lang.System.out.print(system.getEnvironment());
        java.lang.System.out.println(String.format("0 / %d (0%%)", system.getNbIterations()));
        java.lang.System.out.println();

        int count = 0;
        while (count < system.getNbIterations()) {
            if (isCancelled()) return null;
            count++;

            system.execute();

            // Affichage de la grille résultante si nécessaire.
            if (frequency != 0d && count % frequency == 0) {
                java.lang.System.out.print(system.getEnvironment());
                java.lang.System.out.println(String.format("%d / %d (%.0f%%)",
                        count, system.getNbIterations(), (double) count / system.getNbIterations() * 100));
                java.lang.System.out.println();

                updateValue(system.copy());
            }
        }

        if (frequency == 0 || count % frequency != 0) {
            java.lang.System.out.print(system.getEnvironment());
            java.lang.System.out.println(String.format("%d / %d (100%%)", count, system.getNbIterations()));
            java.lang.System.out.println();
        }

        return system;
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
