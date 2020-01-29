package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.Direction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Agent intelligent parcourant un environnement comme un jambon.
 * Un agent est identifié par un Long auto-incrémenté avec l'instantiation d'un nouvel agent.
 */
public class Agent implements Movable {

    private static Long COUNTER_INSTANTIATIONS = 0L;
    private Long id;

    private double kPlus;
    private double k;

    Queue<BlockValue> memory;

    private Block holding;


    public Agent(int memorySize, double kPlus, double k) {
        this.kPlus = kPlus;
        this.k = k;
        attributeId();
        buildMemory(memorySize);
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

    // STRATEGY PICK UP ------------------------------------------------------------------------------------------------

    public Block pickUp(Map<Direction, Movable> neighbors) {
        neighbors.computeIfPresent()
    }

    /**
     * Détermine si l'agent prend le bloc ou non.
     * @param currentBlock: Bloc à tester
     * @return boolean
     */
    public boolean doITakeIt(Block currentBlock) {
        double proba = computeFPickUp(currentBlock);

        double rand = new Random().nextDouble();

        return (rand <= proba);
    }

    /**
     * Calcule la probabilité de prendre un bloc.
     * @return Probabilité.
     */
    public double computeProbaPickUp() {
        double f = computeFPickUp();
        return Math.pow((kPlus / (kPlus + f)), 2);
    }

    /**
     * Calcule le résultat de la fonction de prise.
     * @return Valeur f
     */
    public double computeFPickUp() {
        Long nbBlocks = computeBlockPickUp().getValue();
        return (double) nbBlocks / this.memory.size();
    }

    /**
     * Calcule le type de bloc qu'il est préférable de saisir.
     * @return Type de bloc avec le nombre d'occurences dans la mémoire
     */
    public Map.Entry<BlockValue, Long> computeBlockPickUp() {
        Map<BlockValue, Long> nbBlocks = countBlocks();
        return Collections.min(nbBlocks.entrySet(), Map.Entry.comparingByValue());
    }

    /**
     * Compte le nombre de blocs de chaque valeur dans la mémoire de l'agent.
     * @return Nombre d'éléments de chaque valeur dans la mémoire
     */
    public Map<BlockValue, Long> countBlocks() {
        Map<BlockValue, Long> result = new HashMap<>();
        Arrays.stream(BlockValue.values()).forEach(value -> result.put(value, countBlocks(value)));

        return result;
    }

    /**
     * Compte le nombre de blocs d'une certaine valeur dans la mémoire de l'agent.
     * @param value Valeur du bloc à compter.
     * @return Nombre d'éléments correspondants dans la mémoire
     */
    public Long countBlocks(BlockValue value) {
        return this.memory.stream().filter(val -> val == value).count();
    }

    /**
     * Calcule le f dans le cas où l'agent pose un objet
     * @param blocks: Blocks perçus autour de l'agent
     * @return double
     */
    public double getFPutDown(Map<Direction, Movable> blocks) {
        HashMap<BlockValue, Integer> numberOfBlocks = new HashMap<BlockValue, Integer>();
        numberOfBlocks.put(BlockValue.A, 0);
        numberOfBlocks.put(BlockValue.B, 0);

        for (Direction direction : blocks.keySet()) {
            Movable element = blocks.get(direction);

            if (element instanceof Block) {
                BlockValue blockValue = ((Block) element).getValue();
                numberOfBlocks.put(blockValue, numberOfBlocks.get(blockValue));
            }
        }

        double nb;

        switch (holding.getValue()) {
            case A : {
                nb = numberOfBlocks.get(BlockValue.A);
                break;
            }
            case B: {
                nb = numberOfBlocks.get(BlockValue.B);
                break;
            }
            default: {
                return 0;
            }
        }

        return nb / blocks.keySet().size();
    }

    // STRATEGY PUT DOWN -----------------------------------------------------------------------------------------------

    /**
     * Calcule la proba de poser le bloc tenu
     * @param blocks: Blocs perçus autour de l'agent
     * @return boolean
     */
    public boolean doIPutItDown(Map<Direction, Movable> blocks) {
        double f = getFPutDown(blocks);

        double proba = Math.pow((f / (k + f)), 2);
        double rand = new Random().nextDouble();

        return (rand <= proba);
    }

    public void visit(BlockValue value) {
        memory.add(value);
    }

    public boolean isHolding() {
        return this.holding == null;
    }

    @Override
    public String toString() {
        return Long.toString(id);
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
}
