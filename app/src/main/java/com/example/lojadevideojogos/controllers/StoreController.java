package com.example.lojadevideojogos.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.network.ApiCallback;
import com.example.lojadevideojogos.network.RestDbClient;
import com.example.lojadevideojogos.util.DataState;

import java.util.List;

import okhttp3.ResponseBody;

public class StoreController {

    private final MutableLiveData<DataState<List<Game>>> games = new MutableLiveData<>();
    private final MutableLiveData<DataState<Game>> actionState = new MutableLiveData<>();

    public LiveData<DataState<List<Game>>> getGames() { return games; }
    public LiveData<DataState<Game>> getActionState() { return actionState; }

    public void loadGames() {
        games.setValue(DataState.loading());
        RestDbClient.getApi().getGames().enqueue(new ApiCallback<List<Game>>() {
            @Override
            protected void onSuccess(List<Game> body) {
                if (body == null || body.isEmpty()) games.setValue(DataState.empty());
                else games.setValue(DataState.success(body));
            }
            @Override
            protected void onError(Exception error) {
                games.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    public void addGame(Game game) {
        actionState.setValue(DataState.loading());
        RestDbClient.getApi().createGame(copyWithoutId(game)).enqueue(new ApiCallback<Game>() {
            @Override
            protected void onSuccess(Game body) {
                actionState.setValue(DataState.success(body));
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    public void updateGame(Game game) {
        if (game.getId() == null) {
            actionState.setValue(DataState.error("Jogo sem identificador."));
            return;
        }
        actionState.setValue(DataState.loading());
        RestDbClient.getApi().updateGame(game.getId(), copyWithoutId(game)).enqueue(new ApiCallback<Game>() {
            @Override
            protected void onSuccess(Game body) {
                actionState.setValue(DataState.success(body != null ? body : game));
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    public void deleteGame(String id) {
        if (id == null) {
            actionState.setValue(DataState.error("Jogo sem identificador."));
            return;
        }
        actionState.setValue(DataState.loading());
        RestDbClient.getApi().deleteGame(id).enqueue(new ApiCallback<ResponseBody>() {
            @Override
            protected void onSuccess(ResponseBody body) {
                if (body != null) body.close();
                actionState.setValue(DataState.success(null));
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    private Game copyWithoutId(Game g) {
        return new Game(null, g.getRawgId(), g.getTitle(), g.getImageUrl(),
                g.getPrice(), g.getStock(), g.isOnSale(), g.getPromoPrice());
    }
}