package com.example.lojadevideojogos.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.model.GameRequest;
import com.example.lojadevideojogos.model.RequestStatus;
import com.example.lojadevideojogos.network.ApiCallback;
import com.example.lojadevideojogos.network.RestDbClient;
import com.example.lojadevideojogos.util.DataState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestController {

    private final MutableLiveData<DataState<List<GameRequest>>> pendingRequests = new MutableLiveData<>();
    private final MutableLiveData<DataState<GameRequest>> actionState = new MutableLiveData<>();

    public LiveData<DataState<List<GameRequest>>> getPendingRequests() { return pendingRequests; }
    public LiveData<DataState<GameRequest>> getActionState() { return actionState; }

    public void loadPendingRequests() {
        pendingRequests.setValue(DataState.loading());
        RestDbClient.getApi().getRequests("{\"status\":\"PENDING\"}")
                .enqueue(new ApiCallback<List<GameRequest>>() {
                    @Override
                    protected void onSuccess(List<GameRequest> body) {
                        if (body == null || body.isEmpty()) pendingRequests.setValue(DataState.empty());
                        else pendingRequests.setValue(DataState.success(body));
                    }
                    @Override
                    protected void onError(Exception error) {
                        pendingRequests.setValue(DataState.error(error.getMessage()));
                    }
                });
    }

    public void createRequest(int rawgId, String title, String imageUrl) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        GameRequest request = new GameRequest(null, rawgId, title, imageUrl, RequestStatus.PENDING, today);

        actionState.setValue(DataState.loading());
        RestDbClient.getApi().createRequest(request).enqueue(new ApiCallback<GameRequest>() {
            @Override
            protected void onSuccess(GameRequest body) {
                actionState.setValue(DataState.success(body));
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    public void rejectRequest(GameRequest request) {
        updateStatus(request, RequestStatus.REJECTED);
    }

    public void approveRequest(GameRequest request, double price, int stock) {
        actionState.setValue(DataState.loading());
        Game newGame = new Game(null, request.getRawgId(), request.getTitle(),
                request.getImageUrl(), price, stock, false, null);

        RestDbClient.getApi().createGame(newGame).enqueue(new ApiCallback<Game>() {
            @Override
            protected void onSuccess(Game body) {
                updateStatus(request, RequestStatus.APPROVED); // passo 2
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }

    private void updateStatus(GameRequest request, RequestStatus newStatus) {
        if (request.getId() == null) {
            actionState.setValue(DataState.error("Pedido sem identificador."));
            return;
        }
        actionState.setValue(DataState.loading());
        GameRequest body = new GameRequest(null, request.getRawgId(), request.getTitle(),
                request.getImageUrl(), newStatus, request.getRequestedDate());

        RestDbClient.getApi().updateRequest(request.getId(), body).enqueue(new ApiCallback<GameRequest>() {
            @Override
            protected void onSuccess(GameRequest response) {
                request.setStatus(newStatus);
                actionState.setValue(DataState.success(request));
            }
            @Override
            protected void onError(Exception error) {
                actionState.setValue(DataState.error(error.getMessage()));
            }
        });
    }
}