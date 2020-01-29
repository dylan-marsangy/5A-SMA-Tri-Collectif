package org.polytech.environnement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environnement Tests")
public class EnvironnementTest {

    private final int n = 5;
    private final int m = 5;
    private final int nbAgents = 5;
    private final int nbObjects = 10;

    private final int t = 10;
    private final double k = 0.3;
    private final double kPlus = 0.1;

    private Environnement environnement;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(n, m, nbAgents, nbObjects);
        Agent.cleanID();
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Initialize Grid")
    public void hasCorrectDimensions() {
        assertTrue(environnement.isInside(0, 0));
        assertTrue(environnement.isInside(0, m - 1));
        assertTrue(environnement.isInside(n - 1, 0));
        assertTrue(environnement.isInside(n - 1, m - 1));

        assertFalse(environnement.isInside(n, m));
    }

    @Test
    @DisplayName("Display Grid")
    public void displayGrid() {
        environnement.insert(new Agent(t, kPlus, k), 0, 0);
        environnement.insert(new Agent(t, kPlus, k), 1, 0);
        environnement.insert(new Agent(t, kPlus, k), 2, 0);
        environnement.insert(new Block(BlockValue.A), 2, 3);
        environnement.insert(new Block(BlockValue.B), 4, 2);

        String expected =
                " 1 | 0 | 0 | 0 | 0 |\n" +
                        " 2 | 0 | 0 | 0 | 0 |\n" +
                        " 3 | 0 | 0 | A | 0 |\n" +
                        " 0 | 0 | 0 | 0 | 0 |\n" +
                        " 0 | 0 | B | 0 | 0 |\n";

        assertEquals(expected, environnement.toString());
    }

    // INSERT ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Insert Entity Inside Grid")
    public void insertEntity_insideGrid() {
        Agent agent = new Agent(t, kPlus, k);
        environnement.insert(agent, 1, 1);
        assertEquals(agent, environnement.getEntity(1 ,1));
    }

    @Test
    @DisplayName("Avoid Collisions While Inserting")
    public void avoidCollisions_whenInserting() {
        environnement.insert(new Agent(t, kPlus, k), 1, 1);
        assertThrows(CollisionException.class, () -> environnement.insert(new Agent(t, kPlus, k), 1, 1));
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Remove Entity From Grid")
    public void removeEntity() {
        Agent agent = new Agent(t, kPlus, k);
        environnement.insert(agent, 1, 1);
        environnement.remove(1 ,1);
        assertNull(environnement.getEntity(1 ,1));
    }

    // MOVE ------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Move Entity Inside Grid")
    public void moveEntity() {
        Agent agent = new Agent(t, kPlus, k);
        environnement.insert(agent, 1,1);
        environnement.move(agent, Direction.NORTH, 1);
        assertNull(environnement.getEntity(1, 1));
        assertEquals(agent, environnement.getEntity(0, 1));
    }

    @Test
    @DisplayName("Avoid Moves Outside Grid")
    public void avoidMoving_outsideGrid() {
        Agent a1 = new Agent(t, kPlus, k);
        environnement.insert(a1, 0, 0);
        assertFalse(environnement.move(a1, Direction.NORTH, 1));
    }

    @Test
    @DisplayName("Avoid Collisions While Moving")
    public void avoidCollisions_whenMoving() {
        Agent a1 = new Agent(t, kPlus, k);
        Agent a2 = new Agent(t, kPlus, k);
        environnement.insert(a1, 0, 1);
        environnement.insert(a2, 1, 1);
        assertThrows(CollisionException.class, () -> environnement.move(a2, Direction.NORTH, 1));
    }

    // PERCEPTION ------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Entity Perception For All Directions")
    public void getPerception_allDirections() {
        Agent agent = new Agent(t, kPlus, k);
        Agent a2 = new Agent(t, kPlus, k);
        Agent a3 = new Agent(t, kPlus, k);
        Agent a4 = new Agent(t, kPlus, k);

        environnement.insert(agent, 0, 2);
        environnement.insert(a2, 2, 2);
        environnement.insert(a3, 0, 0);
        environnement.insert(a4, 4, 2);

        Map<Direction, Movable> expected = new HashMap<>();
        expected.put(Direction.SOUTH, a2);
        expected.put(Direction.EAST, null);
        expected.put(Direction.WEST, a3);

        assertEquals(expected, environnement.perception(agent, 2));
    }

    // RANDOM INSERTS --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Count elements on grid")
    public void hasCorrectElements() {
        environnement.insertAgents(t, kPlus, k);
        environnement.insertBlocks();

        int agents = 0;
        int objects = 0;

        for (int i = 0; i < environnement.getGrid().length ; i++) {
            for (int j = 0 ; j < environnement.getGrid()[i].length ; j++) {
                if (environnement.getEntity(i, j) instanceof Agent) {
                    ++agents;
                }
                else if (environnement.getEntity(i, j) instanceof Block) {
                    ++objects;
                }
            }
        }

        assertEquals(agents, nbAgents);
        assertEquals(objects, nbObjects);
    }

    // RUNNING ---------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Pick Random Agent")
    public void pickRandomAgent() {
        environnement.insertAgents(t, kPlus, k);
        Set<Long> picked = new HashSet<>();
        IntStream.rangeClosed(1, 20).forEach(input -> picked.add(environnement.pickRandomAgent().getID()));

        assertNotEquals(1, picked.size());
    }


}
