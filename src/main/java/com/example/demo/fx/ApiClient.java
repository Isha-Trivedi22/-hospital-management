package com.example.demo.fx;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    private static final String BASE_URL = "https://hospital-management-uaob.onrender.com/api";
    
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
            
    private static final MediaType JSON = MediaType.get("application/json");

    public static String get(String endpoint) {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            System.out.println("GET error: " + e.getMessage());
            return "[]";
        }
    }

    public static String post(String endpoint, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            System.out.println("POST error: " + e.getMessage());
            return "{}";
        }
    }

    public static String delete(String endpoint) {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            System.out.println("DELETE error: " + e.getMessage());
            return "{}";
        }
    }
}