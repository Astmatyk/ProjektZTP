package gamelogic;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class AudioManager {
    private Clip clip;
    private boolean isMuted = false;

    /**
     * Odtwarza plik audio z katalogu resources/
     * @param filename nazwa pliku (np. "music.mp3")
     */
    public void playAudio(String filename) {
        if (isMuted) return;

        try {
            // Ścieżka do pliku w katalogu resources
            String resourcePath = "resources" + File.separator + filename;
            File audioFile = new File(resourcePath);

            if (!audioFile.exists()) {
                System.err.println("Plik audio nie znaleziony: " + resourcePath);
                return;
            }

            // Stopujemy poprzedni odtwarzany utwór (jeśli istnieje)
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            // Otwieramy plik audio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Nieobsługiwany format pliku audio: " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku audio: " + filename);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Linia audio niedostępna");
            e.printStackTrace();
        }
    }

    /**
     * Odtwarza plik audio w pętli
     * @param filename nazwa pliku (np. "music.mp3")
     */
    public void playAudioLoop(String filename) {
        playAudio(filename);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Zatrzymuje odtwarzanie
     */
    public void stopAudio() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Wycisza/włącza dźwięk
     */
    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted && clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    /**
     * Zwraca stan wyciszenia
     */
    public boolean isMuted() {
        return isMuted;
    }
}
