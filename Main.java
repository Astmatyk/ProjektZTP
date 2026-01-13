import gui.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainGUI(); // tworzy okno, menu, panele itd.
        });
    }
}
