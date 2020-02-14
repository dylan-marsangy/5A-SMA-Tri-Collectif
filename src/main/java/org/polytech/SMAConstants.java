package org.polytech;

/**
 * Constantes utilisées globalement dans l'application.
 */
public class SMAConstants {

    // ALGORITHME ------------------------------------------------------------------------------------------------------

    /**
     * Nombre d'itérations des actions des agents (nombre de fois total où les agents agissent).
     */
    public final static int ITERATION_LOOPS = 1600000;
    /***
     * Fréquence à laquelle afficher la grille pendant l'exécution de l'application par rapport au nombre d'itérations des actions des agents.
     * Si la fréquence est à 0, alors seulement l'état initial et final de l'environnement sont montrés.
     */
    public final static double FREQUENCY_DISPLAY_GRID = 0.25;

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
