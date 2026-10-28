package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.model.GameRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestDbApi {

    @GET("games")
    Call<List<Game>> getGames();

    @POST("games")
    Call<Game> createGame(@Body Game game);

    @PUT("games/{id}")
    Call<Game> updateGame(@Path("id") String id, @Body Game game);

    @DELETE("games/{id}")
    Call<ResponseBody> deleteGame(@Path("id") String id);

    @GET("requests")
    Call<List<GameRequest>> getRequests(@Query("q") String query);

    @POST("requests")
    Call<GameRequest> createRequest(@Body GameRequest request);

    @PUT("requests/{id}")
    Call<GameRequest> updateRequest(@Path("id") String id, @Body GameRequest request);
}