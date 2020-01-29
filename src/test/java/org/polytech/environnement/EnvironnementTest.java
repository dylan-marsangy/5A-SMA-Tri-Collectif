package org.polytech.environnement;

import org.junit.jupiter.api.*;
import org.polytech.agent.Agent;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Environnement Tests")
public class EnvironnementTest {

    private static final int n = 3;
    private static final int m = 3;
    private static final int t = 10;

    private static Environnement environnement;

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

}
