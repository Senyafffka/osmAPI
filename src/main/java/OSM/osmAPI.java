package OSM;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.ArrayList;


public class osmAPI {
    private final String pathToOverPassAPI = "https://overpass-api.de/api/interpreter?data=";
    private final String pathToNominatim = "";
    private final Gson gson = new Gson();

    private String RequestOverPass(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "http://overpass-api.de/api/interpreter?data=" + encodedQuery;

        HttpURLConnection conn = null;
        StringBuilder content = new StringBuilder();
        BufferedReader in = null;
        URL url = null;

        //Выполнение запроса
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return content.toString();
    }
    public osmEntity GetOSMEntityById(String type, int id){
        if (!type.equals("node") && !type.equals("relation")  && !type.equals("way")){
            throw new IllegalArgumentException("Несуществующий тип");
        }
        //Шаблон запроса
        String query =  "[out:json];" +
                type + "(" + id + ");" +
                "out body;";

        String result = RequestOverPass(query);
        //Парсинг ответа
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonArray elementsArray = jsonObject.getAsJsonArray("elements");
        jsonObject = elementsArray.get(0).getAsJsonObject();
        switch (type){
            case "node":
                return gson.fromJson(jsonObject, Node.class);
            case "relation":
                return gson.fromJson(jsonObject, Relation.class);
            case "way":
                return gson.fromJson(jsonObject, Way.class);
        }

        return null;
    }

    public ArrayList<Node> institutionOnStreet(String city, String street, Map<String, String> amenity) {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("nwr[\"addr:street\"=\"").append(street).append("\"][\"addr:city\"=\"").append(city).append("\"];");

        queryBuilder.append("nwr[");
        for (Map.Entry<String, String> entry : amenity.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("][");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append("]");

        queryBuilder.append("(around:10); out geom;");
        String query = queryBuilder.toString();
        System.out.println(query);

        String result = RequestOverPass(query);
        //Парсинг ответа
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonArray elements = jsonObject.getAsJsonArray("elements");

        ArrayList<Node> nodes = new ArrayList<Node>();

        for (JsonElement element : elements) {
            JsonObject nodeObject = element.getAsJsonObject();
            if ("node".equals(nodeObject.get("type").getAsString())) {
                Node node = gson.fromJson(nodeObject, Node.class);
                nodes.add(node);
            }
        }

        return nodes;
    }
}