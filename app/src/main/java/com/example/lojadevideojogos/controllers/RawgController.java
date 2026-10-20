package com.example.lojadevideojogos.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.model.RawgGame;
import com.example.lojadevideojogos.model.RawgSearchResponse;
import com.example.lojadevideojogos.network.RawgClient;
import com.example.lojadevideojogos.util.DataState;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RawgController {

    // LiveData que as Views vão observar
    private final MutableLiveData<DataState<List<Game>>> searchResults = new MutableLiveData<>();

    public LiveData<DataState<List<Game>>> getSearchResults() {
        return searchResults;
    }

    public void searchGames(String query) {
        searchResults.setValue(DataState.loading());

        RawgClient.getApi().searchGames(query, 10).enqueue(new Callback<RawgSearchResponse>() {
            @Override
            public void onResponse(Call<RawgSearchResponse> call, Response<RawgSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RawgGame> rawResults = response.body().getResults();
                    if (rawResults.isEmpty()) {
                        searchResults.setValue(DataState.empty());
                    } else {
                        // Converter RawgGame para o nosso Game (Phase 2)
                        List<Game> convertedGames = new ArrayList<>();
                        for (RawgGame raw : rawResults) {
                            // Não temos price, stock, nem promos na RAWG. Inicializamos a 0/false.
                            Game g = new Game(null, raw.getId(), raw.getName(), raw.getBackgroundImage(), 0.0, 0, false, 0.0);
                            convertedGames.add(g);
                        }
                        searchResults.setValue(DataState.success(convertedGames));
                    }
                } else if (response.code() == 429) {
                    searchResults.setValue(DataState.error("Limite de pedidos atingido. Tente mais tarde."));
                } else {
                    searchResults.setValue(DataState.error("Erro na API: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<RawgSearchResponse> call, Throwable t) {
                // Timeout ou sem internet
                searchResults.setValue(DataState.error("Falha na ligação: " + t.getMessage()));
            }
        });
    }
}