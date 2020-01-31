package org.polytech.environnement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Block Tests")
public class BlockTest {

    @Test
    @DisplayName("Identification by either A or B.")
    public void blocksIdentification() {
        assertEquals("A", new Block(BlockValue.A).toString(), "Block A is not identified by A.");
        assertEquals("B", new Block(BlockValue.B).toString(), "Block B is not identified by B.");
    }
}
