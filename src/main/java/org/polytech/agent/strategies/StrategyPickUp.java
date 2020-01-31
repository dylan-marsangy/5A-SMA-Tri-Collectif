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

            // Si l'agent n'a pas de bloc à prendre, on ajoute dans sa mémoire "0".
            if (perception.size() == 0) {
                agent.visit(new Block(BlockValue.ZERO));
                return null;
            }

            // Choisir aléatoirement une direction restante.
            Direction result = perception.keySet().stream()
                    .skip(new Random().nextInt(perception.size()))
                    .findFirst()
                    .orElse(null);

            // Agent tente de prendre un bloc : il l'insère dans sa mémoire.
            agent.visit((Block) perception.get(result));

            return result;
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
        Map.Entry<BlockValue, Double> preferredBlock = computeBlock(agent);
        double f = preferredBlock.getValue() / agent.getMemory().size();
        return new AbstractMap.SimpleEntry<>(preferredBlock.getKey(), f);
    }

    /**
     * Calcule le type de bloc qu'il est préférable de saisir pour un certain agent (incluant l'erreur de perception).
     *
     * @param agent Agent à inspecter
     * @return Type de bloc avec le nombre d'occurences dans la mémoire de l'agent
     */
    private Map.Entry<BlockValue, Double> computeBlock(Agent agent) {
        // Pour saisir un bloc, on préfère le type qui est le moins présent dans le voisinage parcouru par l'agent.
        Map<BlockValue, Double> nbBlocks = countBlocks(agent);

        // Bruitage (error de perception des agents)
        if (agent.getError() > 0) {
            double countBlockA = nbBlocks.get(BlockValue.A);
            double countBlockB = nbBlocks.get(BlockValue.B);
            countBlockA += countBlockB * agent.getError();
            countBlockB += countBlockA * agent.getError();

            nbBlocks.put(BlockValue.A, countBlockA);
            nbBlocks.put(BlockValue.B, countBlockB);
        }

        return Collections.min(nbBlocks.entrySet(), Map.Entry.comparingByValue());
    }

    /**
     * Compte le nombre de blocs de chaque valeur dans la mémoire de l'agent ("0" exclu).
     *
     * @param agent Agent à inspecter
     * @return Chaque type de bloc avec leur nombre d'occurrence
     */
    private Map<BlockValue, Double> countBlocks(Agent agent) {
        Map<BlockValue, Double> result = new HashMap<>();

        double counterA = 0L;
        double counterB = 0L;
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
