package org.example;
import java.lang.reflect.Array;
import java.util.HashMap;
import OSM.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        osmAPI api = new osmAPI();
        HashMap<String, String> amenity = new HashMap<String, String>();

        amenity.put("amenity", "cafe");
        amenity.put("name", "\"БлинБери\"");
        var a = api.institutionOnStreet("Москва", "Тверская улица", amenity);
        }
    }