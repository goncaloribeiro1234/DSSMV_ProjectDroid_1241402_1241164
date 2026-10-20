package com.example.lojadevideojogos.util;

public class DataState<T> {
    public enum Status { LOADING, SUCCESS, EMPTY, ERROR }

    public final Status status;
    public final T data;
    public final String errorMessage;

    private DataState(Status status, T data, String errorMessage) {
        this.status = status;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> DataState<T> loading() { return new DataState<>(Status.LOADING, null, null); }
    public static <T> DataState<T> success(T data) { return new DataState<>(Status.SUCCESS, data, null); }
    public static <T> DataState<T> empty() { return new DataState<>(Status.EMPTY, null, null); }
    public static <T> DataState<T> error(String errorMessage) { return new DataState<>(Status.ERROR, null, errorMessage); }
}