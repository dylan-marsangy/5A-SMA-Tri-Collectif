package org.polytech.system;

import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environment.Environment;
import org.polytech.environment.RandomEnvironment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Fabrique permettant l'instantiation d'objets SystemMA.
 */
public class SystemMAFactory {

    private SystemMAFactory() {}

    /**
     * Instancie un objet {@link SystemMA} composé d'un environnement aléatoire et d'agents placés aléatoirement dans cet environnement.
     * @param n Nombre de lignes de la grille de l'environnement
     * @param m Nombre de colonnes de la grille de l'environnement
     * @param nbAgents Nombre d'agents placés dans l'environnement
     * @param nbBlocksA Nombre de blocs de type A placés dans l'environnement
     * @param nbBlocksB Nombre de blocs de type B placés dans l'environnement
     * @param i Distance de déplacement et perception des agents
     * @param t Taille de la mémoire des agents
     * @param kPlus Valeur K+ des agents
     * @param kMinus Valeur K- des agents
     * @param error Valeur d'erreur de reconnaissance des objets des agents (e)
     * @return Un système aléatoire
     */
    public static SystemMA instantiateRandom(int n, int m, int nbAgents, int nbBlocksA, int nbBlocksB,
                                       int i, int t, double kPlus, double kMinus, double error) {
        // Génération de l'environnement
        Environment environment = new RandomEnvironment(n, m, nbBlocksA, nbBlocksB);

        // Génération des agents
        Set<Agent> agents = new HashSet<>();
        IntStream.rangeClosed(1, nbAgents).forEach(index ->
                agents.add(new Agent(i, t, kPlus, kMinus, error)));

        // Génération du système (place les agents dans l'environnement)
        SystemMA system = new SystemMA(environment, agents, SMAConstants.ITERATION_LOOPS, SMAConstants.FREQUENCY_DISPLAY_GRID);
        system.placeAgentsOnGrid(agents);
        return system;
    }
}
