package org.polytech.statistiques;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.environment.Environment;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Colonies test")
public class ColoniesTest {
    private final int N = 5;
    private final int M = 5;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0;

    private Environment environment;
    private Colonies colonies;
    private Neighbours neighbours;


    @BeforeEach
    public void initializeEnvironment() {
        environment = new Environment(N, M);

        this.neighbours = new Neighbours(environment, 1);
        this.neighbours.calculateNeighbours();
        initializeColonies();
    }

    public void initializeColonies() {
        this.colonies = new Colonies(this.environment, this.neighbours);
    }

    @Test
    @DisplayName("Colonies should be separated from one another")
    public void giveColonyNumbers() {
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        assertEquals(-1, ((Block) environment.getEntity(0, 1)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(0, 2)).getColonyNumber());
        assertEquals(2, ((Block) environment.getEntity(0, 4)).getColonyNumber());
        assertEquals(2, ((Block) environment.getEntity(1, 4)).getColonyNumber());
        assertEquals(3, ((Block) environment.getEntity(2, 0)).getColonyNumber());
        assertEquals(3, ((Block) environment.getEntity(2, 1)).getColonyNumber());
        assertEquals(-1, ((Block) environment.getEntity(2, 2)).getColonyNumber());
        assertEquals(4, ((Block) environment.getEntity(3, 4)).getColonyNumber());
    }

    @Test
    @DisplayName("Colonies should be separated from one another with a neighbourhood size set to 2")
    public void giveColonyNumbersWithNeighbourhoodSize() {
        this.neighbours = new Neighbours(environment, 2);
        this.neighbours.calculateNeighbours();
        initializeColonies();

        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 4, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        assertEquals(-1, ((Block) environment.getEntity(0, 1)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(0, 2)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(0, 4)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(1, 4)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(2, 0)).getColonyNumber());
        assertEquals(1, ((Block) environment.getEntity(2, 1)).getColonyNumber());
        assertEquals(-1, ((Block) environment.getEntity(2, 2)).getColonyNumber());
        assertEquals(2, ((Block) environment.getEntity(4, 4)).getColonyNumber());
    }

    @Test
    @DisplayName("There should be 4 colonies")
    public void getColonyNumbers() {
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        int coloniesNumber = colonies.countColonies();

        assertEquals(4, coloniesNumber);
    }

    @Test
    @DisplayName("Check if the numbers of blocks in the colonies are correct")
    public void getNumberOfBlocksPerColony() {
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        HashMap<Integer, HashMap<BlockValue, Integer>> numberOfBlocksPerColony = colonies.getNumberOfBlocksPerColony();

        assertEquals(4, numberOfBlocksPerColony.keySet().size());
        assertEquals(0, numberOfBlocksPerColony.get(1).get(BlockValue.A));
        assertEquals(1, numberOfBlocksPerColony.get(1).get(BlockValue.B));
        assertEquals(2, numberOfBlocksPerColony.get(2).get(BlockValue.A));
        assertEquals(0, numberOfBlocksPerColony.get(2).get(BlockValue.B));
        assertEquals(1, numberOfBlocksPerColony.get(3).get(BlockValue.A));
        assertEquals(1, numberOfBlocksPerColony.get(3).get(BlockValue.B));
    }

    @Test
    @DisplayName("Check if the average numbers of blocks in the colonies are correct")
    public void getAverageOfBlocksByColony() {
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        double AProportion = colonies.getAverageColoniesBlockWithValue(BlockValue.A);
        double BProportion = colonies.getAverageColoniesBlockWithValue(BlockValue.B);

        assertEquals(15.0 / 40.0, AProportion);
        assertEquals(25.0 / 40.0, BProportion);
    }

    @Test
    @DisplayName("Check if the average size of the colonies is correct")
    public void getAverageSizeOfColonies() {
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 0, 0);
        environment.insert(new Block(BlockValue.ZERO), 0, 1);
        environment.insert(new Block(BlockValue.B), 0, 2);

        environment.insert(new Block(BlockValue.B), 2, 0);
        environment.insert(new Block(BlockValue.A), 2, 1);
        environment.insert(new Block(BlockValue.ZERO), 2, 2);

        environment.insert(new Block(BlockValue.A), 0, 4);
        environment.insert(new Block(BlockValue.A), 1, 4);
        environment.insert(new Block(BlockValue.B), 3, 4);
        environment.insert(new Agent(I, T, K_PLUS, K_MINUS, ERROR), 3, 3);

        System.out.println(environment);

        colonies.createColonies();

        double averageSize = colonies.getAverageSizeOfColonies();

        assertEquals(6.0 / 4.0, averageSize);
    }
}
