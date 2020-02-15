package org.polytech.system;

import org.polytech.agent.Agent;
import org.polytech.agent.strategies.StrategyMove;
import org.polytech.agent.strategies.StrategyPickUp;
import org.polytech.agent.strategies.StrategyPutDown;
import org.polytech.environment.Direction;
import org.polytech.environment.Environment;
import org.polytech.environment.Movable;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;

import java.util.*;

/**
 * Système étant principalement caractérisé par un environnement (avec les blocs à trier) et les agents y évoluant.
 * Les agents sont placés aléatoirement dans l'environnement lors de l'instanciation d'un système.
 */
public class SystemMA {

    /**
     * Collection des agents évoluant dans l'environnement.
     *
     * @see #environment
     */
    protected Set<Agent> agents;
    /**
     * Environnement composé de blocs à trier sur une grille.
     */
    private Environment environment;

    /**
     * Nombre d'itérations de l'exécution de l'algorithme (nombre de fois où un agent exécute une action dans l'environnement).
     */
    private int nbIterations;

    /**
     * Fréquence d'affichage de la grille lors d'une exécution de l'algorithme.
     */
    private double frequencyDiplayGrid;

    private SystemMA() {}

    public SystemMA(Environment environment,
                    Set<Agent> agents,
                    int nbIterations, double frequencyDiplayGrid) throws IllegalArgumentException {
        Map<BlockValue, Integer> countBlocks = environment.getNbBlocks();
        if (agents.size() + countBlocks.get(BlockValue.A) + countBlocks.get(BlockValue.B)
                >= environment.getNbRows() * environment.getNbColumns())
            throw new IllegalArgumentException("Il y a trop d'entités par rapport aux dimensions de la grille.");

        this.environment = environment;
        this.nbIterations = nbIterations;
        this.frequencyDiplayGrid = frequencyDiplayGrid;

        this.agents = agents;
        placeAgentsOnGrid();
    }

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    /**
     * Place aléatoirement les {@link #agents} dans l'environnement.
     */
    public void placeAgentsOnGrid() {
        Random rand = new Random();
        int n = environment.getNbRows();
        int m = environment.getNbColumns();

        agents.forEach(agent -> {
            int x, y;
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            } while (!environment.isEmpty(x, y));

            environment.insert(agent, x, y);
        });
    }

    // EXECUTION -------------------------------------------------------------------------------------------------------

    /**
     * Exécute l'algorithme de tri collectif (exécution du système) sur plusieurs itérations.
     */
    public void run() {
        int frequency = (int) (nbIterations * frequencyDiplayGrid);
        int count = 0;

        displayProgress(count);

        while (count < nbIterations) {
            count++;

            execute();

            // Affichage de la grille résultante si nécessaire.
            if (frequency != 0d && count % frequency == 0) {
                displayProgress(count);
            }
        }

        if (frequency == 0 || count % frequency != 0) {
            displayProgress(count);
        }
    }

    /**
     * Exécute l'algorithme de tri collectif (exécution du système) sur une itération.
     */
    public void execute() {
        // Tirage aléatoire d'un agent (simulation du multi-threading).
        Agent agent = pickRandomAgent();
        int distance = agent.getDistance(); // Distance de perception d'un agent.

        // Agent perçoit son environnement et détermine une direction dans laquelle se diriger (aléatoire).
        Map<Direction, Movable> perception = environment.perception(agent, distance);
        Direction goalDirection = agent.execute(new StrategyMove(), perception);
        if (goalDirection != null) {
            Movable obstacle = environment.getEntityAfterMove(agent, goalDirection, distance);

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
                        environment.pickUpBlock(agent, goalDirection, distance); // Prise
                        environment.move(agent, goalDirection, distance); // Déplacement
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
                environment.move(agent, goalDirection, distance);

                // Dépôt du bloc sur la position d'origine si possible.
                if (agent.isHolding()) {
                        /* Si l'agent maintient la direction cible après l'exécution de la stratégie 'Put Down',
                        cela signifie qu'il dépose le bloc sur sa position d'origine après le déplacement. */
                    goalDirection = agent.execute(new StrategyPutDown(goalDirection), perception);
                    if (goalDirection != null) {
                        assert goalDirection.contrary() != null; // Une direction a forcément un contraire.
                        environment.putDownBlock(agent, goalDirection.contrary(), distance);
                    }
                }
            }
        }
        // Si l'agent ne s'est pas déplacé, il n'a rencontré aucun nouveau bloc.
        else {
            agent.visit(new Block(BlockValue.ZERO));
        }
    }

    /**
     * Affiche en console le progrès de l'exécution de l'algorithme.
     * @param count Étape actuelle de l'algorithme
     */
    private void displayProgress(int count) {
        System.out.print(environment);
        System.out.println(String.format("%d / %d (%.0f%%)", count, nbIterations, (double) count / nbIterations * 100));
        System.out.println();
    }

    /**
     * Tire aléatoirement un agent parmi les {@link #agents agents disposés dans l'environnement}.
     *
     * @return Agent aléatoire
     */
    public Agent pickRandomAgent() {
        return new HashSet<>(agents).stream().skip(new Random().nextInt(agents.size())).findFirst().orElse(null);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Crée une copie de l'objet actuel.
     *
     * @return Copie de l'objet
     */
    public SystemMA copy() {
        SystemMA copy = new SystemMA();
        copy.environment = this.environment.copy();
        copy.agents = new HashSet<>(agents);
        copy.nbIterations = this.nbIterations;
        copy.frequencyDiplayGrid = this.frequencyDiplayGrid;
        return copy;
    }

    public Set<Agent> getAgents() {
        return agents;
    }


    public Environment getEnvironment() {
        return environment;
    }

    public int getNbIterations() {
        return nbIterations;
    }

    public double getFrequencyDiplayGrid() {
        return frequencyDiplayGrid;
    }

}
