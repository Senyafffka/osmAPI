package OSM;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import Enum.OsmEntityType;

public class OsmService {

    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter?data=";
    private static final Gson GSON = new Gson();

    private OkHttpClient httpClient = new OkHttpClient();

    // Запрос к Overpass API
    private String RequestToOverpassApi(String query){
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = OVERPASS_URL + encodedQuery;
        String responseContent = null;

        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new IOException("Запрос к API не был успешен: " +
                        response.code());
            }
            responseContent = response.body().string();
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
        }

        return responseContent;
    }

    public osmEntity GetOsmEntityById(String type, long id){
        CheckType(type);

        String query =  "[out:json];" +
                type + "(" + id + ");" +
                "out body;";

        String result = RequestToOverpassApi(query);
        System.out.println(result);

        return null;
    }

    private void CheckType(String type){
        if (!type.equalsIgnoreCase(OsmEntityType.NODE.toString())
            && !type.equalsIgnoreCase(OsmEntityType.WAY.toString())
            && !type.equalsIgnoreCase(OsmEntityType.RELATION.toString())){
            throw new IllegalArgumentException("Несуществующий тип");
        }
    }
}
