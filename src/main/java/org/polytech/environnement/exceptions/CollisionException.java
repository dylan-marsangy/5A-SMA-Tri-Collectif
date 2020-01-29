package org.polytech.environnement.exceptions;

public class CollisionException extends RuntimeException {

    public CollisionException() { }

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