package com.example.lojadevideojogos.model;

/**
 * Estados possíveis de um pedido de adição de jogo.
 * O Gson converte o enum para/de texto ("PENDING", "APPROVED", "REJECTED"),
 * que é exatamente o valor guardado no campo "status" do RestDB.
 */
public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED
}