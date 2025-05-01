package com.example.demowithtests.util.exception;

public class FieldMissingException extends RuntimeException {
    public FieldMissingException(String message) {
        super(message);
    }
}