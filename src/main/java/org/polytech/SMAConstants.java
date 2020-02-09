package org.polytech;

/**
 * Constantes utilisées globalement dans l'application.
 */
public class SMAConstants {

    /**
     * Nombre d'itérations des actions des agents (nombre de fois total où les agents agissent).
     */
    public final static int ITERATION_LOOPS = 1000;
    /***
     * Fréquence à laquelle afficher la grille pendant l'exécution de l'application par rapport au nombre d'itérations des actions des agents.
     */
    public final static double FREQUENCY_DISPLAY_GRID = 0d;
    /**
     * Taille du voisinage utilisé pour l'évaluation
     */
    public static final int NEIGHBOURHOOD_SIZE = 1;
    /**
     * Nombre de fois où un algo est exécuté (exécuter plusieurs fois donne des statistiques plus sûres)
     */
    public static final int NB_RUN = 10;
}
