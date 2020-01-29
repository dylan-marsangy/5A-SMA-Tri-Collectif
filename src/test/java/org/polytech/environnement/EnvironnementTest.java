package org.polytech.environnement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environnement Tests")
public class EnvironnementTest {

    private static final int n = 5;
    private static final int m = 5;
    private static final int t = 10;

    private Environnement environnement;

    @BeforeEach
    public void initializeEnvironnement() {
        environnement = new Environnement(n, m);
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Initialiaze Grid")
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
        environnement.insert(new Agent(t), 0, 0);
        environnement.insert(new Agent(t), 1, 0);
        environnement.insert(new Agent(t), 2, 0);
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
        Agent agent = new Agent(t);
        environnement.insert(agent, 1, 1);
        assertEquals(agent, environnement.getEntity(1 ,1));
    }

    @Test
    @DisplayName("Refuse Entity Insertion Outside Grid")
    public void insertEntity_outsideGrid() {
        assertThrows(CollisionException.class, () -> environnement.insert(new Agent(t), 0, -10));
    }

    @Test
    @DisplayName("Avoid Collisions While Inserting")
    public void avoidCollisions_whenInserting() {
        environnement.insert(new Agent(t), 1, 1);
        assertThrows(CollisionException.class, () -> environnement.insert(new Agent(t), 1, 1));
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Remove Entity From Grid")
    public void removeEntity() {
        Agent agent = new Agent(t);
        environnement.insert(agent, 1, 1);
        environnement.remove(1 ,1);
        assertNull(environnement.getEntity(1 ,1));
    }

    @Test
    @DisplayName("Refuse Removal Walls")
    public void removeWalls() {
        assertThrows(CollisionException.class, () -> environnement.remove(0, -10));
    }

    // MOVE ------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Move Entity Inside Grid")
    public void moveEntity() {
        Agent agent = new Agent(t);
        environnement.insert(agent, 1,1);
        environnement.move(agent, Direction.NORTH);
        assertNull(environnement.getEntity(1, 1));
        assertEquals(agent, environnement.getEntity(0, 1));
    }

    @Test
    @DisplayName("Avoid Moves Outside Grid")
    public void avoidMoving_outsideGrid() {
        Agent a1 = new Agent(t);
        environnement.insert(a1, 0, 0);
        assertThrows(CollisionException.class, () -> environnement.move(a1, Direction.NORTH));
    }

    @Test
    @DisplayName("Avoid Collisions While Moving")
    public void avoidCollisions_whenMoving() {
        Agent a1 = new Agent(t);
        Agent a2 = new Agent(t);
        environnement.insert(a1, 0, 1);
        environnement.insert(a2, 1, 1);
        assertThrows(CollisionException.class, () -> environnement.move(a2, Direction.NORTH));
    }

    // PERCEPTION ------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Entity Perception For Direction")
    public void getPerception_oneDirection() {
        Agent agent = new Agent(t);
        Agent a2 = new Agent(t);
        Agent a3 = new Agent(t);

        environnement.insert(agent, 2, 2);
        environnement.insert(a2, 1, 2);
        environnement.insert(a3, 0, 2);

        assertEquals(a2, environnement.perception(agent, Direction.NORTH, 1));
        assertEquals(a3, environnement.perception(agent, Direction.NORTH, 2));
    }

    @Test
    @DisplayName("Get Entity Perception For All Directions")
    public void getPerception_allDirections() {
        Agent agent = new Agent(t);
        Agent a2 = new Agent(t);
        Agent a3 = new Agent(t);
        Agent a4 = new Agent(t);
        Agent a5 = new Agent(t);

        environnement.insert(agent, 2, 2);
        environnement.insert(a2, 0, 2);
        environnement.insert(a3, 4, 2);
        environnement.insert(a4, 2, 4);
        environnement.insert(a5, 3, 3);

        Map<Direction, Movable> expected = new HashMap<>();
        expected.put(Direction.NORTH, a2);
        expected.put(Direction.SOUTH, a3);
        expected.put(Direction.EAST, a4);
        expected.put(Direction.WEST, null);

        assertEquals(expected, environnement.perception(agent, 2));
    }

}
