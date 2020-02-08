package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;


/**
 * Classe utilisée pour générer les statistiques liées à une exécution de l'algorithme
 */
public class Evaluation {
    private Environnement environnement;
    private Neighbours neighbours;
    private Colonies colonies;

    public Evaluation(Environnement environnement) {
        this.environnement = environnement;

        // calcule les voisins de chaque bloc A et B
        this.neighbours = new Neighbours(environnement);
        this.neighbours.calculateNeighbours();

        // cherche les colonies
        this.colonies = new Colonies(environnement, neighbours);
        this.colonies.createColonies();
    }

    /**
     * Calcule le nombre total de blocs présents dans l'environnement avec la valeur en paramètre
     * @param blockValue: BlockValue à rechercher (A ou B)
     * @return int
     */
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

    /**
     * Retourne le nombre de colonies trouvées dans l'environnement
     * @return int
     */
    public int getNumberOfColonies() {
        return colonies.countColonies();
    }

    /**
     * Calcule la proportion de blocs avec la valeur firstBlockValue ayant pour voisin un bloc de valeur secondBlockValue
     * @param firstBlockValue  BlockValue (A ou B)
     * @param secondBlockValue BlockValue (A ou B)
     * @return double
     */
    public double getNeighborhoodPercentage(BlockValue firstBlockValue, BlockValue secondBlockValue) {
        double searchedNeighbours = neighbours.getNeighboursWithValue(firstBlockValue, secondBlockValue);

        if (!firstBlockValue.equals(secondBlockValue)) {
            searchedNeighbours+= neighbours.getNeighboursWithValue(secondBlockValue, firstBlockValue);
        }

        double totalNeighbours = neighbours.getTotalOfComputedNeighbours();

        return searchedNeighbours / totalNeighbours;
    }

    /**
     * Retourne la proportion moyenne de blocs avec la valeur en paramètre dans les colonies
     * @param blockValue BlockValue (A ou B)
     * @return double
     */
    public double getAverageColoniesBlockWithValue(BlockValue blockValue) {
        return colonies.getAverageColoniesBlockWithValue(blockValue);
    }

    /**
     * Retourne la taille moyenne d'une colonie
     * @return double
     */
    public double getAverageSizeOfColonies() {
        return colonies.getAverageSizeOfColonies();
    }
}
