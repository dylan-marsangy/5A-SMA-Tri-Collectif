package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.MovableNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stratégie d'un agent pour déposer un bloc.
 * Cette stratégie est principalement définie par la direction cible de l'agent calculée après l'exécution de la stratégie 'Move'.
 * Elle permet de déterminer si, lors du déplacement à effectuer, le bloc tenu doit être déposé ou non.
 */
public class StrategyPutDown implements Strategy {

    /**
     * Direction cible de l'agent.
     */
    private Direction goalDirection;

    public StrategyPutDown(Direction goalDirection) {
        this.goalDirection = goalDirection;
    }

    /**
     * Décide si l'agent doit déposer ou non son bloc tenu sur sa position actuelle avant le déplacement qu'il compte
     * réaliser (déterminée lors de l'exécution de la stragtégie 'Move').
     * Dans tous les cas, l'agent maintient bien sa direction.
     * Si la direction renvoyée est null, cela signifie juste que l'agent garde son bloc sur lui après le déplacement.
     *
     * @param agent      Agent ayant reçu la perception
     * @param perception Perception de laquelle extraire une direction
     * @return (1) Direction cible de l'agent s'il doit déposer son bloc, (2) null sinon.
     * @throws MovableNotFoundException Si l'agent ne tient pas de bloc
     */
    @Override
    public Direction execute(Agent agent, Map<Direction, Movable> perception) throws MovableNotFoundException {
        if (!agent.isHolding()) throw new MovableNotFoundException("L'agent ne tient aucun bloc.");

        // L'agent ne fait aucun nouvelle rencontre.
        agent.visit(new Block(BlockValue.ZERO));

        double proba = computeProba(agent, new HashMap<>(perception));
        if (new Random().nextDouble() <= proba) {
            return goalDirection;
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
        double countBlocks = countBlocks(agent, perception);
        return countBlocks / perception.size();
    }

    /**
     * Compte le nombre de blocs de même valeur que celui tenu par l'agent dans son voisinage immédiat.
     *
     * @param agent      Agent inspectant son entourage
     * @param perception Perception de l'agent (voisinage immédiat)
     * @return Nombre de blocs de même valeur
     */
    private Double countBlocks(Agent agent, Map<Direction, Movable> perception) {
        double counterA = 0L;
        double counterB = 0L;
        Block block;
        for (Movable movable : perception.values()) {
            if (movable instanceof Block) {
                block = (Block) movable;
                if (block.getValue() == BlockValue.A) {
                    counterA++;
                } else {
                    counterB++;
                }
            }
        }

        // Bruitage (error de perception des agents)
        if (agent.getError() > 0) {
            counterA += counterB * agent.getError();
            counterB += counterA * agent.getError();
        }

        return agent.getHolding().getValue() == BlockValue.A ? counterA : counterB;
    }
}
