package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;

import java.util.Map;
import java.util.Random;

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
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) {
        double rand = new Random().nextDouble();

        double proba = computeProba(agent, perception);
        if (rand <= proba) {
            //TODO: Choisir direction vide
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
        return Math.pow((f / (agent.getK() + f)), 2);
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
