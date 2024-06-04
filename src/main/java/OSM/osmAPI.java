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
import java.util.PropertyPermission;
import java.util.ArrayList;


public class osmAPI {
    private static final String PATH_TO_OVERPASS_API = "https://overpass-api.de/api/interpreter?data=";
    // TODO: Добавить ссылку к Nominatim API (геокодирование)
    //private final String pathToNominatim = "";
    private final Gson gson = new Gson();

    private String RequestOverPass(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = PATH_TO_OVERPASS_API + encodedQuery;

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
    
    private ArrayList<Relation> parseElementsFromResponseRelation(String result) {
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonArray elements = jsonObject.getAsJsonArray("elements");

        ArrayList<Relation> relations = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Way> ways = new ArrayList<>();

        for (JsonElement element : elements) {
            JsonObject elementObject = element.getAsJsonObject();
            String type = elementObject.get("type").getAsString();
            switch (type) {
                case "node":
                    Node node = gson.fromJson(elementObject, Node.class);
                    nodes.add(node);
                    break;
                case "way":
                    Way way = gson.fromJson(elementObject, Way.class);
                    ways.add(way);
                    break;
                case "relation":
                    Relation relation = gson.fromJson(elementObject, Relation.class);
                    relations.add(relation);
                    break;
            }
        }

        // Добавление узлов и путей в отношения
        for (Relation relation : relations) {
            relation.setNodeList(nodes);
            relation.setWayList(ways);
        }

        return relations;
    }
    private <T> ArrayList<T> parseElementsFromResponse (String result, String type, Class<T> clazz) {
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonArray elements = jsonObject.getAsJsonArray("elements");

        ArrayList<T> elementsList = new ArrayList<>();

        for (JsonElement element : elements) {
            JsonObject elementObject = element.getAsJsonObject();
            if (type.equals(elementObject.get("type").getAsString())) {
                T entity = gson.fromJson(elementObject, clazz);
                elementsList.add(entity);
            }
        }

        return elementsList;
    }

    //Получение сущности OSM по ID
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

    // Получение сущности OSM по координатам
    /*
    [out:json];
        node(around:100,50.7,7.1);
    out body;
    */
    public osmEntity GetOSMEntityByCoordinate(String type, Double latitude, Double longitude){
        if (!type.equals("node") && !type.equals("relation")  && !type.equals("way")){
            throw new IllegalArgumentException("Несуществующий тип");
        }
        //Шаблон запроса
        String query =  "[out:json];" +
            type + "(around:100," + latitude + "," + longitude + ");" +
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

    // Получение сущностей OSM (Node) по адресу
    /*
    [out:json];
    node
    ["addr:city"~"D.sseldorf"]
    ["addr:street"~"F.rstenwall"]
    ["addr:housenumber"~"66b"];
    out body;
     */
    public ArrayList<Node> GetOSMEntityByAddress(String city, String street, String housenumber) {
        String query = 
            "[out:json];" +
            "node[\"addr:city\"=\"" + city + "\"][\"addr:street\"=\"" + street + "\"][\\\"addr:housenumber\\\"=\\\"" + housenumber + "\\\"];" +
            "out body;";

        String result = RequestOverPass(query);
        //Парсинг ответа
        return parseElementsFromResponse(result, "node", Node.class);
    }

    // Получение сущности OSM по имени
    /*
    [out:json];
        node ["name"~".[Мм]окрушин."];
        (._;>;);
    out body;
    */
    public osmEntity GetOSMEntityByName(String type, String name){
        if (!type.equals("node") && !type.equals("relation")  && !type.equals("way")){
            throw new IllegalArgumentException("Несуществующий тип");
        }
        //Шаблон запроса
        String query =  "[out:json];" +
            type + "[\"name\"~\"." + name + ".\"];" +
            "(._;>;); out body;";

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
    
    // Поиск заведений на улице с фильтрацией по тегам
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
        return parseElementsFromResponse(result, "node", Node.class);
    }
    
    // Поиск остановок маршрута общественного транспорта на улице
    /*
    [out:json];
        nwr["addr:street"="проспект Ленина"]["addr:city"="Томск"];
        nwr[highway=bus_stop](around:100); 
    out geom;
     */
    public ArrayList<Node> publicTransportStopsOnStreet(String city, String street) {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("nwr[\"addr:street\"=\"").append(street).append("\"][\"addr:city\"=\"").append(city).append("\"];");

        queryBuilder.append("nwr[highway=bus_stop](around:100);out geom;");
        String query = queryBuilder.toString();
        System.out.println(query);

        String result = RequestOverPass(query);
        //Парсинг ответа
        return parseElementsFromResponse(result, "node", Node.class);
    }

    // Поиск остановок маршрута общественного транспорта в городе
    /*
    [out:json];
        area
            ["name"="Томск"]->.cityArea;
        relation
            ["ref"="29"](area.cityArea)->.route;
        node
            (r.route)["highway"="bus_stop"];
        (._;>;);
    out body;
     */
    public ArrayList<Node> publicTransportStopsRouteInTheCity(String city, String ref) {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("area[\"name\"=\"").append(city).append("\"]->.cityArea;");
        queryBuilder.append("relation[\"ref\"=\"").append(ref).append("\"](area.cityArea)->.route;");

        queryBuilder.append("node(r.route)[\"highway\"=\"bus_stop\"];(._;>;);out body;");
        String query = queryBuilder.toString();
        System.out.println(query);

        String result = RequestOverPass(query);
        //Парсинг ответа
        return parseElementsFromResponse(result, "node", Node.class);
    }

    // Поиск всех маршрутов общественного транспорта в городе
    /*
    [out:json];
    area
        ["name"="Томск"]->.cityArea;
        relation
        ["ref"~"."](area.cityArea);
        (._;>;);
    out body;
    */
    public ArrayList<Relation> publicTransportRoutesInTheCity(String city) {
        StringBuilder queryBuilder = new StringBuilder("[out:json];");
        queryBuilder.append("area[\"name\"=\"").append(city).append("\"]->.cityArea;");

        queryBuilder.append("relation(area.cityArea)[\"ref\"~\".\"];(._;>;);out body;");
        String query = queryBuilder.toString();
        System.out.println(query);

        String result = RequestOverPass(query);
        //Парсинг ответа
        return parseElementsFromResponseRelation(result);
    }

}
/*

  {
    "type": "node",
    "id": 11941603734,
    "lat": 47.3989530,
    "lon": 40.0963503,
    "tags": {
      "railway": "milestone",
      "railway:position": "1175"
    }
  },
  {
    "type": "node",
    "id": 11944341069,
    "lat": 57.8782119,
    "lon": 83.1798518,
    "tags": {
      "crossing": "uncontrolled",
      "crossing:markings": "yes",
      "highway": "crossing"
    }
  },
  {
    "type": "way",
    "id": 15799866,
    "nodes": [
      673444160,
      1933136587,
      673444150
    ],
    "tags": {
      "access": "yes",
      "bicycle": "yes",
      "cycleway": "no",
      "highway": "primary",
      "lanes": "3",
      "lit": "yes",
      "maxspeed": "RU:urban",
      "motorcycle": "no",
      "name": "проспект Ленина",
      "name:en": "Lenin Avenue",
      "name:etymology:wikidata": "Q1394",
      "name:ru": "проспект Ленина",
      "oneway": "yes",
      "sidewalk": "separate",
      "surface": "asphalt",
      "trolley_wire": "yes",
      "turn:lanes:forward": "left|left|right"
    }
  },
  {
    "type": "way",
    "id": 15799867,
    "nodes": [
      1420633349,
      11127501937,
      11875842133,
      1420631785
    ],
    "tags": {
      "highway": "primary",
      "lanes": "7",
      "lanes:backward": "4",
      "lanes:forward": "3",
      "lit": "yes",
      "maxspeed": "40",
      "name": "Комсомольский проспект",
      "name:en": "Komsomolsky Avenue",
      "name:eo": "Komsomolo avenuo",
      "name:ru": "Комсомольский проспект",
      "name:zh": "共青团大街",
      "oneway": "no",
      "sidewalk": "separate",
      "source:maxspeed": "sign",
      "surface": "asphalt",
      "trolley_wire": "yes",
      "turn:lanes:backward": "through|through|through|slight_right"
    }
  },

*/