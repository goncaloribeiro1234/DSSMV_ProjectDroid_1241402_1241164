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
                            Game g = new Game(null, raw.getId(), raw.getName(), raw.getBackgroundImage(), 0.0, 0, false, 0.0);
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

    // Fallback inteligente: força o jogo pesquisado a ficar sempre em 1º lugar (index 0) para o Glide mostrar a capa certa
    private void loadMockFallback(String query) {
        try {
            List<Game> mockList = MockData.getMockGames();
            if (mockList != null && !mockList.isEmpty()) {
                List<Game> exactMatches = new ArrayList<>();
                List<Game> others = new ArrayList<>();

                for (Game g : mockList) {
                    if (g.getTitle() != null && g.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        exactMatches.add(g);
                    } else {
                        others.add(g);
                    }
                }

                List<Game> finalMockList = new ArrayList<>();
                finalMockList.addAll(exactMatches); // Os correspondentes ficam no topo
                finalMockList.addAll(others);      // Os restantes logo a seguir

                if (!finalMockList.isEmpty()) {
                    searchResults.postValue(DataState.success(finalMockList));
                } else {
                    searchResults.postValue(DataState.success(mockList));
                }
                return;
            }
        } catch (Exception e) {
            // Ignora
        }
        searchResults.postValue(DataState.error("Erro ao carregar dados locais."));
    }
}