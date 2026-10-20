package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.model.RawgGame;
import com.example.lojadevideojogos.model.RawgSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RawgApi {
    @GET("games")
    Call<RawgSearchResponse> searchGames(@Query("search") String query, @Query("page_size") int pageSize);

    @GET("games/{id}")
    Call<RawgGame> getGameDetails(@Path("id") int id);
}