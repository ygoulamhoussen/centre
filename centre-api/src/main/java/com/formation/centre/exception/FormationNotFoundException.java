package com.formation.centre.exception;

public class FormationNotFoundException extends RuntimeException {
    public FormationNotFoundException(String message) {
        super(message);
    }
}
