package org.polytech.environnement;

import javafx.util.Pair;
import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.exceptions.CollisionException;
import org.polytech.environnement.exceptions.MovableNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Environnement étant caractérisé par une grille 2D contenant des blocs.
 * De manière générale la grille peut contenir des objets qui peuvent s'y déplacer (objets implémentant l'interface Movable).
 * @see Movable
 */
public class Environnement {

    /**
     * Grille sur laquelle évoluent les blocs.
     */
    private Movable[][] grid;

    /**
     * Nombre de blocs A dans l'environnement.
     */
    private int nbBlocksA;

    /**
     * Nombre de blocs B dans l'environnement.
     */
    private int nbBlocksB;


    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    private Environnement() {
    }

    public Environnement(int n, int m, int nbBlocksA, int nbBlocksB) throws IllegalArgumentException {
        if (nbBlocksA + nbBlocksB >= n * m)
            throw new IllegalArgumentException("Il y a trop d'entités par rapport aux dimensions de la grille.");

        this.grid = new Movable[n][m];
        insertBlocks(nbBlocksA, nbBlocksB);

        this.nbBlocksA = nbBlocksA;
        this.nbBlocksB = nbBlocksB;
    }

    /**
     * Insère des blocs de type A et B dans l'environnement.
     * Le comportement d'insertion des blocs est à définir dans les classes filles d'Environnement.
     * @param nbBlocksA Nombre de blocs A à insérer
     * @param nbBlocksB Nombre de blocs B à insérer
     */
    public void insertBlocks(int nbBlocksA, int nbBlocksB) {}

    // EXÉCUTION DES STRATÉGIES DES AGENTS -----------------------------------------------------------------------------

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

    // GESTION DE LA GRILLE --------------------------------------------------------------------------------------------

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
                sb.append(String.format("%s|", entity != null ? entity : "0"));

                if (j == grid[i].length - 1) sb.append("\n");
            }
        }

        return sb.toString();
    }

    public Movable[][] getGrid() {
        return grid;
    }

    /**
     * Renvoie le nombre de lignes de l'environnement.
     * @return Nombre de lignes de l'environnement
     * @see #grid
     */
    public int getNbRows() {
        return this.grid.length;
    }

    /**
     * Renvoie le nombre de colonnes de l'environnement.
     * @return Nombre de colonnes de l'environnement
     * @see #grid
     */
    public int getNbColumns() {
        return this.grid[0].length;
    }

    public int getNbBlocksA() {
        return nbBlocksA;
    }

    public int getNbBlocksB() {
        return nbBlocksB;
    }
}
