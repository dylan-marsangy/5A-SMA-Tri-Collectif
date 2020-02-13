package org.polytech.environnement.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.utils.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Block Tests")
public class BlockTest {

    @Test
    @DisplayName("Identification by either A or B.")
    public void blocksIdentification() {
        assertEquals(Color.BLUE_BACKGROUND + "A" + Color.RESET, new Block(BlockValue.A).toString(), "Block A is not identified by A.");
        assertEquals(Color.RED_BACKGROUND + "B" + Color.RESET, new Block(BlockValue.B).toString(), "Block B is not identified by B.");
    }
}
