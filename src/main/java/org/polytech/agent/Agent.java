package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.Direction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Agent intelligent parcourant un environnement.
 */
public class Agent implements Movable {

    private static Long COUNTER_INSTANTIATIONS = 0L;
    private Long id;

    private double kPlus;
    private double k;

    Queue<BlockValue> memory;

    private Block holding;

    private Agent(double kPlus, double k) {
        this.kPlus = kPlus;
        this.k = k;
        attributeId();
    }

    public Agent(int memorySize) {
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

    private HashMap<BlockValue, Integer> getNumberOfBlocksInMemory() {
        Iterator<BlockValue> it = memory.iterator();
        HashMap<BlockValue, Integer> numberOfBlocks = new HashMap<BlockValue, Integer>();
        numberOfBlocks.put(BlockValue.A, 0);
        numberOfBlocks.put(BlockValue.B, 0);

        while(it.hasNext()){
            BlockValue block = it.next();

            switch (block) {
                case A:
                {
                    numberOfBlocks.put(BlockValue.A, numberOfBlocks.get(BlockValue.A) + 1);
                    break;
                }
                case B:
                {
                    numberOfBlocks.put(BlockValue.B, numberOfBlocks.get(BlockValue.B) + 1);
                    break;
                }
                default:
                {
                    break;
                }
            }
        }

        return numberOfBlocks;
    }

    private double getFTake(BlockValue currentBlock) {
        double nb;
        HashMap<BlockValue, Integer> numberOfBlocks = getNumberOfBlocksInMemory();

        switch (currentBlock) {
            case A: {
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

        return nb / memory.size();
    }

    private boolean doITakeIt(BlockValue currentBlock) {
        double f = getFTake(currentBlock);

        double proba = Math.pow((kPlus / (kPlus + f)), 2);
        double rand = new Random().nextDouble();

        return (rand <= proba);
    }

    private boolean doIPutItDown(BlockValue currentBlock) {
        double f = getFTake(currentBlock);

        double proba = Math.pow((f / (k + f)), 2);
        double rand = new Random().nextDouble();

        return (rand <= proba);
    }

    @Override
    public void move(Direction direction) {

    }

    public void visit(BlockValue value) {
        memory.add(value);
        System.out.println(memory.toString());
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
