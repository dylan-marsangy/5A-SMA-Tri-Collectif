package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Evaluation {
    private Environnement environnement;
    private Neighbours neighbours;

    public Evaluation(Environnement environnement) {
        this.environnement = environnement;
        this.neighbours = new Neighbours(environnement);
        this.neighbours.calculateNeighbours();
    }

    private int countColonies() {
        return 0;
    }
}
