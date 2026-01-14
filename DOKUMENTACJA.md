## BUILDER

**Cel użycia**  
Wzorzec Builder został użyty do tworzenia obiektu Game w różnych konfiguracjach (PvP, PvE, EvE) bez konieczności stosowania wielu przeciążonych konstruktorów. Pozwala to oddzielić proces inicjalizacji gry od samego obiektu Game.

**Role wzorca w projekcie**  
Builder: GameBuilder  
Concrete Builder: GameBuilderPvP, GameBuilderPvE, GameBuilderEvE  
Product: Game  
Client: kod inicjalizujący grę (np. main lub kontroler aplikacji)

**Lokalizacja w kodzie**  
Definicja wzorca: gamelogic/GameBuilder.java  
Implementacje: gamelogic/GameBuilderPvP.java, GameBuilderPvE.java, GameBuilderEvE.java  
Produkt: gamelogic/Game.java  

**Użycie**  
Obiekt gry tworzony jest poprzez wywołanie kolejnych metod budowy (buildBoard, buildPlayers), a następnie metody getResult(). Wariant getResult(String gameId) umożliwia odtworzenie gry z zapisu.  

**Wektor zmian**  
Zastosowanie wzorca Builder umożliwia łatwe dodanie nowych trybów gry lub zmianę procesu inicjalizacji bez modyfikowania klasy Game.  
