package org.polytech.environnement;

import javafx.util.Pair;

public class Environnement {

    Movable[][] grid;

    private Environnement() {}

    public Environnement(int n, int m) {
        this.grid = new Movable[n][m];
    }

    public void move(Movable entity, Direction direction) {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int x = coordinates.getKey() ;
        int y = coordinates.getValue();

        int xGoal = coordinates.getKey() + direction.x;
        int yGoal = coordinates.getValue() + direction.y;

        //TODO: Empêcher collision et vérifier bon fonctionnement
        if (xGoal >=0 && xGoal < grid.length &&
                yGoal >=0 && yGoal < grid[0].length) {
            this.grid[x][y] = null;
            this.grid[x + direction.x][y + direction.y] = entity;
        }
    }

    private Movable getEntity(int x, int y) {
        return this.grid[x][y];
    }

    private boolean isEmpty(int i, int j) {
        return getEntity(i, j) == null;
    }

    private Pair<Integer, Integer> findEntity(Movable entity) throws MovableNotFoundException {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals(entity)) {
                    return new Pair<>(i, j);
                }
            }
        }

        throw new MovableNotFoundException("Entity does not exist on the grid.");
    }

}
