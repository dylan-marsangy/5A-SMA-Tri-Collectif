package org.polytech.environnement;

import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;

import java.util.Random;

/**
 * Environnement aléatoire (génération aléatoire des agents et blocs sur la grille).
 */
public class RandomEnvironnement extends Environnement {

    // CONSTRUCTORS ----------------------------------------------------------------------------------------------------

    /**
     * Génère un environnement dans lequel les blocs sont aléatoirement placés.
     *
     * @param n         Nombre de lignes de la grille
     * @param m         Nombre de colonnes de la grille
     * @param nbBlocksA Nombre de blocs de type A à placer
     * @param nbBlocksB Nombre de blocs de type B à placer
     * @throws IllegalArgumentException S'il y a trop de blocs sur la grille
     */
    public RandomEnvironnement(int n, int m, int nbBlocksA, int nbBlocksB) throws IllegalArgumentException {
        super(n, m);

        if (nbBlocksA + nbBlocksB >= n * m)
            throw new IllegalArgumentException("Il y a trop de blocs dans l'environnement.");
        else insertBlocks(nbBlocksA, nbBlocksB);
    }

    /**
     * Insère aléatoirement les blocs A et B dans l'environnement.
     *
     * @param nbBlocksA Nombre de blocs de type A à placer
     * @param nbBlocksB Nombre de blocs de type B à placer
     */
    public void insertBlocks(int nbBlocksA, int nbBlocksB) {
        insertTypeBlocks(BlockValue.A, nbBlocksA);
        insertTypeBlocks(BlockValue.B, nbBlocksB);
    }

    /**
     * Insère un certain nombre d'un type de bloc particulier dans l'environnement.
     *
     * @param value    Type de  bloc à insérer
     * @param nbBlocks Nombre de blocs à insérer
     */
    private void insertTypeBlocks(BlockValue value, int nbBlocks) {
        int n = getNbRows();
        int m = getNbRows();

        Random rand = new Random();
        int x, y;
        for (int i = 0; i < nbBlocks; i++) {
            do {
                x = rand.nextInt(n);
                y = rand.nextInt(m);
            } while (!isEmpty(x, y));

            insert(new Block(value), x, y);
        }
    }

}
