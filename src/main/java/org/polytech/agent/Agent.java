package org.polytech.agent;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.polytech.agent.strategies.Strategy;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;
import org.polytech.environnement.exceptions.MovableNotFoundException;

import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Agent intelligent parcourant un environnement comme un jambon.
 * Un agent est identifié par un Long auto-incrémenté avec l'instantiation d'un nouvel agent.
 */
public class Agent implements Movable {

    /**
     * Nombre d'agents instanciés depuis que l'application tourne.
     **/
    private static Long COUNTER_INSTANTIATIONS = 0L;
    /**
     * ID de l'agent (auto-incrémenté).
     */
    private Long id;

    /**
     * Distance de perception d'un agent.
     */
    private int distance;
    /**
     * Constante k+ utilisée dans la formule de probabilité de prise et dépôt de bloc.
     */
    private double kPlus;
    /**
     * Constante k- utilisée dans la formule de probabilité de prise et dépôt de bloc.
     */
    private double kMinus;
    /** Taux d'erreur dans la reconnaissances des objets rencontrés.
     *
     */
    private double error;

    /**
     * Mémoire de l'agent.
     */
    Queue<BlockValue> memory;

    /**
     * Prise de l'agent.
     */
    private Block holding;

    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    private Agent() {
        attributeId();
    }

    public Agent(int distance, int memorySize, double kPlus, double kMinus, double error) {
        attributeId();
        buildMemory(memorySize);

        this.distance = distance;
        this.kPlus = kPlus;
        this.kMinus = kMinus;
        this.error = error;
    }

    /**
     * Auto-incrémente l'ID des agents.
     */
    private void attributeId() {
        COUNTER_INSTANTIATIONS++;
        setID(COUNTER_INSTANTIATIONS);
    }

    /**
     * Construit la mémoire des agents (initialise la queue et la remplit de "0").
     * @param memorySize Taille de la mémoire
     */
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

        setHolding(block);
        visit(block);
    }

    /**
     * Dépose un bloc.
     * @throws MovableNotFoundException Si l'agent ne tient pas de bloc
     *
     */
    public void putDown() throws MovableNotFoundException {
        if (!isHolding()) throw new MovableNotFoundException("L'agent ne tient pas de bloc");

        setHolding(null);
    }

    /**
     * Visite un bloc (l'ajoute dans la mémoire de l'agent le type du bloc).
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

    /**
     * Réinitialise le compteur d'instanciations des agents à 0.
     */
    public static void cleanID() {
        COUNTER_INSTANTIATIONS = 0L;
    }

    public Long getID() { return this.id; }

    private void setID(Long id) { this.id = id; }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

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

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }
}
