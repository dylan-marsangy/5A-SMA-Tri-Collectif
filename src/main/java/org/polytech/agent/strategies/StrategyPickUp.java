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

        Map<BlockValue, Double> probaBlocks = computeProba(agent);
        Map.Entry<BlockValue, Double> preferredBlock =  Collections.max(probaBlocks.entrySet(), Map.Entry.comparingByValue());
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
     * Calcule la probabilité d'un agent de prendre chaque type de bloc.
     *
     * @param agent Agent à inspecter
     * @return Probabilité de prendre un bloc
     */
    public Map<BlockValue, Double> computeProba(Agent agent) {
        Map<BlockValue, Double> fValues = computeF(agent);

        // Calcul de la probabilité de prise.
        fValues.forEach(((blockValue, fValue) ->
                fValues.replace(blockValue, Math.pow((agent.getkPlus() / (agent.getkPlus() + fValue)), 2))));

        return fValues;
    }

    /**
     * Calcule la proportion du chaque type de bloc dans la mémoire de l'agent (bruité).
     *
     * @param agent Agent à inspecter
     * @return Proportion de chaque type de bloc
     */
    private Map<BlockValue, Double> computeF(Agent agent) {
        Map<BlockValue, Double> countedBlocks = countBlocks(agent);

        countedBlocks.forEach(((blockValue, count) ->
                countedBlocks.replace(blockValue, count / agent.getMemory().size())));
        return countedBlocks;
    }

    /**
     * Compte le nombre de blocs de chaque valeur dans la mémoire de l'agent ("0" exclu), bruité (erreur de perception).
     *
     * @param agent Agent à inspecter
     * @return Chaque type de bloc avec leur nombre d'occurrence (bruité)
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

        // Bruitage (error de perception des agents)
        if (agent.getError() > 0) {
            counterA += counterB * agent.getError();
            counterB += counterA * agent.getError();
        }

        result.put(BlockValue.A, counterA);
        result.put(BlockValue.B, counterB);
        return result;
    }
}
