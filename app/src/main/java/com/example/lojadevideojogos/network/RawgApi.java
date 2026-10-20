package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.model.RawgGame;
import com.example.lojadevideojogos.model.RawgSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RawgApi {
    // Pesquisar jogos por nome
    @GET("games")
    Call<RawgSearchResponse> searchGames(@Query("search") String query, @Query("page_size") int pageSize);

    // Obter os detalhes de um jogo específico pelo ID
    @GET("games/{id}")
    Call<RawgGame> getGameDetails(@Path("id") int id);
}