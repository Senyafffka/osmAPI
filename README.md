# **OsmAPI**
Библиотека реализует ряд функций для извлечения данных об объектах окружающего мира из открытых географических карт OpenStreetMap. 

Реализация методов представлена в папке `service`.

Все сущности исследуемых объектов osm реализованы классами в папке `model`:
+ Address
+ Coordinates
+ Element
+ Node
+ Relation
+ Way

> [!NOTE]
> Именно эти сущности будут использоваться в качестве входных данных или возвращаемых значений.

## **Краткое описание методов:**
### getNodesByIds(List<Long> ids)
> Возвращает список точек `Node` по ID объекта.

### getElementById(String type, long id) 
> Возвращает элемент (сущность osm) по типу сущности и ID

### GetOSMEntityByCoordinate(String type, Double latitude, Double longitude)
> Возвращает элемент соответсвующий тому типу сущности osm, который указывался в запросе вместе с координатами `Coordinates`.
  
### GetOSMEntityByName(String type, String name)
> Возвращает список элементов, представляющих собой сущности osm по типу и имени.

### institutionOnStreet(String city, String street, Map<String, String> amenity)
> Возвращает список точек `Node` по встроенным тегам в заданном городе, на определенной улице.

### publicTransportStopsOnStreet(String city, String street)
> Возвращает список точек `Node`, представляющих собой транспортные остановки в выбранном городе, на заданной улице.

### publicTransportStopsRouteInTheCity(String city, int routeNumber)
> Возвращает список точек `Node`, представляющих собой транспортные остановки заданного маршрута автобуса (номер маршрута) в заданном городе.

### publicTransportRoutesInTheCity(String city)
> Возвращает все номера маршрутов автобусов в заданном городе.

### directGeocoding(String addr)
> Прямое геокодирование возаращает координаты `Coordinates` по переданному адресу `Address`.

### reverseGeocoding(double lat, double lon)
> Обратное геокодирование возвращает адрес `Address` по переанным координатам `Coordinates`.
