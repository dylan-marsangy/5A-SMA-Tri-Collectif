package org.polytech.environnement;

import javafx.util.Pair;
import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.*;

public class Environnement implements Runnable {

    /**
     * Grille sur laquelle évoluent les agents et les blocs.
     */
    Movable[][] grid;
    /**
     * Nombre d'agents sur la grille.
     */
    private int nbAgents;
    /**
     * Nombre de blocs sur la grille.
     *  */
    private int nbBlocks;

    /**
     * Collection des agents évoluent sur la grille.
     */
    private Set<Agent> agents;

    private Environnement() {}

    public Environnement(int n, int m, int nbAgents, int nbObjects) {
        this.grid = new Movable[n][m];
        this.nbAgents = nbAgents;
        this.nbBlocks = nbObjects;
    }

    public void insertAgents(int memorySize) {
        Random rand = new Random();
        int x, y;
        int n = grid.length;
        int m = grid[0].length;

        agents = new HashSet<>();
        for (int i = 0 ; i < nbAgents ; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            }
            while (!isEmpty(x, y));

            Agent entity = new Agent(memorySize);
            insert(entity, x, y);
            agents.add(entity);
        }
    }

    public void insertBlocks() {
        Random rand = new Random();
        int x, y;
        int n = grid.length;
        int m = grid[0].length;
        BlockValue value;
        int randIndex;

        for (int i = 0; i < nbBlocks; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            }
            while (!isEmpty(x, y));

            randIndex = rand.nextInt(2);

            if (randIndex == 0) {
                value = BlockValue.A;
            }
            else {
                value = BlockValue.B;
            }

            Movable entity = new Block(value);
            insert(entity, x, y);
        }
    }

    /**
     * Déplacement une entité dans une direction donnée.
     * @param entity Entité à déplacer
     * @param direction Direction dans laquelle déplacer l'entité
     * @throws CollisionException Si le mouvement implique une collision
     */
    public void move(Movable entity, Direction direction) throws CollisionException {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int x = coordinates.getKey() ;
        int y = coordinates.getValue();

        int xGoal = coordinates.getKey() + direction.x;
        int yGoal = coordinates.getValue() + direction.y;

        // Déplacement
        insert(entity, xGoal, yGoal);
        remove(x, y);
    }

    /**
     * Perception d'une entité dans toutes les directions sur une certaine distance.
     * @param entity Entité qui cherche à scanner son environnement
     * @param d Distance à scanner
     * @return Objets qui se trouve à la d-ième case dans toutes les directions par rapport à l'entité
     */
    public Map<Direction, Movable> perception(Movable entity, int d) {
        Map<Direction, Movable> neighbors = new HashMap<>();

        for (Direction direction : Direction.values()) {
            neighbors.put(direction, perception(entity, direction, d));
        }

        return neighbors;
    }

    /**
     * Perception d'une entité dans une direction sur une certaine distance.
     * @param entity Entité qui cherche à scanner son environnement
     * @param direction Direction dans laquelle l'entité scanne
     * @param d Distance à scanner
     * @return Objet qui se trouve à la d-ième case par rapport à l'entité dans la direction souhaitée
     */
    public Movable perception(Movable entity, Direction direction, int d) {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int xGoal = coordinates.getKey() + d * direction.x;
        int yGoal = coordinates.getValue() + d * direction.y;

        return getEntity(xGoal, yGoal);
    }

    public void remove(int x, int y) {
        if (isInside(x, y)) this.grid[x][y] = null;
        else throw new CollisionException("Vous êtes en train de casser les murs. >:(");
    }

    public void insert(Movable entity, int x, int y) throws CollisionException {
        if (isInside(x, y)) {
            if (isEmpty(x, y)) this.grid[x][y] = entity;
            else throw new CollisionException("Une entité est déjà présente sur la case.");
        } else throw new CollisionException("L'entité ne peut pas rentrer dans les murs.");

    }

    public Movable getEntity(int x, int y) {
        return (isInside(x, y)) ? this.grid[x][y] : null;
    }

    public boolean isInside(int x, int y) {
        return (x >=0 && x < grid.length &&
                y >=0 && y < grid[0].length);
    }

    public boolean isEmpty(int i, int j) {
        return getEntity(i, j) == null;
    }

    public Pair<Integer, Integer> findEntity(Movable entity) throws MovableNotFoundException {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (entity.equals(grid[i][j])) {
                    return new Pair<>(i, j);
                }
            }
        }

        throw new MovableNotFoundException("Entity does not exist on the grid.");
    }

    public Agent pickRandomAgent() {
        return agents.stream().skip(new Random().nextInt(agents.size())).findFirst().orElse(null);
    }

    @Override
    public void run() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Movable entity;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                entity = getEntity(i, j);
                sb.append(String.format(" %s |", entity != null ? entity.toString() : "0"));

                if (j == grid[i].length - 1) sb.append("\n");

            }
        }

        return sb.toString();
    }

    public Movable[][] getGrid() {
        return grid;
    }

    public void setGrid(Movable[][] grid) {
        this.grid = grid;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }
}
