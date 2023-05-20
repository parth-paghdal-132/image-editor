package com.editor.image.utils;

public abstract class Result<T> {

    private final T data;
    private final String message;

    public Result(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Success<T> extends Result<T> {
        public Success(T data) {
            super(data, null);
        }
    }

    public static class Error<T> extends Result<T> {
        public Error(String message, T data) {
            super(data, message);
        }
    }

    public static class Loading<T> extends Result<T> {
        public Loading() {
            super(null, null);
        }
    }
}
