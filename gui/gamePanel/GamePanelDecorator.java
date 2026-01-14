package gui.gamePanel;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public abstract class GamePanelDecorator extends JPanel implements GamePanelInterface {
    protected GamePanelInterface wrappedPanel;

    public GamePanelDecorator(GamePanelInterface panel) {
        this.wrappedPanel = panel;
        this.setLayout(new BorderLayout());
        
        // Musimy dodać panel do tego panelu (dekoratora)
        // Zakładamy, że każdy GamePanelInterface jest też JPanelem
        this.add((JPanel)wrappedPanel, BorderLayout.CENTER);
    }

    @Override
    public void display() {
        wrappedPanel.display();
    }
}