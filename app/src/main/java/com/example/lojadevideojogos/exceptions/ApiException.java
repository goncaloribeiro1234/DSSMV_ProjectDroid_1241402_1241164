package com.example.lojadevideojogos.exceptions;

public class ApiException extends Exception {
    private int statusCode;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() { return statusCode; }
}