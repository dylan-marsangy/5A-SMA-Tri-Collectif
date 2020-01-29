package org.polytech.environnement;

import javafx.util.Pair;

public class Environnement {

    Movable[][] grid;

    private Environnement() {}

    public Environnement(int n, int m) {
        this.grid = new Movable[n][m];
    }

    public void move(Movable entity, Direction direction) throws CollisionException {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int x = coordinates.getKey() ;
        int y = coordinates.getValue();

        int xGoal = coordinates.getKey() + direction.x;
        int yGoal = coordinates.getValue() + direction.y;

        // Déplacement
        insert(entity, xGoal, yGoal);
        remove(x, y);
    }

    public void remove(int x, int y) {
        if (isInside(x, y)) this.grid[x][y] = null;
        else throw new CollisionException("Vous êtes en train de casser les murs. >:(");
    }

    public void insert(Movable entity, int x, int y) throws CollisionException {
        if (isInside(x, y)) {
            if (isEmpty(x, y)) this.grid[x][y] = entity;
            else throw new CollisionException("Une entité est déjà présente sur la case.");
        } else throw new CollisionException("L'entité ne peut pas rentrer dans les murs.");

    }

    public Movable getEntity(int x, int y) {
        return this.grid[x][y];
    }

    public boolean isInside(int x, int y) {
        return (x >=0 && x < grid.length &&
                y >=0 && y < grid[0].length);
    }

    public boolean isEmpty(int i, int j) {
        return getEntity(i, j) == null;
    }

    public Pair<Integer, Integer> findEntity(Movable entity) throws MovableNotFoundException {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (entity.equals(grid[i][j])) {
                    return new Pair<>(i, j);
                }
            }
        }

        throw new MovableNotFoundException("Entity does not exist on the grid.");
    }

    public Movable[][] getGrid() {
        return grid;
    }

    public void setGrid(Movable[][] grid) {
        this.grid = grid;
    }
}
