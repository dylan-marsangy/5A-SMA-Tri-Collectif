package org.polytech.environnement.exceptions;

/**
 * Exception renvoyée lorsqu'une collision se produit entre deux entités sur la grille.
 */
public class CollisionException extends RuntimeException {

    public CollisionException() {
    }

    public CollisionException(Throwable e) {
        super(e);
    }

    public CollisionException(String cause) {
        super(cause);
    }

    public CollisionException(String cause, Throwable e) {
        super(cause, e);
    }

}