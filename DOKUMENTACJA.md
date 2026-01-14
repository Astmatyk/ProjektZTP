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

## MEMENTO

**Cel użycia**  
Wzorca memento użyto do funkcjonalności zapisywania oraz odtwarzania gier. Implementacja wzorca sprawia, że klasa Game (originator) nie musi zarządzać historią swojego stanu. Może zapisać swój aktualny stan w dowolnym momencie, a także pobierać snapshoty z historii zaimplementowanej w osobnej klasie i na ich podstawie odtwarzać swój stan.

**Role wzorca w projekcie**  
Originator: Game  
Memento: Snapshot  
Caretaker: GameHistory  

**Lokalizacja w kodzie**  
gamelogic/Game.java, gamelogic/Snapshot.java, gamelogic/GameHistory.java

**Użycie**  
Po każdym strzale originator wywołuje funkcję save(), która tworzy snapshot zawierający kopię stanu gry w danym momencie. Klasa GameHistory zajmuje się zarządzaniem kolekcją tych snapshotów oraz serializacją danych do pliku. Stack ze snapshotami może być ładowany z pliku w celu odtworzenia dawnej rozgrywki.

**Wektor zmian**  
Użycie wzorca zwiększa modularność projektu i upraszcza działanie kluczowej klasy Game, oddelegowując zadania związane z zarządzaniem historią rozgrywki do osobnych klas.
