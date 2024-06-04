package mapper;

import dto.*;
import model.*;
import java.util.List;

public class ElementMapper {
    public static Node map(NodeDto nodeDto) {
        return new Node(nodeDto.getId(), nodeDto.getTags(), nodeDto.getLat(), nodeDto.getLon());
    }

    public static Way map(WayDto wayDto, List<Node> nodes) {
        return new Way(wayDto.getId(), wayDto.getTags(), nodes);
    }

    public static Relation map(RelationDto relationDto, List<Element> members) {
        return new Relation(relationDto.getId(), relationDto.getTags(), members);
    }
}
