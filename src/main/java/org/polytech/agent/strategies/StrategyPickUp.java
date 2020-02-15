package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environment.Direction;
import org.polytech.environment.Movable;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;
import org.polytech.environment.exceptions.MovableNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stratégie d'un agent pour prendre un bloc.
 * Cette stratégie est principalement définie par la direction cible de l'agent calculée après l'exécution de la stratégie 'Move'.
 * Elle permet de déterminer si la direction doit être maintenue (prise de bloc) ou abandonnée.
 */
public class StrategyPickUp implements Strategy {

    /**
     * Direction cible de l'agent.
     */
    private Direction goalDirection;

    public StrategyPickUp(Direction goalDirection) {
        this.goalDirection = goalDirection;
    }

    /**
     * Maintient ou abandonne la direction cible de l'agent (déterminée lors de l'exécution de la stragtégie 'Move').
     * Si l'agent abandonne la direction, cela signifie qu'il ne tente pas de prendre le bloc qui s'y trouve et reste
     * immobile.
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction cible de l'agent si le bloc qui s'y trouve est pris (maintient) ou (2) null sinon (abandonne).
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) {
        if (agent.isHolding()) throw new MovableNotFoundException("L'agent tient déjà un bloc.");

        // Vérification qu'un bloc est bien présent dans la direction cible.
        // Si c'est le cas, tenter de le prendre.
        Movable entity = perception.get(goalDirection);
        if (entity instanceof Block) {
            Block block = (Block) entity;


            // Calculer la proba pour tenter de prendre le bloc cible.
            double probaPickUp = computeProba(agent).get(block.getValue());

            // L'agent a pris un bloc (ou tenté) donc on l'ajout en mémoire.
            agent.visit(block);

            // Tentative de prise
            if (new Random().nextDouble() <= probaPickUp) {
                return goalDirection;
            } else {
                return null;
            }
        }

        // L'agent ne rencontre pas de bloc.
        agent.visit(new Block(BlockValue.ZERO));
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
