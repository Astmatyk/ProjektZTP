## BUILDER

**Cel użycia**  
Wzorzec Builder został użyty do tworzenia obiektu Game w różnych konfiguracjach (PvP, PvE, EvE) bez konieczności stosowania wielu przeciążonych konstruktorów. Pozwala to oddzielić proces inicjalizacji gry od samego obiektu Game.

**Role wzorca w projekcie**  
Builder: GameBuilder  
Concrete Builder: GameBuilderPvP, GameBuilderPvE, GameBuilderEvE  
Product: Game  
Client: kod inicjalizujący grę

**Lokalizacja w kodzie**  
Definicja wzorca: gamelogic/GameBuilder.java  
Implementacje: gamelogic/GameBuilderPvP.java, GameBuilderPvE.java, GameBuilderEvE.java  
Produkt: gamelogic/Game.java  

**Użycie**  
Obiekt gry tworzony jest poprzez wywołanie kolejnych metod budowy (buildBoard, buildPlayers), a następnie metody getResult().  

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


## STRATEGY

**Cel użycia**  
Wzorzec Strategy został użyty do oddzielenia logiki wybory strzału od klasy gracza. 
Dzięki temu różne poziomy trudności bota (Easy, Normal, Hard) mogą używać zupełnie innych algorytmów strzelania bez zmiany kodu Playera, Game, ani żadnych innych klas.
Innymi słowy- zmienia zachowanie Playera bez wprowadzania zmian w samym Playerze.

**Role wzorca w projekcie**  
Strategy (interfejs): ShootingStrategy  
Concrete Strategy: EasyMode, NormalMode, HardMode  
Context: PcPLayer  
Client: GameBuilderPvE, GameBuilderEvE

**Lokalizacja w kodzie**  
Definicja wzorca: gamelogic/ShootingStrategy.java  
Implementacje: gamelogic/EasyMode.java, NormalMode.java, HardMode.java  
Context: gamelogic/PcPlayer.java
Client: gamelogic/GameBuilderPvE.java, gamelogic/GameBuilderEvE.java  

**Użycie**  
GameBuilder na podstawie BotDifficulty wybiera odpowiednią strategię, która jest przekazywana do konstruktora PcPlayer.
Podczas tuty gry, PcPlayer.chooseCoordinates() deleguje wybór strzału do obiektu strategii. Strategia analizuje shootingBoard, wybiera koordynaty do zestrzelenia i je zwraca.

**Wektor zmian**  

Dzięki użyciu Strategy można m. in. dodawać nowe poziomy AI bez naruszania Playera czy Game, testować algorytmy strzelania w izolacji, czy też umożliwić zmianę poziomu trudności w trakcie rozgrywki jesli zaszła by na to potrzeba.

## DECORATOR

**Cel użycia**  
Wzorzec Decorator zostanie użyty do dodawania nowych zachowań klasy GamePanel.
Założeniem jest dodanie nowych funkcji do GamePanel tj. klasy reprezentującej rozgrywkę w GUI w celu obsłużenia różnych trybów gry (PvP, PvE, EvE) ponieważ wymagają one innych zachowań.
Innymi słowy- obsługa trybów gry w GUI.

**Role wzorca w projekcie**  
Interfejs obiektu opakowywanego: GamePanelInterface
Podstawowa implementacja obiektu: ConGamePanel
Klasa abstrakcyjna dekoratora: GamePanelDecorator
Konkretne dekoratory: PvPGamePanel, PvEGamePanel, EvEGamePanel

**Lokalizacja w kodzie**  
Interfejs obiektu opakowywanego: gui/gamePanel/GamePanelInterface.java
Podstawowa implementacja obiektu: gui/gamePanel/ConGamePanel.java
Klasa abstrakcyjna dekoratora: gui/gamePanel/GamePanelDecorator.java
Konkretne dekoratory: gui/gamePanel/PvPGamePanel.java, gui/gamePanel/PvEGamePanel.java, gui/gamePanel/EvEGamePanel.java

**Użycie**  
Docelowym zachowaniem jest opakowanie ConGamePanel w odpowiedni dekorator po wybraniu trybu gry w GameConfigPanel

**Wektor zmian**  
Dzięki użyciu Decoratora można oddzielić logikę poszczególnych trybów gry od siebie oraz umożliwć łatwiejsze ich dodawanie w przyszłości.

**UWAGA! Ten wzorzec nie został jeszcze zrealizowany. W kodzie istnieje tylko jego szkielet.**  
