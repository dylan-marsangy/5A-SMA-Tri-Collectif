package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

/**
 * Stratégie d'un agent pour prendre un bloc.
 */
public class StrategyPickUp implements Strategy {

    public StrategyPickUp() {
    }

    /**
     * Décide la direction éventuelle dans laquelle l'agent veut saisir un bloc.
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction indiquant le bloc à saisir ou (2) null si l'agent ne saisit pas de bloc
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) {
        double rand = new Random().nextDouble();

        Map.Entry<BlockValue, Double> preferredBlock = computeProba(agent);
        if (rand <= preferredBlock.getValue()) {
            // Garder seulement les directions dans lesquelles il y a un bloc de valeur préférée
            perception.values().removeIf(movable ->
                    (!(movable instanceof Block)) || ((Block) movable).getValue() != preferredBlock.getKey());

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
     * Calcule la probabilité de prendre le meilleur type de bloc pour un agent.
     *
     * @param agent Agent à inspecter
     * @return Meilleur type de bloc à saisir avec la probabilité de le saisir
     */
    public Map.Entry<BlockValue, Double> computeProba(Agent agent) {
        Map.Entry<BlockValue, Double> preferredBlock = computeF(agent);

        // Calcul de f.
        double f = preferredBlock.getValue();
        preferredBlock.setValue(Math.pow((agent.getkPlus() / (agent.getkPlus() + f)), 2));

        return preferredBlock;
    }

    /**
     * Calcule la proportion du meilleur type de bloc à saisir dans le voisinage récent d'un agent (basé sur sa mémoire).
     *
     * @param agent Agent à inspecter
     * @return Meilleur type de bloc à saisir avec sa proportion dans le voisinage récent
     */
    private Map.Entry<BlockValue, Double> computeF(Agent agent) {
        Map.Entry<BlockValue, Long> preferredBlock = computeBlock(agent);
        double f = (double) preferredBlock.getValue() / agent.getMemory().size();
        return new AbstractMap.SimpleEntry<>(preferredBlock.getKey(), f);
    }

    /**
     * Calcule le type de bloc qu'il est préférable de saisir pour un certain agent.
     *
     * @param agent Agent à inspecter
     * @return Type de bloc avec le nombre d'occurences dans la mémoire de l'agent
     */
    private Map.Entry<BlockValue, Long> computeBlock(Agent agent) {
        // Pour saisir un bloc, on préfère le type qui est le moins présent dans le voisinage parcouru par l'agent.
        Map<BlockValue, Long> nbBlocks = countBlocks(agent);

        return Collections.min(nbBlocks.entrySet(), Map.Entry.comparingByValue());
    }

    /**
     * Compte le nombre de blocs de chaque valeur dans la mémoire de l'agent ("0" exclu).
     *
     * @param agent Agent à inspecter
     * @return Chaque type de bloc avec leur nombre d'occurrence
     */
    private Map<BlockValue, Long> countBlocks(Agent agent) {
        Map<BlockValue, Long> result = new HashMap<>();

        Long counterA = 0L;
        Long counterB = 0L;
        for (BlockValue value : agent.getMemory()) {
            if (value == BlockValue.A) {
                counterA++;
            } else {
                counterB++;
            }
        }

        result.put(BlockValue.A, counterA);
        result.put(BlockValue.B, counterB);
        return result;
    }
}
