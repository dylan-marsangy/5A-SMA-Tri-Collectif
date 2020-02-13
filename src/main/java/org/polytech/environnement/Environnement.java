package org.polytech.environnement;

import javafx.util.Pair;
import org.polytech.agent.Agent;
import org.polytech.agent.strategies.StrategyMove;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.environnement.exceptions.CollisionException;
import org.polytech.environnement.exceptions.MovableNotFoundException;

import java.util.*;

public class Environnement implements Runnable {

    /**
     * Grille sur laquelle évoluent les agents et les blocs.
     */
    protected Movable[][] grid;

    /**
     * Nombre maximal d'itérations de la boucle des actions des agents.
     */
    private int nbIterations;
    /**
     * Fréquence à laquelle afficher la grille lors d'une exécution du monde.
     */
    private double frequencyDiplayGrid;

    /**
     * Collection des agents évoluent sur la grille.
     */
    protected Set<Agent> agents;

    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    private Environnement() {
    }

    public Environnement(int n, int m, int nbIterations, double frequencyDiplayGrid,
                         int nbAgents, int distance, int memorySize, double kPlus, double kMinus, double error,
                         int nbBlocksA, int nbBlocksB) throws IllegalArgumentException {
        if (nbAgents + nbBlocksA + nbBlocksB >= n * m)
            throw new IllegalArgumentException("Il y a trop d'entités par rapport aux dimensions de la grille.");

        this.grid = new Movable[n][m];
        this.nbIterations = nbIterations;
        this.frequencyDiplayGrid = frequencyDiplayGrid;

        placeAgentsOnGrid(nbAgents, distance, memorySize, kPlus, kMinus, error);
        insertBlocks(nbBlocksA, nbBlocksB);
    }

    public void placeAgentsOnGrid(int nbAgents, int distance, int memorySize, double kPlus, double k, double error) {}

    public void insertBlocks(int nbBlocksA, int nbBlocksB) {}

    // EXECUTION -------------------------------------------------------------------------------------------------------

    @Override
    public void run() {
        int frequency = (int) (nbIterations * frequencyDiplayGrid);
        System.out.print(this);
        System.out.println(String.format("0 / %d (0%%)", nbIterations));
        System.out.println();

        int count = 0;
        Agent agent;
        Movable obstacle;
        int distance;
        Map<Direction, Movable> perception; // Perception d'un agent.
        Direction goalDirection; // Résultat de l'exécution d'une stratégie de l'agent (déplacement, put down, pick up).
        while (count < nbIterations) {
            count++;

            // Tirage aléatoire d'un agent (simulation du multi-threading).
            agent = pickRandomAgent();
            distance = agent.getDistance(); // Distance de perception d'un agent.

            // Agent perçoit son environnement et détermine une direction dans laquelle se diriger (aléatoire).
            perception = perception(agent, distance);
            goalDirection = agent.execute(new StrategyMove(), perception);
            if (goalDirection != null) {
                obstacle = getEntityAfterMove(agent, goalDirection, distance);

                // Si l'obstacle est un autre agent, l'agent élu ne bouge pas et ne visite donc aucun nouveau bloc.
                if (obstacle instanceof Agent) {
                    agent.visit(new Block(BlockValue.ZERO));
                }

                // Si l'obstacle est un bloc (et que l'agent ne tient rien), il peut tenter de le prendre.
                else if (obstacle instanceof Block) {
                    if (!agent.isHolding()) {
                        /* Si l'agent maintient la direction cible après l'exécution de la stratégie 'Pick Up',
                        cela signifie qu'il prend le bloc. */
                        if (goalDirection.equals(agent.execute(new StrategyPickUp(goalDirection), perception))) {
                            pickUpBlock(agent, goalDirection, distance); // Prise
                            move(agent, goalDirection, distance); // Déplacement
                        }
                    }
                    // Sinon (si l'agent tient un bloc), il reste sur place et ne rencontre donc aucun nouveau bloc.
                    else {
                        agent.visit((Block) obstacle);
                    }
                }

                // S'il n'y a pas d'obstacle (et que l'agent tient un bloc), il tente de le déposer lors de son déplacement.
                else if (obstacle == null) {
                    // Déplacement
                    move(agent, goalDirection, distance);

                    // Dépôt du bloc sur la position d'origine si possible.
                    if (agent.isHolding()) {
                        /* Si l'agent maintient la direction cible après l'exécution de la stratégie 'Put Down',
                        cela signifie qu'il dépose le bloc sur sa position d'origine après le déplacement. */
                        goalDirection = agent.execute(new StrategyPutDown(goalDirection), perception);
                        if (goalDirection != null) {
                            assert goalDirection.contrary() != null; // Une direction a forcément un contraire.
                            putDownBlock(agent, goalDirection.contrary(), distance);
                        }
                    }
                }
            }
            // Si l'agent ne s'est pas déplacé, il n'a rencontré aucun nouveau bloc.
            else {
                agent.visit(new Block(BlockValue.ZERO));
            }

                // Affichage de la grille résultante si nécessaire.
                if (frequency != 0d && count % frequency == 0) {
                    System.out.print(this);
                    System.out.println(String.format("%d / %d (%.0f%%)", count, nbIterations, (double) count / nbIterations * 100));
                    System.out.println();
                }
            }

        if (frequency == 0 || count % frequency != 0) {
            System.out.print(this);
            System.out.println(String.format("%d / %d (100%%)", count, nbIterations));
            System.out.println();
        }
    }

