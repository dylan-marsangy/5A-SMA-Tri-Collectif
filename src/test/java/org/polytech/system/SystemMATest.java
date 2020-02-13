package org.polytech.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("System Tests")
public class SystemMATest {

    private final int N = 5;
    private final int M = 5;
    private final int NB_AGENTS = 5;
    private final int NB_BLOCKS_A = 5;
    private final int NB_BLOCKS_B = 5;

    private final int I = 1;
    private final int T = 10;
    private final double K_MINUS = 0.3;
    private final double K_PLUS = 0.1;
    private final double ERROR = 0d;

    private SystemMA system;

    @BeforeEach
    public void initializeEnvironnement() {
        Agent.cleanID();

        // Génération de l'environnement
        Environnement environnement = new RandomEnvironnement(N, M, NB_BLOCKS_A, NB_BLOCKS_B);

        // Génération des agents
        Set<Agent> agents = new HashSet<>();
        IntStream.rangeClosed(1, NB_AGENTS).forEach(index ->
                agents.add(new Agent(I, T, K_PLUS, K_MINUS, ERROR)));

        // Génération du système (place les agents dans l'environnement)
        system = new SystemMA(environnement, agents, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID);

    }

    // RUNNING ---------------------------------------------------------------------------------------------------------

    @RepeatedTest(10)
    @DisplayName("Pick Random Agent")
    public void pickRandomAgent() {
        Set<Long> picked = new HashSet<>();
        IntStream.rangeClosed(1, 20).forEach(input -> {
            Long id = system.pickRandomAgent().getID();
            System.out.println(id);
            picked.add(id);
        });

        assertNotEquals(1, picked.size());
    }

}
