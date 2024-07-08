package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class NetworkUtil {
    private static final String BASE_URL = "http://localhost:3000";

    public static String sendRequest(HttpUriRequest request) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }
    }

    public static String doPost(String endpoint, String jsonBody) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + endpoint);
        StringEntity entity = new StringEntity(jsonBody);
        entity.setContentType("application/json");
        request.setEntity(entity);
        return sendRequest(request);
    }

    public static String doGet(String endpoint) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + endpoint);
        request.addHeader("Content-Type", "application/json");
        return sendRequest(request);
    }

    public static String doDelete(String endpoint) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + endpoint);
        return sendRequest(request);
    }
}
