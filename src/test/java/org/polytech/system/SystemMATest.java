package org.polytech.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.polytech.agent.Agent;

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
    public void initializeEnvironment() {
        Agent.cleanID();

        system = SystemMAFactory.instantiateRandom(
                N, M, NB_AGENTS, NB_BLOCKS_A, NB_BLOCKS_B,
                I, T, K_PLUS, K_MINUS, ERROR);
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
