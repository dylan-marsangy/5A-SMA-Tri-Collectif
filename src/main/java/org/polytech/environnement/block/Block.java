package org.polytech.environnement.block;

import org.polytech.environnement.Movable;
import org.polytech.environnement.Direction;

public class Block implements Movable {

    private BlockValue value;

    private Block() {}

    public Block(BlockValue value) {
        this.value = value;
    }

    @Override
    public void move(Direction direction) {

    }

    public BlockValue getValue() {
        return value;
    }

    public void setValue(BlockValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

