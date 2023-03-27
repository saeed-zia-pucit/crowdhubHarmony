package com.example.crowdhubharmony.model;

public class CheckinResponseModel {
    private final Boolean error;
    private final String message;
    private final Boolean success;

    public CheckinResponseModel(Boolean error, String message, Boolean success) {
        this.error = error;
        this.message = message;
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}

