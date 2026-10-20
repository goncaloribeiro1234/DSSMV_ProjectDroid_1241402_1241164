package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.model.Game;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestDbApi {
    // GET /games -> Vai buscar a lista de todos os jogos na nossa loja
    @GET("games")
    Call<List<Game>> getStoreGames();

    // POST /games -> Adiciona um jogo novo à base de dados
    @POST("games")
    Call<Game> addGameToStore(@Body Game game);
}