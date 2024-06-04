package OSM;

public class Way extends osmEntity{
    int [] nodes;
    
    public Way(int [] nodes, int id){
        this.nodes = nodes;
        this.id = id;
    }

    public String toString() {
        String result = "Way{ nodes = [\n";
        for (var element : nodes) {
            result += element;
            result += ", ";
        }
        result += "\n] };";
        return result;
    }
}
