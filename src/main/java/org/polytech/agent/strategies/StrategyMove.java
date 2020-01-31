package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Stratégie d'un agent pour bouger.
 */
public class StrategyMove implements Strategy {

    public StrategyMove() {
    }

    /**
     * Décide la direction éventuelle à suivre à partir  d'une perception (aléatoire).
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction à suivre ou (2) null si l'agent ne peut pas bouger
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) {
        // Ignorer les directions aboutissant à une case avec un autre agent.
        perception.values().removeIf(movable -> movable instanceof Agent);

        // De plus, si l'agent tient un bloc, on privilégie les directions n'aboutissant pas à une rencontre.
        if (agent.isHolding()) {
            perception.values().removeIf(Objects::nonNull);
        }

        // S'il n'y a pas de cases libres autour, l'agent reste immobile.
        if (perception.size() == 0) return null;

        // Sinon, il choisit aléatoirement une direction.
        return perception.keySet().stream()
                .skip(new Random().nextInt(perception.size()))
                .findFirst()
                .orElse(null);
    }
}
