package com.example.crowdhubharmony.model;

import org.jetbrains.annotations.Nullable;

public final class LoginResponseModel {
    @Nullable
    private  String message;
    @Nullable
    private  Boolean error;
    @Nullable
    private  Boolean success;
    @Nullable
    private  Integer smart_watch;

    @Nullable
    public final String getMessage() {
        return this.message;
    }

    @Nullable
    public final Boolean getError() {
        return this.error;
    }

    @Nullable
    public final Boolean getSuccess() {
        return this.success;
    }

    @Nullable
    public final Integer getSmart_watch() {
        return this.smart_watch;
    }

    public LoginResponseModel(@Nullable String message, @Nullable Boolean error, @Nullable Boolean success, @Nullable Integer smart_watch) {
        this.message = message;
        this.error = error;
        this.success = success;
        this.smart_watch = smart_watch;
    }

}

