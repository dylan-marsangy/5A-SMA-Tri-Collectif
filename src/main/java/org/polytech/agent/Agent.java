package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.agent.strategies.Strategy;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;

import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Agent intelligent parcourant un environnement comme un jambon.
 * Un agent est identifié par un Long auto-incrémenté avec l'instantiation d'un nouvel agent.
 */
public class Agent implements Movable {

    private static Long COUNTER_INSTANTIATIONS = 0L;
    private Long id;

    private double kPlus;
    private double kMinus;

    Queue<BlockValue> memory;

    private Block holding;

    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    private Agent() {
        attributeId();
    }

    public Agent(int memorySize, double kPlus, double kMinus) {
        attributeId();
        buildMemory(memorySize);

        this.kPlus = kPlus;
        this.kMinus = kMinus;
    }

    private void attributeId() {
        COUNTER_INSTANTIATIONS++;
        this.id = COUNTER_INSTANTIATIONS;
    }

    private void buildMemory(int memorySize) {
        this.memory = new CircularFifoQueue<BlockValue>(memorySize) {
            @Override
            public String toString() {
                return memory.stream().map(Enum::toString).collect(Collectors.joining(""));
            }
        };

        for (int i = 0; i < memorySize ; i++) {
            memory.add(BlockValue.ZERO);
        }
    }

    // STRATEGY --------------------------------------------------------------------------------------------------------

    /**
     * Exécute une stratégie.
     *
     * @param strategy   Stratégie à exécuter
     * @param perception Perception sur laquelle se baser
     * @return Résultat de la stratégie (interprétation dépendante de la stratégie appliquée)
     */
    public Direction execute(Strategy strategy, Map<Direction, Movable> perception) {
        return strategy.execute(this, perception);
    }

    /**
     * Prend un bloc.
     * @param block Bloc à prendre
     * @throws CollisionException Si l'agent tient déjà un bloc
     */
    public void pickUp(Block block) throws CollisionException {
        if (isHolding()) throw new CollisionException("L'agent tient déjà un bloc.");
        else setHolding(block); visit(block);
    }

    /**
     * Visite un bloc (l'ajoute dans la mémoire de l'agent).
     * @param block Bloc rencontré
     */
    public void visit(Block block) {
        memory.add(block.getValue());
    }

    /**
     * Indique si l'agent tient un bloc ou non.
     * @return (1) True si l'agent tient un bloc, (2) false sinon
     */
    public boolean isHolding() {
        return this.holding != null;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
//        return Long.toString(id);
        return "X";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        return id.equals(agent.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static void cleanID() {
        COUNTER_INSTANTIATIONS = 0L;
    }

    public Long getID() { return this.id; }

    public Queue<BlockValue> getMemory() {
        return this.memory;
    }

    public void setMemory(Queue<BlockValue> memory) {
        this.memory = memory;
    }

    public Block getHolding() {
        return holding;
    }

    public void setHolding(Block holding) {
        this.holding = holding;
    }

    public int getMemorySize() {
        return this.memory.size();
    }

    public void setMemorySize(int memorySize) {
        Queue<BlockValue> clone = new CircularFifoQueue<>(memorySize);
        clone.addAll(this.memory);
        this.memory = clone;
    }

    public double getkPlus() {
        return kPlus;
    }

    public void setkPlus(double kPlus) {
        this.kPlus = kPlus;
    }

    public double getkMinus() {
        return kMinus;
    }

    public void setkMinus(double kMinus) {
        this.kMinus = kMinus;
    }
}
