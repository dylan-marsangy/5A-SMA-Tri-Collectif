package org.polytech.statistiques;

import org.polytech.environment.Environment;
import org.polytech.environment.Movable;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

/**
 * Classe utilisée pour générer les statistiques liées à une exécution de l'algorithme
 */
public class Evaluation {
    private Environment environment;
    private Neighbours neighbours;
    private Colonies colonies;
    private int NEIGHBOURHOOD_SIZE = 1;

    public Evaluation(Environment environment) {
        this.environment = environment;
        evaluate();
    }

    public Evaluation(Environment environment, int neighbourhoodSize) {
        this.environment = environment;
        this.NEIGHBOURHOOD_SIZE = neighbourhoodSize;
        evaluate();
    }

    public void evaluate() {
        // calcule les voisins de chaque bloc A et B
        this.neighbours = new Neighbours(environment, NEIGHBOURHOOD_SIZE);
        this.neighbours.calculateNeighbours();

        // cherche les colonies
        this.colonies = new Colonies(environment, neighbours);
        this.colonies.createColonies();
    }

    /**
     * Getteur de l'environnement utilisé pour l'évaluation
     *
     * @return Environnement
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Calcule le nombre total de blocs présents dans l'environnement avec la valeur en paramètre
     *
     * @param blockValue: BlockValue à rechercher (A ou B)
     * @return int
     */
    public int getTotalBlockWithValue(BlockValue blockValue) {
        Movable[][] grid = environment.getGrid();
        int sum = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Block && ((Block) grid[i][j]).getValue() == blockValue) {
                    ++sum;
                }
            }
        }
        return sum;
    }

    /**
     * Retourne le nombre de colonies trouvées dans l'environnement
     *
     * @return int
     */
    public int getNumberOfColonies() {
        return colonies.countColonies();
    }

    /**
     * Calcule la proportion de blocs avec la valeur firstBlockValue ayant pour voisin un bloc de valeur secondBlockValue
     *
     * @param firstBlockValue  BlockValue (A ou B)
     * @param secondBlockValue BlockValue (A ou B)
     * @return double
     */
    public double getNeighborhoodPercentage(BlockValue firstBlockValue, BlockValue secondBlockValue) {
        double searchedNeighbours = neighbours.getNeighboursWithValue(firstBlockValue, secondBlockValue);

        if (!firstBlockValue.equals(secondBlockValue)) {
            searchedNeighbours += neighbours.getNeighboursWithValue(secondBlockValue, firstBlockValue);
        }

        double totalNeighbours = neighbours.getTotalOfComputedNeighbours();

        return searchedNeighbours / totalNeighbours;
    }

    /**
     * Retourne la proportion moyenne de blocs avec la valeur en paramètre dans les colonies
     *
     * @param blockValue BlockValue (A ou B)
     * @return double
     */
    public double getAverageColoniesBlockWithValue(BlockValue blockValue) {
        return colonies.getAverageColoniesBlockWithValue(blockValue);
    }

    /**
     * Retourne la taille moyenne d'une colonie
     *
     * @return double
     */
    public double getAverageSizeOfColonies() {
        return colonies.getAverageSizeOfColonies();
    }
}
