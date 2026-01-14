package gamelogic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Snapshot implements Serializable {
    private Player p1Board;
    private Player p2Board;
    private Player currentPlayer;
    private ShotResult lastResult;
    private long timestamp;

    public Snapshot(Player p1Board, Player p2Board, Player currentPlayer, ShotResult lastResult) {
        // NIE przypisujemy tylko kopiujemy stan graczy w tym momencie w czasie
        this.p1Board = deepCopy(p1Board);
        this.p2Board = deepCopy(p2Board);
        this.currentPlayer = deepCopy(currentPlayer);
        this.lastResult = lastResult;
        this.timestamp = System.currentTimeMillis();
    }

    //masa getterów dla game do konsumpcji
    public Player getP1Board() {
        return p1Board;
    }

    public Player getP2Board() {
        return p2Board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ShotResult getLastResult() {
        return lastResult;
    }

    public long getTimestamp() {
        return timestamp;
    }

    //wykonuje kopię obiektu, serializując go po czym deserializując znowu (u nas to gracz)
    //aby to działało gracz musi implementować serializable.
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T deepCopy(T obj) {
        if (obj == null) return null;
        try {
            //konwertujemy naszego gracza na strumień bajtów
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
            }

            //odczytujemy nasz strumień bajtów do nowego obiektu i go zwracamy
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("deep copy się nie udał: ", e);
        }
    }
}
