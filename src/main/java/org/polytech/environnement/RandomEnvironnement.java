package org.polytech.environnement;

import org.polytech.agent.Agent;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.HashSet;
import java.util.Random;

/**
 * Environnement aléatoire (génération aléatoire des agents et blocs sur la grille).
 */
public class RandomEnvironnement extends Environnement {

    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    public RandomEnvironnement(int n, int m, int nbIterations, double frequencyDiplayGrid,
                               int nbAgents, int distance, int memorySize, double kPlus, double kMinus, double error,
                               int nbBlocksA, int nbBlocksB) {
        super(n, m, nbIterations, frequencyDiplayGrid,
                nbAgents, distance, memorySize, kPlus, kMinus, error,
                nbBlocksA, nbBlocksB);
    }

    @Override
    public void placeAgentsOnGrid(int nbAgents, int distance, int memorySize, double kPlus, double kMinus, double error) {
        Random rand = new Random();
        int x, y;
        int n = grid.length;
        int m = grid[0].length;

        agents = new HashSet<>();
        for (int i = 0; i < nbAgents; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            } while (!isEmpty(x, y));

            Agent entity = new Agent(distance, memorySize, kPlus, kMinus, error);
            insert(entity, x, y);
            agents.add(entity);
        }
    }

    @Override
    public void insertBlocks(int nbBlocksA, int nbBlocksB) {
        int n = grid.length;
        int m = grid[0].length;

        Random rand = new Random();
        int x, y;
        for (int i = 0; i < nbBlocksA; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            } while (!isEmpty(x, y));

            insert(new Block(BlockValue.A), x, y);
        }

        for (int i = 0; i < nbBlocksB; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            } while (!isEmpty(x, y));

            insert(new Block(BlockValue.B), x, y);
        }
    }
}
