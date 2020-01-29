package org.polytech.environnement.exceptions;

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