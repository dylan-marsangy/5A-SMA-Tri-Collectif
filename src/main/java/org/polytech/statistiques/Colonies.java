package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Colonies {
    private Environnement environnement;
    private Neighbours neighbours;

    // HashMap associant le numéro de colonie à une HashMap contenant le nombre de blocs A et B contenus dans la colonie
    private HashMap<Integer, HashMap<BlockValue, Integer>> numberOfBlocksPerColony;

    public Colonies(Environnement environnement, Neighbours neighbours) {
        this.environnement = environnement;
        this.neighbours = neighbours;
    }

    /**
     * Crée les colonies à partir de l'environnement
     */
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
                                    // récupère les voisins du bloc courant
                                    Block currentNeighbour = (Block) environnement.getEntity((int) coordinates.get(0), (int) coordinates.get(1));

                                    // si l'un des voisins a déjà une colonie attribuée, on assigne la même au bloc courant
                                    if (currentNeighbour.getColonyNumber() != -1) {
                                        block.setColonyNumber(currentNeighbour.getColonyNumber());
                                    }
                                }
                            }
                        }
                        // si aucun voisin n'a de colonie attribuée, alors il s'agit d'une nouvelle colonie
                        if (block.getColonyNumber() == -1) {
                            block.setColonyNumber(colonyNumber);
                            ++colonyNumber;
                        }
                    }
                }
            }
        }

        // Compte le nombre de blocs par colonie
        computeNumberOfBlocksPerColony();
    }


    /**
     * Retourne le nombre de colonies dans l'environnement
     * @return int
     */
    public int countColonies() {
        return numberOfBlocksPerColony.keySet().size();
    }


    /**
     * Compte le nombre de blocs de chaque type par colonie
     */
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

    /**
     * Getter de numberOfBlocksPerColony
     * @return HashMap<Integer, HashMap<BlockValue, Integer>>
     */
    public HashMap<Integer, HashMap<BlockValue, Integer>> getNumberOfBlocksPerColony() {
        return numberOfBlocksPerColony;
    }


    /**
     * Calcule le nombre moyen de blocs de type BlockValue dans une colonie
     * @param blockValue BlockValue
     * @return double
     */
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


    /**
     * Retourne la taille moyenne des colonies
     * @return double
     */
    public double getAverageSizeOfColonies() {
        double totalBlocksInColonies = 0;

        for (Integer colonyNumber : numberOfBlocksPerColony.keySet()) {
            HashMap<BlockValue, Integer> colony = numberOfBlocksPerColony.get(colonyNumber);
            totalBlocksInColonies+= colony.get(BlockValue.A) + colony.get(BlockValue.B);
        }

        return totalBlocksInColonies / numberOfBlocksPerColony.keySet().size();
    }
}
