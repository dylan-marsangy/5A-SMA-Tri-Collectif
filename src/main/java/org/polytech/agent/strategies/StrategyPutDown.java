package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.exceptions.MovableNotFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Stratégie d'un agent pour déposer un bloc.
 */
public class StrategyPutDown implements Strategy {

    public StrategyPutDown() {
    }

    /**
     * Décide la direction éventuelle dans laquelle l'agent veut poser un bloc.
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction indiquant où déposer le bloc ou (2) null si l'agent ne pose pas son bloc
     * @throws MovableNotFoundException Si l'agent ne tient pas de bloc
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) throws MovableNotFoundException {
        if (agent.getHolding() == null) throw new MovableNotFoundException("L'agent ne tient aucun bloc.");

        double proba = computeProba(agent, perception);
        if (new Random().nextDouble() <= proba) {
            // Garder seulement les directions dans lesquelles la case but est vide.
            perception.values().removeIf(Objects::nonNull);

            // Choisir aléatoirement une direction restante.
            if (perception.size() == 0) return null;
            return perception.keySet().stream()
                    .skip(new Random().nextInt(perception.size()))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    /**
     * Calcule la probabilité de déposer le bloc tenu par un agent.
     *
     * @param agent      Agent inspectant son entourage
     * @param perception Perception de l'agent (voisinage immédiat)
     * @return Probabilité de déposer le bloc
     */
    public double computeProba(Agent agent, Map<Direction, Movable> perception) {
        // Calcul de f.
        double f = computeF(agent, perception);
        return Math.pow((f / (agent.getkMinus() + f)), 2);
    }

    /**
     * Calcule la proportion du type de bloc tenu par l'agent dans son voisinage immédiat.
     *
     * @param agent      Agent inspectant son entourage
     * @param perception Perception de l'agent (voisinage immédiat)
     * @return Proportion du type de bloc
     */
    private double computeF(Agent agent, Map<Direction, Movable> perception) {
        Long countBlocks = countBlocks(agent, perception);
        return (double) countBlocks / perception.size();
    }

    /**
     * Compte le nombre de blocs de même valeur que celui tenu par l'agent dans son voisinage immédiat.
     *
     * @param agent      Agent inspectant son entourage
     * @param perception Perception de l'agent (voisinage immédiat)
     * @return Nombre de blocs de même valeur
     */
    private Long countBlocks(Agent agent, Map<Direction, Movable> perception) {
        return perception.values().stream()
                .filter(movable -> (movable instanceof Block)
                        && ((Block) movable).getValue() == agent.getHolding().getValue())
                .count();
    }
}
