package com.example.nfcapp.api;

import android.util.Base64;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Provides a singleton Retrofit client for network operations. This class configures the Retrofit
 * instance with a base URL, an OkHttpClient for HTTP calls, and adds support for basic authentication.
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://97.106.165.83:8080/";
    private static Retrofit retrofit = null;

    /**
     * Creates an Interceptor that adds a Basic Authentication header and a header to accept JSON responses.
     *
     * @param username The username for Basic Authentication.
     * @param password The password for Basic Authentication.
     * @return An Interceptor that adds authentication and accept headers to requests.
     */
    private static Interceptor createBasicAuthInterceptor(final String username, final String password) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String credentials = username + ":" + password;
                final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("Authorization", basic).header("Accept", "application/json")  // Explicitly accept JSON
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * Returns an instance of the ApiService, creating it if it has not been instantiated yet.
     *
     * @return A singleton instance of ApiService configured with a Retrofit client.
     */
    public static ApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(createBasicAuthInterceptor("user", "pass")).build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ApiService.class);
    }
}
