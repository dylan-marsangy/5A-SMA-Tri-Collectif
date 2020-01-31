package org.polytech.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.environnement.block.BlockValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Utils Tests")
public class UtilsTest {


    @Test
    @DisplayName("should return 3")
    public void updateMap() {
        Map<BlockValue, Double> test = new HashMap<>();
        test.put(BlockValue.A, 2d);

        test.forEach((key, value) -> test.replace(key, value + 1));
        assertEquals(3, test.get(BlockValue.A));
    }

}
