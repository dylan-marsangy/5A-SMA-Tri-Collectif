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
import org.polytech.statistiques.Evaluation;
import org.polytech.statistiques.ExcelGenerator;
import org.polytech.utils.Color;

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
        int distance;
        Map<Direction, Movable> perception; // Perception d'un agent.
        Direction result; // Résultat de l'exécution d'une stratégie de l'agent (déplacement, put down, pick up).
        while (count < nbIterations) {
            count++;

            // Tirage aléatoire d'un agent (simulation du multi-threading).
            agent = pickRandomAgent();
            distance = agent.getDistance(); // Distance de perception d'un agent.

            // Agent perçoit son environnement et détermine une direction dans laquelle se diriger.
            // L'environnement l'y déplace.
            perception = perception(agent, distance);
            result = agent.execute(new StrategyMove(), perception);
            if (result != null) move(agent, result, distance);

            // S'il tient un bloc, il va chercher à le déposer. Sinon, il cherche à en prendre un.
            perception = perception(agent, distance);
            if (agent.isHolding()) {
                result = agent.execute(new StrategyPutDown(), perception);
                if (result != null) putDownBlock(agent, result, distance);
            } else {
                result = agent.execute(new StrategyPickUp(), perception);
                if (result != null) pickUpBlock(agent, result, distance);
            }

            // Affichage de la grille résultante si nécessaire.
            if (frequency != 0d && count % frequency == 0) {
                System.out.print(this);
                System.out.println(String.format("%d / %d (%.0f%%)", count, nbIterations, (double) count / nbIterations * 100));
                System.out.println();
            }
        }

        if (frequency == 0) {
            System.out.print(this);
            System.out.println(String.format("%d / %d (100%%)", count, nbIterations));
            System.out.println();
        }

        Evaluation evaluation = new Evaluation(this);
        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(evaluation);
        ExcelGenerator excelGenerator = new ExcelGenerator(evaluations);
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
        Color format;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                entity = getEntity(i, j);

                if (entity != null) {
                    // Attribuer une couleur d'output selon le type de Movable (Agent ou Bloc A/B).
                    if (entity instanceof Block && ((Block) entity).getValue() == BlockValue.A)
                        format = Color.BLUE_BACKGROUND;
                    else if (entity instanceof Block && ((Block) entity).getValue() == BlockValue.B)
                        format = Color.RED_BACKGROUND;
                    else format = Color.YELLOW;

                    sb.append(String.format(" %s |", format + entity.toString() + Color.RESET));
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
