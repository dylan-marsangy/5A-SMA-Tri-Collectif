package org.polytech.environment.block;

import org.polytech.environment.Movable;

/**
 * Bloc fixe disposé sur l'environnement.
 */
public class Block implements Movable {

    /**
     * Valeur du bloc.
     */
    private BlockValue value;
    private int colonyNumber;

    private Block() {
        colonyNumber = -1;
    }

    public Block(BlockValue value) {
        this.value = value;
        this.colonyNumber = -1;
    }

    public BlockValue getValue() {
        return value;
    }

    public void setValue(BlockValue value) {
        this.value = value;
    }

    public int getColonyNumber() {
        return colonyNumber;
    }

    public void setColonyNumber(int colonyNumber) {
        this.colonyNumber = colonyNumber;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

