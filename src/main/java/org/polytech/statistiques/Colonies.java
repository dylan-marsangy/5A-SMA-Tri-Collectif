package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Colonies {
    private Environnement environnement;
    private Neighbours neighbours;
    private HashMap<Integer, HashMap<BlockValue, Integer>> numberOfBlocksPerColony;

    public Colonies(Environnement environnement, Neighbours neighbours) {
        this.environnement = environnement;
        this.neighbours = neighbours;
    }


    public void createColonies() {
        Movable[][] grid = environnement.getGrid();
        int colonyNumber = 1;

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0 ; j < grid[i].length ; j++) {
                if (grid[i][j] instanceof Block) {
                    Block block = ((Block) grid[i][j]);

                    if (block.getValue() != BlockValue.ZERO) {
                        Map<BlockValue, Set<List<Integer>>> blockNeighbours = neighbours.getNeighboursAt(i, j);

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

        computeNumberOfBlocksPerColony();
    }

    public int countColonies() {
        return numberOfBlocksPerColony.keySet().size();
    }

    public void computeNumberOfBlocksPerColony() {
        Movable[][] grid = environnement.getGrid();
        numberOfBlocksPerColony = new HashMap<>();

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
                        numberOfBlocksWithValue.putIfAbsent(BlockValue.A, 0);
                        numberOfBlocksWithValue.putIfAbsent(BlockValue.B, 0);
                        numberOfBlocksPerColony.put(colonyNumber, numberOfBlocksWithValue);
                    }
                }
            }
        }
    }

    public HashMap<Integer, HashMap<BlockValue, Integer>> getNumberOfBlocksPerColony() {
        return numberOfBlocksPerColony;
    }

    public double getAverageColoniesBlockWithValue(BlockValue blockValue) {

        List<Double> proportions = new ArrayList<>();

        for (Integer colonyNumber : numberOfBlocksPerColony.keySet()) {
            HashMap<BlockValue, Integer> colony = numberOfBlocksPerColony.get(colonyNumber);
            double searchedBlocks = colony.get(blockValue);
            double totalBlocks = colony.get(BlockValue.A) + colony.get(BlockValue.B);

            proportions.add(searchedBlocks / totalBlocks);
        }

        double sum = 0;

        for (Double proportion : proportions) {
            sum+= proportion;
        }

        return sum / proportions.size();
    }
}
