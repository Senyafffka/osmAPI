package OSM;

import java.util.ArrayList;

public class Relation extends osmEntity {
    private ArrayList<Node> nodeList;
    private ArrayList<Way> wayList;

    public Relation(ArrayList<Node> nodes, ArrayList<Way> ways) {
        this.nodeList = nodes;
        this.wayList = ways;
    }

    // Getters and setters
    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public ArrayList<Way> getWayList() {
        return wayList;
    }

    public void setWayList(ArrayList<Way> wayList) {
        this.wayList = wayList;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Relation = {\nnodeList: [\n");
        for (Node element : nodeList) {
            result.append(element.toString()).append("\n");
        }
        result.append("],\nwayList: [\n");
        for (Way element : wayList) {
            result.append(element.toString()).append("\n");
        }
        result.append("\n]\n}");
        return result.toString();
    }
}
