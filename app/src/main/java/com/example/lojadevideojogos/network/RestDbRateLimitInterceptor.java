package com.example.lojadevideojogos.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RestDbRateLimitInterceptor implements Interceptor {

    private static final long MIN_INTERVAL_MS = 1100;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_BASE_DELAY_MS = 1500;

    private static final Object LOCK = new Object();
    private static long lastRequestTime = 0;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        waitForSlot();
        Response response = chain.proceed(request);

        int attempt = 0;
        while (response.code() == 429 && attempt < MAX_RETRIES) {
            response.close();
            attempt++;
            sleep(RETRY_BASE_DELAY_MS * attempt);
            waitForSlot();
            response = chain.proceed(request);
        }
        return response;
    }

    private void waitForSlot() throws IOException {
        synchronized (LOCK) {
            long wait = MIN_INTERVAL_MS - (System.currentTimeMillis() - lastRequestTime);
            if (wait > 0) sleep(wait);
            lastRequestTime = System.currentTimeMillis();
        }
    }

    private void sleep(long ms) throws IOException {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Pedido interrompido");
        }
    }
}