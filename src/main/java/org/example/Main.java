package org.example;

import model.*;
import service.OsmApiService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        OsmApiService apiService = new OsmApiService();

        /*
        try{
            var node = apiService.getElementById("node", 1);
            //List<Long> ids = new ArrayList<>();
            //ids.add(21533912L);
            //ids.add(822403L);
            //ids.add(1L);
            //List<model.Node> nodes = apiService.getNodesByIds(ids);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        /*
        try{
            ArrayList<Node> rel = apiService.getNodesByAddress("Томск", "проспект Ленина", "97");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try{
            model.Element rel = apiService.GetOSMEntityByCoordinate("node", 47.3989530, 40.0963503);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try{
            //ArrayList<model.Element>  rel = apiService.GetOSMEntityByName("node", "ТГУ");
            //ArrayList<model.Element> rel = apiService.GetOSMEntityByName("relation", "Автобус №16/131: Инженерный центр - Малиновка");
            ArrayList<model.Element>  rel = apiService.GetOSMEntityByName("way", "Лагерный сад");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try{
            HashMap<String, String> tags = new HashMap<>();
            tags.put("amenity", "cafe");
            ArrayList<model.Node>  rel = apiService.institutionOnStreet("Москва", "Тверская улица", tags);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        try{
            ArrayList<model.Node>  rel = apiService.publicTransportStopsOnStreet("Томск", "проспект Ленина");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try{
            ArrayList<model.Node>  rel1 = apiService.publicTransportStopsRouteInTheCity("Томск", 4);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try{
            ArrayList<String>  rel1 = apiService.publicTransportRoutesInTheCity("Томск");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        /*
        try{
            ArrayList<Coordinates>  rel1 = apiService.directGeocoding("Томск, проспект Ленина 36");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        try{
            Address  rel1 = apiService.reverseGeocoding(56.4695871, 84.94677153472858);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        /*
        try{
            var ways = apiService.getWaysByStreet("Unter den Linden", "Berlin");
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        try{
            HashMap<String, String> tags = new HashMap<>();
            tags.put("amenity", "cafe");
            var  nodes = apiService.institutionOnCity("Томск", tags);
            System.out.println("Работа закончена");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
