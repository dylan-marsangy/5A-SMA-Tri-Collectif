package org.polytech.environnement;

/**
 * Directions possibles de déplacement et de perception sur la grille.
 */
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

    /**
     * Renvoie la direction contraire.
     * @return Direction contraire.
     */
    public Direction contrary() {
        switch (this) {
            case SOUTH:
                return NORTH;
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            default:
                return null;
        }
    }

}
