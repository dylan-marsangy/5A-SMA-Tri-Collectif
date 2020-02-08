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

    public void createColonies() {
        Movable[][] grid = environnement.getGrid();
        int colonyNumber = 1;

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0 ; j < grid[i].length ; j++) {
                if (grid[i][j] instanceof Block) {
                    Block block = ((Block) grid[i][j]);

                    if (block.getValue() != BlockValue.ZERO) {
                        Map <BlockValue, Set<List<Integer>>> blockNeighbours = neighbours.getNeighboursAt(i, j);

                        for (BlockValue neighbourBlockValue : blockNeighbours.keySet()) {
                            for (List coordinates : blockNeighbours.get(neighbourBlockValue)) {
                                if (environnement.getEntity((int) coordinates.get(0), (int) coordinates.get(1)) instanceof Block) {
                                    Block currentNeighbour = (Block) environnement.getEntity((int) coordinates.get(0), (int) coordinates.get(1));

                                    if (currentNeighbour.getColonyNumber() != -1) {
                                        block.setColonyNumber(currentNeighbour.getColonyNumber());
                                    }
                                }
                            }
                        }

                        if (block.getColonyNumber() == -1) {
                            block.setColonyNumber(colonyNumber);
                            ++colonyNumber;
                        }
                    }
                }
            }
        }
    }

    public int countColonies() {
        Movable[][] grid = environnement.getGrid();
        int colonyNumber = 0;

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0 ; j < grid[i].length ; j++) {
                if (grid[i][j] instanceof Block) {
                    int blockColony = ((Block) grid[i][j]).getColonyNumber();
                    if (blockColony > colonyNumber) {
                        colonyNumber = blockColony;
                    }
                }
            }
        }

        return colonyNumber;
    }

    public HashMap<Integer, HashMap<BlockValue, Integer>> getNumberOfBlocksPerColony() {
        Movable[][] grid = environnement.getGrid();
        HashMap<Integer, HashMap<BlockValue, Integer>> numberOfBlocksPerColony = new HashMap<>();

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Block) {
                    Block block = (Block) grid[i][j];
                    int colonyNumber = block.getColonyNumber();
                    HashMap<BlockValue, Integer> numberOfBlocksWithValue;

                    if (colonyNumber != -1) {
                        if (numberOfBlocksPerColony.get(colonyNumber) == null) {
                            numberOfBlocksWithValue = new HashMap<>();
                            numberOfBlocksWithValue.put(block.getValue(), 1);
                        }
                        else {
                            numberOfBlocksWithValue = numberOfBlocksPerColony.get(colonyNumber);

                            numberOfBlocksWithValue.putIfAbsent(block.getValue(), 0);
                            numberOfBlocksWithValue.put(block.getValue(), numberOfBlocksPerColony.get(colonyNumber).get(block.getValue()) + 1);
                        }
                        numberOfBlocksPerColony.put(colonyNumber, numberOfBlocksWithValue);
                    }
                }
            }
        }

        return numberOfBlocksPerColony;
    }
}
