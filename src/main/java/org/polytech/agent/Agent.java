package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.Direction;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Agent intelligent parcourant un environnement.
 */
public class Agent implements Movable {

    private static Long COUNTER_INSTANTIATIONS = 0L;
    private Long id;

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
