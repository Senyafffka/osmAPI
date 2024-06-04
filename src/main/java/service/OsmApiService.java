package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.deserializer.ElementDtoDeserializer;

import api.OverpassApiClient;
import model.*;
import dto.*;
import mapper.ElementMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class OsmApiService {
    private OverpassApiClient client;
    private Gson gson;

    public OsmApiService() {
        this.client = new OverpassApiClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ElementDto.class, new ElementDtoDeserializer())
                .create();
    }

    public List<Node> getNodesByIds(List<Long> ids) throws IOException {
        StringBuilder query = new StringBuilder("[out:json];");
        query.append("node(id:");
        for (int i = 0; i < ids.size(); i++) {
            query.append(ids.get(i));
            if (i < ids.size() - 1) {
                query.append(",");
            }
        }
        query.append(");out body;");

        String jsonResponse = client.sendQuery(query.toString());
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        List<NodeDto> nodeDtos = response.getElements().stream()
                .filter(e -> e instanceof NodeDto)
                .map(e -> (NodeDto) e)
                .toList();

        return nodeDtos.stream()
                .map(ElementMapper::map)
                .collect(Collectors.toList());
    }

    public Way getWayById(long wayId) throws IOException {
        String query = "[out:json];way(" + wayId + ");out body;";
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty() || !(response.getElements().get(0) instanceof WayDto)) {
            return null;
        }

        WayDto wayDto = (WayDto) response.getElements().get(0);
        List<Node> nodes = getNodesByIds(wayDto.getNodes());

        return ElementMapper.map(wayDto, nodes);
    }

    public Relation getRelationById(long relationId) throws IOException {
        String query = "[out:json];relation(" + relationId + ");out body;";
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty() || !(response.getElements().get(0) instanceof RelationDto)) {
            return null;
        }

        RelationDto relationDto = (RelationDto) response.getElements().get(0);
        List<Element> elements = new ArrayList<>();

        for (RelationMemberDto member : relationDto.getMembers()) {
            Element element = getElementById(member.getType(), member.getRef());
            elements.add(element);
        }

        return ElementMapper.map(relationDto, elements);
    }

    // TODO: Проверить Relation
    public Element getElementById(String type, long id) throws IOException {
        String query = "[out:json];" + type + "(" + id + ");out body;";
        String jsonResponse = client.sendQuery(query);
        OverpassResponseDto response = gson.fromJson(jsonResponse, OverpassResponseDto.class);

        if (response.getElements().isEmpty()) {
            return null;
        }

        ElementDto elementDto = response.getElements().get(0);
        if (elementDto instanceof NodeDto) {
            return ElementMapper.map((NodeDto) elementDto);
        } else if (elementDto instanceof WayDto) {
            return getWayById(elementDto.getId());
        } else if (elementDto instanceof RelationDto) {
            return getRelationById(elementDto.getId());
        }
        return null;
    }
}
