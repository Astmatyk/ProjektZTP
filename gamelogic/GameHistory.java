package gamelogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

//TODO: udokumentować jakoś ładnie bo jest padaka
public class GameHistory {
	private final Stack<Snapshot> history = new Stack<>();
	private final String gameId;

	public GameHistory(String gameId) {
		if (gameId == null || gameId.isEmpty()) throw new IllegalArgumentException("gameId is required");
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void push(Snapshot snap) {
		if (snap == null) throw new IllegalArgumentException("snapshot is null");

		// persist individual snapshot (optional)
		File dir = new File("snapshots");
		if (!dir.exists()) dir.mkdirs();

		File out = new File(dir, "snapshot_" + snap.getTimestamp() + ".ser");
		try (FileOutputStream fos = new FileOutputStream(out);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(snap);
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist snapshot", e);
		}

		history.push(snap);

		// persist entire history stack for this game
		saveStackToDisk();
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

	private File historyFile() {
		File dir = new File("histories");
		if (!dir.exists()) dir.mkdirs();
		return new File(dir, "history_" + gameId + ".ser");
	}

	private void saveStackToDisk() {
		File f = historyFile();
		try (FileOutputStream fos = new FileOutputStream(f);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(history);
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist history stack", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static GameHistory loadFromFile(File f) {
		if (f == null || !f.exists()) return null;
		// parse gameId from filename history_<id>.ser
		String name = f.getName();
		if (!name.startsWith("history_") || !name.endsWith(".ser")) return null;
		String gameId = name.substring("history_".length(), name.length() - ".ser".length());

		try (FileInputStream fis = new FileInputStream(f);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			Object obj = ois.readObject();
			Stack<Snapshot> stack = (Stack<Snapshot>) obj;
			GameHistory gh = new GameHistory(gameId);
			gh.history.addAll(stack);
			return gh;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to load history from file", e);
		}
	}
}
