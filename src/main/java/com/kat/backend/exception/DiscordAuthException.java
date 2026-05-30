package com.kat.backend.exception;

public class DiscordAuthException extends RuntimeException {

    public DiscordAuthException(String message) {
        super(message);
    }

    public DiscordAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}