package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestDbClient {
    // ATENÇÃO: Substituir pelo vosso URL do RestDB (tem de acabar com a barra '/')
    private static final String BASE_URL = "https://A_TUA_BASE_AQUI.restdb.io/rest/";
    private static Retrofit retrofit = null;

    public static RestDbApi getApi() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // O RestDB exige a chave secreta e o formato JSON nos Headers
                        Request request = original.newBuilder()
                                .header("x-apikey", BuildConfig.RESTDB_API_KEY)
                                .header("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RestDbApi.class);
    }
}