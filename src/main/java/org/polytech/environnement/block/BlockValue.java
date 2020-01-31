package org.polytech.environnement.block;

/**
 * Valeur d'un bloc.
 */
public enum BlockValue {
    A("A"), B("B"), ZERO("0");

    private String value;

    BlockValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
