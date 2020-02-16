package org.polytech.environment.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.utils.ColorConsole;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Block Tests")
public class BlockTest {

    @Test
    @DisplayName("Identification by either A or B.")
    public void blocksIdentification() {
        assertEquals(ColorConsole.BLUE_BACKGROUND + "A" + ColorConsole.RESET, new Block(BlockValue.A).toString(), "Block A is not identified by A.");
        assertEquals(ColorConsole.RED_BACKGROUND + "B" + ColorConsole.RESET, new Block(BlockValue.B).toString(), "Block B is not identified by B.");
    }
}
