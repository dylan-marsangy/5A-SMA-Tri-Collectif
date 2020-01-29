package org.polytech.environnement;

public enum Direction {
    SOUTH(1, 0),
    NORTH(-1, 0),
    EAST(0, 1),
    WEST(0, -1);

    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

}