    /**
     * Tire aléatoirement un agent parmi les agents disposés sur la grille.
     * @return Agent aléatoire
     */
    public Agent pickRandomAgent() {
        return new HashSet<>(agents).stream().skip(new Random().nextInt(agents.size())).findFirst().orElse(null);
    }

    /**
     * Fait prendre un bloc à un agent.
     *
     * @param agent     Agent prenant le bloc
     * @param direction Direction dans laquelle le bloc se situe
     * @param d         Distance de l'agent à laquelle se situe le bloc
     * @throws MovableNotFoundException S'il n'y a pas de bloc à la case cible
     */
    public void pickUpBlock(Agent agent, Direction direction, int d) throws MovableNotFoundException {
        Pair<Integer, Integer> coordinates = findEntity(agent);
        int xGoal = coordinates.getKey() + d * direction.x;
        int yGoal = coordinates.getValue() + d * direction.y;

        // Déposer le bloc.
        Movable entity = getEntity(xGoal, yGoal);
        if (!(entity instanceof Block)) throw new MovableNotFoundException("Il n'y a pas de bloc sur la case cible.");

        agent.pickUp((Block) entity); // Prend le bloc.
        remove(xGoal, yGoal);
    }

    /**
     * Dépose le bloc tenu par un agent.
     *
     * @param agent     Agent dont le bloc est à déposer
     * @param direction Direction dans laquelle poser le bloc
     * @param d         Distance sur laquelle poser le bloc
     * @throws CollisionException Si la case cible de dépôt est déjà occupée
     */
    public void putDownBlock(Agent agent, Direction direction, int d) {
        Pair<Integer, Integer> coordinates = findEntity(agent);
        int xGoal = coordinates.getKey() + d * direction.x;
        int yGoal = coordinates.getValue() + d * direction.y;

        // Déposer le bloc.
        Block block = agent.getHolding();
        insert(block, xGoal, yGoal);
        agent.putDown();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Déplace une entité dans une direction donnée sur une distance donnée si possible.
     *
     * @param entity    Entité à déplacer
     * @param direction Direction dans laquelle déplacer l'entité
     * @param d         Distance sur laquelle déplacer l'entité
     * @return (1) True si le mouvement a réussi, (2) false sinon.
     * @throws CollisionException Si le mouvement implique une collision
     */
    public boolean move(Movable entity, Direction direction, int d) throws CollisionException {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int x = coordinates.getKey() ;
        int y = coordinates.getValue();

        int xGoal = coordinates.getKey() + d * direction.x;
        int yGoal = coordinates.getValue() + d * direction.y;

        // Déplacement
        if (isInside(xGoal, yGoal)) {
            insert(entity, xGoal, yGoal);
            remove(x, y);
            return true;
        }

        return false;
    }

    /**
     * Réalise un déplacement fictif pour obtenir l'entité éventuellement présente sur la case cible.
     *
     * @param entity    Entité à déplacer virtuellement
     * @param direction Direction dans laquelle déplacer l'entité
     * @param d         Distance sur laquelle déplacer l'entité
     * @return Entité présente sur la case cible (null si elle est vide)
     */
    public Movable getEntityAfterMove(Movable entity, Direction direction, int d) throws CollisionException {
        Pair<Integer, Integer> coordinates = findEntity(entity);
        int xGoal = coordinates.getKey() + d * direction.x;
        int yGoal = coordinates.getValue() + d * direction.y;

        if (isInside(xGoal, yGoal)) {
            return getEntity(xGoal, yGoal);
        }

        return null;
    }

    /**
     * Perception d'une entité dans toutes les directions sur une certaine distance.
     * S'il y a un mur dans une direction, elle n'est pas répertoriée dans le résultat.
     *
     * @param entity Entité qui cherche à scanner son environnement
     * @param d      Distance à scanner
     * @return Objets qui se trouve à la d-ième case dans toutes les directions par rapport à l'entité
     */
    public Map<Direction, Movable> perception(Movable entity, int d) {
        Map<Direction, Movable> neighbors = new HashMap<>();

        Pair<Integer, Integer> coordinates;
        int xGoal, yGoal;
        for (Direction direction : Direction.values()) {
            coordinates = findEntity(entity);
            xGoal = coordinates.getKey() + d * direction.x;
            yGoal = coordinates.getValue() + d * direction.y;

            if (isInside(xGoal, yGoal)) {
                neighbors.put(direction, getEntity(xGoal, yGoal));
            }
        }

        return neighbors;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indique si la position appartient à la grille ou non.
     *
     * @param x Abscisse de la position
     * @param y Ordonnée de la position
     * @return (1) True si la position est à l'intérieur de la grille, (2) false sinon.
     */
    public boolean isInside(int x, int y) {
        return (x >= 0 && x < grid.length &&
                y >= 0 && y < grid[0].length);
    }

    /**
     * Vide une case de la grille.
     *
     * @param x Abscisse de la case à vider
     * @param y Ordonnée de la case à vider
     */
    public void remove(int x, int y) {
        this.grid[x][y] = null;
    }

    /**
     * Insère une entité sur la grille si possible.
     *
     * @param entity Entité à insérer
     * @param x      Abscisse de la case dans laquelle insérer l'entité
     * @param y      Ordonnée de la case dans laquelle insérer l'entité
     * @throws CollisionException Si la case est en dehors de la grille ou déjà occupée par une autre entité
     */
    public void insert(Movable entity, int x, int y) throws CollisionException {
        if (isEmpty(x, y)) this.grid[x][y] = entity;
        else throw new CollisionException("Une entité est déjà présente sur la case.");
    }

    /**
     * Renvoie l'entité éventuellement présente dans une case de la grille.
     *
     * @param x Abscisse de la case
     * @param y Ordonnée de la case
     * @return Entité présente sur la case, null si elle est vide.
     */
    public Movable getEntity(int x, int y) {
        return this.grid[x][y];
    }

    /**
     * Indique si une entité est présente ou non dans une case de la grille.
     *
     * @param i Abscisse de la position de l'entité
     * @param j Ordonnée de la position de l'entité
     * @return (1) True si la case est vide, (2) false sinon.
     */
    public boolean isEmpty(int i, int j) {
        return getEntity(i, j) == null;
    }

    /**
     * Renvoie les coordonnées d'une entité de la grille.
     * @param entity Entité à rechercher
     * @return Coordonnées x et y de la position de l'entité dans la grille (matrice)
     * @throws MovableNotFoundException Si l'entité n'existe pas sur la grille
     */
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

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Movable entity;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                entity = getEntity(i, j);
                if (entity != null) {
                    sb.append(String.format(" %s |", entity));
                } else {
                    sb.append(" 0 |");
                }

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
