package OSM;

public class Node extends osmEntity {
    Double lat;
    Double lon;

    public Node(Double lat, Double lon, int id){
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }
}
