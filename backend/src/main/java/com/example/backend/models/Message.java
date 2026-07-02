package com.example.backend.models;

public class Message {
    private String message;
    private boolean success;

    public Message() {}

    public Message(String message) {
        this.message = message;
        this.success = true;
    }

     public Message(boolean s, String message) {
        this.message = message;
        this.success = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    
}
