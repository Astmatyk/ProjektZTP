package gamelogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

public class GameHistory {
	private final Stack<Snapshot> history = new Stack<>();
	private final String gameId;

	public GameHistory(String gameId) {
		if (gameId == null || gameId.isEmpty()) throw new IllegalArgumentException("wymagane id gry!");
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void push(Snapshot snap) {
		if (snap == null) throw new IllegalArgumentException("snapshot is null");

		history.push(snap);
		saveStack();
	}

	public Snapshot pop() {
		if (history.isEmpty()) return null;
		return history.pop();
	}

	public boolean isEmpty() {
		return history.isEmpty();
	}

	public int size() {
		return history.size();
	}

	private void saveStack() {
		File dir = new File("saves");
		if (!dir.exists()) dir.mkdirs();
		File f = new File(dir, gameId + ".sav");

		//serializujemy nasz stack do pliku
		try (FileOutputStream fos = new FileOutputStream(f);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(history);
		} catch (IOException e) {
			throw new RuntimeException("nie udało się zapisać gry", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static GameHistory loadFromFile(File f) {
		if (f == null || !f.exists()) return null;

		String name = f.getName();
		String suffix = ".sav";

		if (!name.endsWith(suffix)) return null;
		String gameId = name.substring(0, name.length() - suffix.length());

		//odczytujemy obiekt z pliku i zwracamy odczytany stack
		try (FileInputStream fis = new FileInputStream(f);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			Object obj = ois.readObject();
			Stack<Snapshot> stack = (Stack<Snapshot>) obj;
			GameHistory gh = new GameHistory(gameId);
			gh.history.addAll(stack);
			return gh;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("nie udało się odczytać pliku", e);
		}
	}
}
