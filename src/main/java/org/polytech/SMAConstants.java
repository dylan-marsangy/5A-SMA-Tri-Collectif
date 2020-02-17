package org.polytech;

/**
 * Constantes utilisées globalement dans l'application.
 */
public class SMAConstants {

    // ALGORITHME ------------------------------------------------------------------------------------------------------

    /**
     * Nombre d'itérations des actions des agents (nombre de fois total où les agents agissent).
     */
    public final static int ITERATION_LOOPS = 570000;
    /***
     * Fréquence à laquelle afficher la grille pendant l'exécution de l'application par rapport au nombre d'itérations des actions des agents.
     * Si la fréquence est à 0, alors seulement l'état initial et final de l'environnement sont montrés.
     */
    public final static double FREQUENCY_DISPLAY_GRID = 0.25;

    // ENVIRONNEMENT & AGENTS ------------------------------------------------------------------------------------------

    // ENVIRONNEMENT

    /**
     * Nombre de lignes de la grille de l'environnement.
     */
    public static final int GRID_ROWS = 50; // N
    /**
     * Nombre de colonnes de la grille de l'environnement.
     */
    public static final int GRID_COLUMNS = 50; // M
    /**
     * Nombre de blocs A à générer dans l'environnement.
     */
    public static final int NUMBER_BLOCKS_A = 200;
    /**
     * Nombre de blocs B à générer dans l'environnement.
     */
    public static final int NUMBER_BLOCKS_B = 200;
    /**
     * Nombre d'agents à générer et placer dans l'environnement.
     */
    public static final int NUMBER_AGENTS = 20;

    // AGENTS

    /**
     * Taille de la mémoire des agents.
     */
    public static final int MEMORY_SIZE = 10; // t
    /**
     * Distance de perception et de déplacement des agents.
     */
    public static final int SUCCESSIVE_MOVEMENTS = 1; // i
    /**
     * Valeur k+ des agents.
     */
    public static final double K_PLUS = 0.1; // k+
    /**
     * Valeur k- des agents.
     */
    public static final double K_MINUS = 0.3; // k-
    /**
     * Valeur d'erreur de perception des agents.
     */
    public static final double ERROR = 0d; // e

    // EVALUATION ------------------------------------------------------------------------------------------------------

    /**
     * Taille du voisinage utilisé pour l'évaluation.
     */
    public static final int NEIGHBOURHOOD_SIZE = 1;
    /**
     * Nombre de fois où un algorithme est exécuté.
     * Exécuter plusieurs fois donne des statistiques plus sûres.
     */
    public static final int NB_RUN = 10;
}
