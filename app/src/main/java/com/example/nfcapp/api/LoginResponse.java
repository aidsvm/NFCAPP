package com.example.nfcapp.api;

/**
 * Converts API login responses to JSON format.
 */
public class LoginResponse {
    private boolean success;
    private String message;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

