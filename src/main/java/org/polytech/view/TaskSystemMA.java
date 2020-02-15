package org.polytech.view;

import javafx.concurrent.Task;
import org.polytech.system.SystemMA;
import org.polytech.system.SystemMAFactory;

import static org.polytech.SMApplicationV1.*;
import static org.polytech.SMApplicationV1.ERROR;

public class TaskSystemMA extends Task<SystemMA> implements ViewSystemObserver {

    private SystemMA system;

    public TaskSystemMA() {

        // Génération du système
        this.system = SystemMAFactory.instantiateRandom(
                GRID_ROWS, GRID_COLUMNS, NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR);

        system.addObserver(this);
    }

    @Override
    protected SystemMA call() throws Exception {
        system.run();
        return system;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        System.out.println("Algorithme terminé !");
    }

    @Override protected void cancelled() {
        super.cancelled();

        updateMessage("Algorithme interrompu !");
    }

    @Override protected void failed() {
        super.failed();

        updateMessage("Une erreur imprévue est survenue !");
    }

    public SystemMA getSystem() {
        return system;
    }

    @Override
    public void updateViewSystemObserver() {
        updateValue(system.save());
    }

}
