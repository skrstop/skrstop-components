package dev.morphia;


public class MissingIdException extends RuntimeException {
    MissingIdException() {
        super("The entity to be replaced has no ID. Please insert this entity first.");
    }
}
