package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RawgClient {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static Retrofit retrofit = null;

    public static RawgApi getApi() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        HttpUrl originalUrl = originalRequest.url();

                        HttpUrl urlComChave = originalUrl.newBuilder()
                                .addQueryParameter("key", BuildConfig.RAWG_API_KEY)
                                .build();

                        Request novoRequest = originalRequest.newBuilder()
                                .url(urlComChave)
                                .build();

                        return chain.proceed(novoRequest);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RawgApi.class);
    }
}