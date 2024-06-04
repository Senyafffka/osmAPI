# **OsmAPI**
Библиотека реализует ряд функций для извлечения данных об объектах окружающего мира из открытых географических карт OpenStreetMap. 

## **Краткое описание методов:**
+ ### getNodesByIds(List<Long> ids)
> Возвращает список точек (Node) по ID объекта 

+ ### getElementById(String type, long id) 
> Возвращает элемент (сущность osm) по типу сущности и ID

+ ### GetOSMEntityByCoordinate(String type, Double latitude, Double longitude)
> Возвращает элемент (сущность osm) по переданным координатам и типу (сущность osm)
  
+ ### GetOSMEntityByName(String type, String name)
> Возвращает список элементов (сущности osm) по типу и имени

+ ### institutionOnStreet(String city, String street, Map<String, String> amenity)
> Возвращает список точек (Node) по улицу и тегам (встроенные)

+ ### publicTransportStopsOnStreet(String city, String street)
> Возвращает список точек по улице (транспортные остановки на улице)

+ ### publicTransportStopsRouteInTheCity(String city, int routeNumber)
> Возвращает список точек остановок по городу конкретного номера автобуса 

+ ### publicTransportRoutesInTheCity(String city)
> Возвращает все номера маршрутов в городе 

+ ### directGeocoding(String addr)
> Прямое геокодирование возаращает координаты по переданному адресу 

+ ### reverseGeocoding(double lat, double lon)
> Обратное геокодирование возвращает адрес по переанным координатам 
