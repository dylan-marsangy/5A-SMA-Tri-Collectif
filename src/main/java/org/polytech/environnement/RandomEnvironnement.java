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

    public RandomEnvironnement(int n, int m, int nbBlocksA, int nbBlocksB) {
        super(n, m, nbBlocksA, nbBlocksB);
    }

    /**
     * Insère aléatoirement les blocs A et B dans l'environnement.
     * @param nbBlocksA Nombre de blocs A à insérer
     * @param nbBlocksB Nombre de blocs B à insérer
     */
    @Override
    public void insertBlocks(int nbBlocksA, int nbBlocksB) {
        int n = getNbRows();
        int m = getNbRows();

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
