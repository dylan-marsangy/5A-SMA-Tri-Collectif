package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Neighbours {
    private Environnement environnement;
    private Map<BlockValue, Map<BlockValue, Integer>> neighboursNumberByBlockValue;

    public Neighbours(Environnement environnement) {
        this.environnement = environnement;

        this.neighboursNumberByBlockValue = new HashMap<>();
        neighboursNumberByBlockValue.put(BlockValue.A, createEmptyNeighbours());
        neighboursNumberByBlockValue.put(BlockValue.B, createEmptyNeighbours());
    }


    public Map<BlockValue, Integer> createEmptyNeighbours() {
        Map<BlockValue, Integer> neighbours = new HashMap<>();
        neighbours.put(BlockValue.A, 0);
        neighbours.put(BlockValue.B, 0);
        neighbours.put(BlockValue.ZERO, 0);

        return neighbours;
    }

    public void calculateNeighbours() {
        Movable[][] grid = environnement.getGrid();

        for (int i = 0 ; i < grid.length ; i++) {
            for (int j = 0 ; j < grid[i].length ; j++) {
                if (grid[i][j] instanceof Block) {
                    Block block = (Block) grid[i][j];

                    if (block.getValue() != BlockValue.ZERO) {
                        Map<BlockValue, Integer> currentNeighbours = getNeighboursNumberAt(i, j);

                        Map<BlockValue, Integer> totalNeighbours = neighboursNumberByBlockValue.get(block.getValue());
                        Integer totalA = totalNeighbours.get(BlockValue.A) + currentNeighbours.get(BlockValue.A);
                        Integer totalB = totalNeighbours.get(BlockValue.B) + currentNeighbours.get(BlockValue.B);
                        Integer totalZERO = totalNeighbours.get(BlockValue.ZERO) + currentNeighbours.get(BlockValue.ZERO);

                        totalNeighbours.put(BlockValue.A, totalA);
                        totalNeighbours.put(BlockValue.B, totalB);
                        totalNeighbours.put(BlockValue.ZERO, totalZERO);

                        neighboursNumberByBlockValue.put(block.getValue(), totalNeighbours);
                    }
                }
            }
        }
    }

    public Map<BlockValue, Integer> getNeighboursNumberAt(int x, int y) {
        Map<BlockValue, Integer> neighbours = createEmptyNeighbours();

        for (int i = x - 1 ; i <= x + 1 ; i++) {
            for (int j = y - 1 ; j <= y + 1 ; j++) {
                if (environnement.isInside(i, j)) {
                    if (environnement.getEntity(i, j) instanceof Block) {
                        if (i != x || j != y) {
                            BlockValue blockValue = ((Block) environnement.getEntity(i, j)).getValue();
                            neighbours.put(blockValue, neighbours.get(blockValue)+1);
                        }
                    }
                }
            }
        }

        return neighbours;
    }

    public Map<BlockValue, Set<List<Integer>>> getNeighboursAt(int x, int y) {
        Map<BlockValue, Set<List<Integer>>> neighbours = new HashMap<>();
        neighbours.put(BlockValue.A, new HashSet<>(new ArrayList<>()));
        neighbours.put(BlockValue.B, new HashSet<>(new ArrayList<>()));
        neighbours.put(BlockValue.ZERO, new HashSet<>(new ArrayList<>()));

        for (int i = x - 1 ; i <= x + 1 ; i++) {
            for (int j = y - 1 ; j <= y + 1 ; j++) {
                if (environnement.isInside(i, j)) {
                    if (environnement.getEntity(i, j) instanceof Block) {
                        if (i != x || j != y) {
                            BlockValue blockValue = ((Block) environnement.getEntity(i, j)).getValue();
                            ArrayList<Integer> coordinates = new ArrayList<>();
                            coordinates.add(0, i);
                            coordinates.add(1, j);
                            neighbours.get(blockValue).add(coordinates);
//                            neighbours.put(blockValue, (neighbours.get(blockValue)).add(coordinates));
                        }
                    }
                }
            }
        }

        return neighbours;
    }

    public Map<BlockValue, Map<BlockValue, Integer>> getNeighboursNumberByBlockValue() {
        return neighboursNumberByBlockValue;
    }

    public Map<BlockValue, Integer> getNeighboursNumberByBlockValue(BlockValue blockValue) {
        return neighboursNumberByBlockValue.get(blockValue);
    }

    public Integer getNeighboursWithValue(BlockValue centerBlockValue, BlockValue neighbourBlockValue) {
        return neighboursNumberByBlockValue.get(centerBlockValue).get(neighbourBlockValue);
    }
}
