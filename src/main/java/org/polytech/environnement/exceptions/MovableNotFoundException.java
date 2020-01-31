package org.polytech.environnement.exceptions;

/**
 * Exception renvoyée lorsque l'environnement tente de manipuler une entité qui n'existe pas sur la grille.
 */
public class MovableNotFoundException extends RuntimeException {

    public MovableNotFoundException() { }

    public MovableNotFoundException(Throwable e) {
        super(e);
    }

    public MovableNotFoundException(String cause) {
        super(cause);
    }

    public MovableNotFoundException(String cause, Throwable e) {
        super(cause, e);
    }

}