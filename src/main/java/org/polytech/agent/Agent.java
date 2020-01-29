package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.Direction;

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

    private final double kPlus = 0.1;
    private final double kMinus = 0.3;

    Queue<BlockValue> memory;

    private Block holding;

    private Agent() {
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
    }

    private boolean doITakeIt(BlockValue currentBlock) {
        Iterator<BlockValue> it = memory.iterator();
        int cptA = 0;
        int cptB = 0;

        while(it.hasNext()){
            BlockValue block = it.next();   // o a pour valeur un objet de la collectio

            switch (block) {
                case A:
                {
                    ++cptA;
                    break;
                }
                case B:
                {
                    ++cptB;
                    break;
                }
                default:
                {
                    break;
                }
            }
        }

        int f;

        switch (currentBlock) {
            case A: {
                f = cptA;
                break;
            }
            case B: {
                f = cptB;
                break;
            }
            default: {
                return false;
            }
        }
        double proba = (kPlus / (kPlus + f));
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
