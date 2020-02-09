package org.polytech.statistiques;

import org.polytech.environnement.Environnement;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Neighbours {
    private Environnement environnement;
    private int NEIGHBOURHOOD_SIZE;

    // nombre de voisins de chaque type, pour chaque valeur possible de Bloc (A ou B)
    private Map<BlockValue, Map<BlockValue, Integer>> neighboursNumberByBlockValue;

    public Neighbours(Environnement environnement, int neighbourhoodSize) {
        this.environnement = environnement;
        this.NEIGHBOURHOOD_SIZE = neighbourhoodSize;

        this.neighboursNumberByBlockValue = new HashMap<>();
        neighboursNumberByBlockValue.put(BlockValue.A, createEmptyNeighbours());
        neighboursNumberByBlockValue.put(BlockValue.B, createEmptyNeighbours());
    }


    /**
     * Génère une HashMap contenant 0 voisin de chaque type
     * @return Map<BlockValue, Integer>
     */
    public Map<BlockValue, Integer> createEmptyNeighbours() {
        Map<BlockValue, Integer> neighbours = new HashMap<>();
        neighbours.put(BlockValue.A, 0);
        neighbours.put(BlockValue.B, 0);
        neighbours.put(BlockValue.ZERO, 0);

        return neighbours;
    }


    /**
     * Calcule les voisins de chaque type de Bloc contenu dans l'environnement
     */
    public void calculateNeighbours() {
        Movable[][] grid = environnement.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Block) {
                    Block block = (Block) grid[i][j];

                    if (block.getValue() != BlockValue.ZERO) {
                        Map<BlockValue, Integer> currentNeighbours = getNeighboursNumberAt(i, j);

                        Map<BlockValue, Integer> totalNeighbours = neighboursNumberByBlockValue.get(block.getValue());

                        // additionne les voisins du bloc aux voisins déjà trouvés pour les blocs de même type
                        Integer totalA = totalNeighbours.get(BlockValue.A) + currentNeighbours.get(BlockValue.A);
                        Integer totalB = totalNeighbours.get(BlockValue.B) + currentNeighbours.get(BlockValue.B);
                        Integer totalZERO = totalNeighbours.get(BlockValue.ZERO) + currentNeighbours.get(BlockValue.ZERO);

                        // met à jour la Map
                        totalNeighbours.put(BlockValue.A, totalA);
                        totalNeighbours.put(BlockValue.B, totalB);
                        totalNeighbours.put(BlockValue.ZERO, totalZERO);

                        neighboursNumberByBlockValue.put(block.getValue(), totalNeighbours);
                    }
                }
            }
        }
    }


    /**
     * Récupère les voisins du bloc situé à la position indiquée en paramètre
     * @param x int
     * @param y int
     * @return  Map<BlockValue, Integer>
     */
    public Map<BlockValue, Integer> getNeighboursNumberAt(int x, int y) {
        Map<BlockValue, Integer> neighbours = createEmptyNeighbours();

        for (int i = x - NEIGHBOURHOOD_SIZE; i <= x + NEIGHBOURHOOD_SIZE; i++) {
            for (int j = y - NEIGHBOURHOOD_SIZE; j <= y + NEIGHBOURHOOD_SIZE; j++) {
                if (environnement.isInside(i, j)) {
                    if (environnement.getEntity(i, j) instanceof Block) {
                        if (i != x || j != y) {
                            BlockValue blockValue = ((Block) environnement.getEntity(i, j)).getValue();
                            neighbours.put(blockValue, neighbours.get(blockValue) + 1);
                        }
                    }
                }
            }
        }

        return neighbours;
    }


    /**
     * Récupère la liste des voisins autour de la position en paramètre
     * @param x int
     * @param y int
     * @return  Map<BlockValue, Set<List<Integer>>>
     */
    public Map<BlockValue, Set<List<Integer>>> getNeighboursAt(int x, int y) {
        Map<BlockValue, Set<List<Integer>>> neighbours = new HashMap<>();
        neighbours.put(BlockValue.A, new HashSet<>(new ArrayList<>()));
        neighbours.put(BlockValue.B, new HashSet<>(new ArrayList<>()));
        neighbours.put(BlockValue.ZERO, new HashSet<>(new ArrayList<>()));

        for (int i = x - NEIGHBOURHOOD_SIZE; i <= x + NEIGHBOURHOOD_SIZE; i++) {
            for (int j = y - NEIGHBOURHOOD_SIZE; j <= y + NEIGHBOURHOOD_SIZE; j++) {
                if (environnement.isInside(i, j)) {
                    if (environnement.getEntity(i, j) instanceof Block) {
                        if (i != x || j != y) {
                            BlockValue blockValue = ((Block) environnement.getEntity(i, j)).getValue();
                            ArrayList<Integer> coordinates = new ArrayList<>();
                            coordinates.add(0, i);
                            coordinates.add(1, j);
                            neighbours.get(blockValue).add(coordinates);
                        }
                    }
                }
            }
        }

        return neighbours;
    }


    /**
     * Getter de neighboursNumberByBlockValue
     * @return Map<BlockValue, Map<BlockValue, Integer>>
     */
    public Map<BlockValue, Map<BlockValue, Integer>> getNeighboursNumberByBlockValue() {
        return neighboursNumberByBlockValue;
    }


    /**
     * Récupère le nombre de voisins de chaque type pour la valeur donnée en paramètre
     * @param blockValue BlockValue
     * @return Map<BlockValue, Integer>
     */
    public Map<BlockValue, Integer> getNeighboursNumberByBlockValue(BlockValue blockValue) {
        return neighboursNumberByBlockValue.get(blockValue);
    }


    /**
     * Retourne le nombre de bloc voisins de valeur neighbourBlockValue situés autour des blocs de valeur centerBlockValue
     * @param centerBlockValue    BlockValue dont on cherche les voisins
     * @param neighbourBlockValue BlockValue dans le voisinage
     * @return Integer
     */
    public Integer getNeighboursWithValue(BlockValue centerBlockValue, BlockValue neighbourBlockValue) {
        return neighboursNumberByBlockValue.get(centerBlockValue).get(neighbourBlockValue);
    }


    /**
     * Retourne le nombre total de voisins calculés
     * @return Integer
     */
    public Integer getTotalOfComputedNeighbours() {
        return neighboursNumberByBlockValue.get(BlockValue.A).get(BlockValue.A) +
                neighboursNumberByBlockValue.get(BlockValue.A).get(BlockValue.B) * 2 +
                neighboursNumberByBlockValue.get(BlockValue.B).get(BlockValue.B);
    }
}
