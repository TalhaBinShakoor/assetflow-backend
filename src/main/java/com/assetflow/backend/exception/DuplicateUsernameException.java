package com.assetflow.backend.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
}