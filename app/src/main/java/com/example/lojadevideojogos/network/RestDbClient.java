package com.example.lojadevideojogos.network;

import com.example.lojadevideojogos.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestDbClient {

    private static RestDbApi api = null;

    public static synchronized RestDbApi getApi() {
        if (api == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .header("x-apikey", BuildConfig.RESTDB_API_KEY)
                                .build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(new RestDbRateLimitInterceptor());

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
                logger.setLevel(HttpLoggingInterceptor.Level.BODY);
                logger.redactHeader("x-apikey");
                builder.addInterceptor(logger);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.RESTDB_BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(RestDbApi.class);
        }
        return api;
    }
}