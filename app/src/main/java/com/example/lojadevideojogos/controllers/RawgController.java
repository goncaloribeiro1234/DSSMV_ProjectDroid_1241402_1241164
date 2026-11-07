package com.example.lojadevideojogos.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lojadevideojogos.mock.MockData;
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
                        loadMockFallback(query);
                    } else {
                        List<Game> convertedGames = new ArrayList<>();
                        for (RawgGame raw : rawResults) {
                            String img = raw.getBackgroundImage();
                            if (img == null || img.isEmpty()) {
                                img = "https://upload.wikimedia.org/wikipedia/commons/6/65/No-image-available.svg";
                            }
                            Game g = new Game(null, raw.getId(), raw.getName(), img, 0.0, 0, false, 0.0);
                            convertedGames.add(g);
                        }
                        searchResults.setValue(DataState.success(convertedGames));
                    }
                } else {
                    loadMockFallback(query);
                }
            }

            @Override
            public void onFailure(Call<RawgSearchResponse> call, Throwable t) {
                loadMockFallback(query);
            }
        });
    }

    private void loadMockFallback(String query) {
        try {
            List<Game> mockList = MockData.getMockGames();
            if (mockList != null && !mockList.isEmpty()) {
                List<Game> filtered = new ArrayList<>();
                String q = query != null ? query.toLowerCase().trim() : "";

                // Filtra estritamente para incluir apenas o jogo pesquisado
                for (Game g : mockList) {
                    if (g.getTitle() != null && g.getTitle().toLowerCase().contains(q)) {
                        filtered.add(g);
                    }
                }

                if (!filtered.isEmpty()) {
                    searchResults.postValue(DataState.success(filtered));
                } else {
                    // Se não encontrar correspondência, devolve vazio em vez de mostrar o catálogo todo com o GTA V
                    searchResults.postValue(DataState.empty());
                }
                return;
            }
        } catch (Exception e) {
            // Ignora
        }
        searchResults.postValue(DataState.error("Erro ao carregar dados locais."));
    }
}