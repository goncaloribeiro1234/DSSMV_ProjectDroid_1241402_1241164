package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.util.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    protected abstract void onSuccess(T body);
    protected abstract void onError(Exception error);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onError(ErrorUtils.fromCode(response.code()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(ErrorUtils.fromThrowable(t));
    }
}