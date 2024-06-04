package api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class OverpassApiClient {
    private static final String OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";
    private OkHttpClient client;

    public OverpassApiClient() {
        this.client = new OkHttpClient();
    }

    public String sendQuery(String query) throws IOException {
        Request request = new Request.Builder()
                .url(OVERPASS_API_URL + "?data=" + query)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
