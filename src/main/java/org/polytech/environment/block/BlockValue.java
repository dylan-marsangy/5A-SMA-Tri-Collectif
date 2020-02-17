package org.polytech.environment.block;

import org.polytech.utils.ColorConsole;

/**
 * Valeur d'un bloc.
 */
public enum BlockValue {
    A("A", ColorConsole.BLUE_BACKGROUND), B("B", ColorConsole.RED_BACKGROUND), ZERO("0", ColorConsole.RESET);

    /**
     * Couleur d'affichage console de la valeur.
     */
    private ColorConsole colorConsole;
    /**
     * Valeur d'un bloc.
     */
    private String value;

    BlockValue(String value, ColorConsole colorConsole) {
        this.value = value;
        this.colorConsole = colorConsole;
    }

    @Override
    public String toString() {
        return this.colorConsole + this.value + ColorConsole.RESET;
    }
}
