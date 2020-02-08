package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Evaluation {
    private Environnement environnement;
    private Neighbours neighbours;
    private Colonies colonies;

    public Evaluation(Environnement environnement) {
        this.environnement = environnement;
        this.neighbours = new Neighbours(environnement);
        this.neighbours.calculateNeighbours();
        this.colonies = new Colonies(environnement, neighbours);
        this.colonies.createColonies();
    }

    public int getTotalBlockWithValue(BlockValue blockValue) {
        Movable[][] grid = environnement.getGrid();
        int sum = 0;

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0 ; j < grid[i].length ; j++) {
                if (grid[i][j] instanceof Block && ((Block) grid[i][j]).getValue() == blockValue) {
                    ++sum;
                }
            }
        }
        return sum;
    }

    public int getNumberOfColonies() {
        return colonies.countColonies();
    }

    public double getNeighborhoodPercentage(BlockValue firstBlockValue, BlockValue secondBlockValue) {
        double searchedNeighbours = neighbours.getNeighboursWithValue(firstBlockValue, secondBlockValue);

        if (!firstBlockValue.equals(secondBlockValue)) {
            searchedNeighbours+= neighbours.getNeighboursWithValue(secondBlockValue, firstBlockValue);
        }

        double totalNeighbours = neighbours.getTotalOfComputedNeighbours();

        return searchedNeighbours / totalNeighbours;
    }

    public double getAverageColoniesBlockWithValue(BlockValue blockValue) {
        return colonies.getAverageColoniesBlockWithValue(blockValue);
    }
}
