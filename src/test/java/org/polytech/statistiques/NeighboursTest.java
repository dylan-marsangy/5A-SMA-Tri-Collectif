package org.polytech.statistiques;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Neighbours test")
public class NeighboursTest {
    private final int N = 5;
    private final int M = 5;
    private final int NB_BLOCKS_A = 0;
    private final int NB_BLOCKS_B = 0;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environnement environnement;
    private Neighbours neighbours;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(N, M, NB_BLOCKS_A, NB_BLOCKS_B);
        initializeNeighbours();
    }

    public void initializeNeighbours() {
        this.neighbours = new Neighbours(this.environnement, 1);
    }

    @Test
    @DisplayName("HashMap with A, B and ZERO neighbours should be generated with a 0 value")
    public void generateEmptyNeighbours() {
        Map<BlockValue, Integer> singleBlockNeighbours = neighbours.createEmptyNeighbours();
        assertEquals(0, singleBlockNeighbours.get(BlockValue.A));
        assertEquals(0, singleBlockNeighbours.get(BlockValue.B));
        assertEquals(0, singleBlockNeighbours.get(BlockValue.ZERO));
    }

    @Test
    @DisplayName("Block A should have 0 Block B neighbours")
    public void getSpecificNeighbours() {
        Integer neighboursNumber = neighbours.getNeighboursWithValue(BlockValue.A, BlockValue.B);
        assertEquals(0, neighboursNumber);
    }

    @Test
    @DisplayName("Block A should have 0 Block A, Block B and Block ZERO neighbours")
    public void getNeighboursFromBlockValue() {
        Map<BlockValue, Integer> neighboursNumber = neighbours.getNeighboursNumberByBlockValue(BlockValue.A);
        assertEquals(0, neighboursNumber.get(BlockValue.A));
        assertEquals(0, neighboursNumber.get(BlockValue.B));
        assertEquals(0, neighboursNumber.get(BlockValue.ZERO));
    }

    @Test
    @DisplayName("Block A and Block B should have 0 Block A, Block B and Block ZERO neighbours")
    public void getAllNeighbours() {
        Map<BlockValue, Map<BlockValue, Integer>> neighboursNumber = neighbours.getNeighboursNumberByBlockValue();
        assertEquals(0, neighboursNumber.get(BlockValue.A).get(BlockValue.A));
        assertEquals(0, neighboursNumber.get(BlockValue.A).get(BlockValue.B));
        assertEquals(0, neighboursNumber.get(BlockValue.A).get(BlockValue.ZERO));
        assertEquals(0, neighboursNumber.get(BlockValue.B).get(BlockValue.A));
        assertEquals(0, neighboursNumber.get(BlockValue.B).get(BlockValue.B));
        assertEquals(0, neighboursNumber.get(BlockValue.B).get(BlockValue.ZERO));
        assertNull(neighboursNumber.get(BlockValue.ZERO));
    }

    @Test
    @DisplayName("Block A in (1, 1) should have 1 A neighbour, 2 B neighbours and 4 ZERO neighbours")
    public void countNeighboursFromPosition() {
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Block(BlockValue.ZERO), 0, 1);
        environnement.insert(new Block(BlockValue.B), 0, 2);
        environnement.insert(new Block(BlockValue.ZERO), 1, 0);
        environnement.insert(new Block(BlockValue.A), 1, 1);
        environnement.insert(new Block(BlockValue.ZERO), 1, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 1);
        environnement.insert(new Block(BlockValue.ZERO), 2, 2);

        System.out.println(environnement);

        Map<BlockValue, Integer> neighboursNumber = neighbours.getNeighboursNumberAt(1, 1);
        assertEquals(1, neighboursNumber.get(BlockValue.A));
        assertEquals(2, neighboursNumber.get(BlockValue.B));
        assertEquals(4, neighboursNumber.get(BlockValue.ZERO));
    }

    @Test
    @DisplayName("Counts neighbours of all blocks in the environment")
    public void countAllNeighbours() {
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Block(BlockValue.ZERO), 0, 1);
        environnement.insert(new Block(BlockValue.B), 0, 2);
        environnement.insert(new Block(BlockValue.ZERO), 1, 0);
        environnement.insert(new Block(BlockValue.A), 1, 1);
        environnement.insert(new Block(BlockValue.ZERO), 1, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 1);
        environnement.insert(new Block(BlockValue.ZERO), 2, 2);

        System.out.println(environnement);

        neighbours.calculateNeighbours();

        Map<BlockValue, Map<BlockValue,  Integer>> neighboursNumber = neighbours.getNeighboursNumberByBlockValue();

        for (BlockValue blockValue : neighboursNumber.keySet()) {
            switch (blockValue) {
                case A:
                {
                    assertEquals(2, neighboursNumber.get(blockValue).get(BlockValue.A));
                    assertEquals(3, neighboursNumber.get(blockValue).get(BlockValue.B));
                    assertEquals(7, neighboursNumber.get(blockValue).get(BlockValue.ZERO));
                    break;
                }

                case B:
                {
                    assertEquals(3, neighboursNumber.get(blockValue).get(BlockValue.A));
                    assertEquals(0, neighboursNumber.get(blockValue).get(BlockValue.B));
                    assertEquals(3, neighboursNumber.get(blockValue).get(BlockValue.ZERO));
                    break;
                }

                case ZERO:
                {
                    assertNull(neighboursNumber.get(blockValue));
                    break;
                }
            }
        }
    }

    @Test
    @DisplayName("Counts neighbours of all blocks in the environment with neighbourhood size set to 2")
    public void countAllNeighboursWithNeighbourhoodSize() {
        neighbours = new Neighbours(environnement, 2);
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Block(BlockValue.ZERO), 0, 1);
        environnement.insert(new Block(BlockValue.B), 0, 2);
        environnement.insert(new Block(BlockValue.ZERO), 1, 0);
        environnement.insert(new Block(BlockValue.A), 1, 1);
        environnement.insert(new Block(BlockValue.ZERO), 1, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 1);
        environnement.insert(new Block(BlockValue.ZERO), 2, 2);

        System.out.println(environnement);

        neighbours.calculateNeighbours();

        Map<BlockValue, Map<BlockValue,  Integer>> neighboursNumber = neighbours.getNeighboursNumberByBlockValue();

        for (BlockValue blockValue : neighboursNumber.keySet()) {
            switch (blockValue) {
                case A:
                {
                    assertEquals(2, neighboursNumber.get(blockValue).get(BlockValue.A));
                    assertEquals(4, neighboursNumber.get(blockValue).get(BlockValue.B));
                    assertEquals(8, neighboursNumber.get(blockValue).get(BlockValue.ZERO));
                    break;
                }

                case B:
                {
                    assertEquals(4, neighboursNumber.get(blockValue).get(BlockValue.A));
                    assertEquals(2, neighboursNumber.get(blockValue).get(BlockValue.B));
                    assertEquals(8, neighboursNumber.get(blockValue).get(BlockValue.ZERO));
                    break;
                }

                case ZERO:
                {
                    assertNull(neighboursNumber.get(blockValue));
                    break;
                }
            }
        }
    }

    @Test
    @DisplayName("Block A should have 0 Block B neighbours")
    public void getTotalComputedNeighbours() {
        environnement.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environnement.insert(new Block(BlockValue.ZERO), 0, 1);
        environnement.insert(new Block(BlockValue.B), 0, 2);
        environnement.insert(new Block(BlockValue.ZERO), 1, 0);
        environnement.insert(new Block(BlockValue.A), 1, 1);
        environnement.insert(new Block(BlockValue.ZERO), 1, 2);
        environnement.insert(new Block(BlockValue.B), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 1);
        environnement.insert(new Block(BlockValue.ZERO), 2, 2);

        System.out.println(environnement);

        neighbours.calculateNeighbours();

        assertEquals(8, neighbours.getTotalOfComputedNeighbours());
    }
}
