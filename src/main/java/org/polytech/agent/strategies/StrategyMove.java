package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environment.Direction;
import org.polytech.environment.Movable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stratégie d'un agent pour bouger.
 */
public class StrategyMove implements Strategy {

    public StrategyMove() {
    }

    /**
     * Décide la direction éventuelle à suivre à partir  d'une perception (aléatoire).
     * La direction élue, bien qu'aléatoire, évite toutefois une rencontre avec un agent.
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction à suivre ou (2) null si l'agent ne peut pas bouger
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) {
        // Ignorer les directions aboutissant à une case avec un autre agent.
        Map<Direction, Movable> clonedPerception = new HashMap<>(perception);
        clonedPerception.values().removeIf(movable -> movable instanceof Agent);

        // S'il n'y a pas de cases libres autour, l'agent reste immobile.
        if (clonedPerception.size() == 0) return null;

        // Sinon, il choisit aléatoirement une direction.
        return clonedPerception.keySet().stream()
                .skip(new Random().nextInt(clonedPerception.size()))
                .findFirst()
                .orElse(null);
    }
}
