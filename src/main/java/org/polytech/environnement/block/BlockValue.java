package org.polytech.environnement.block;

import org.polytech.utils.Color;

/**
 * Valeur d'un bloc.
 */
public enum BlockValue {
    A("A", Color.BLUE_BACKGROUND), B("B", Color.RED_BACKGROUND), ZERO("0", Color.RESET);

    /**
     * Couleur d'affichage console de la valeur.
     */
    private Color color;
    /**
     * Valeur d'un bloc.
     */
    private String value;

    BlockValue(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.color + this.value + Color.RESET;
    }
}
