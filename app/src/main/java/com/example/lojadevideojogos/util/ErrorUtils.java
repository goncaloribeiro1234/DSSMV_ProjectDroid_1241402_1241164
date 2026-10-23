package com.example.lojadevideojogos.util;

import com.example.lojadevideojogos.exceptions.ApiException;
import com.example.lojadevideojogos.exceptions.GameNotFoundException;
import com.example.lojadevideojogos.exceptions.NoInternetException;
import com.example.lojadevideojogos.exceptions.RateLimitException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ErrorUtils {
    public static Exception fromCode(int code) {
        switch (code) {
            case 429:
                return new RateLimitException("Demasiados pedidos ao servidor. Aguarda uns segundos e tenta outra vez.");
            case 401:
            case 403:
                return new ApiException(code, "Acesso recusado. Verifica a chave da API.");
            case 404:
                return new GameNotFoundException("O registo pedido não foi encontrado.");
            default:
                return new ApiException(code, "Erro do servidor (código " + code + ").");
        }
    }

    public static Exception fromThrowable(Throwable t) {
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return new NoInternetException("Sem ligação à internet. Verifica a rede e tenta outra vez.");
        }
        if (t instanceof SocketTimeoutException) {
            return new ApiException(0, "O servidor demorou demasiado a responder.");
        }
        return new ApiException(0, "Erro inesperado: " + t.getMessage());
    }
}