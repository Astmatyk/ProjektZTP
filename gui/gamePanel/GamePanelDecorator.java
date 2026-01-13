package gui.gamePanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public abstract class GamePanelDecorator extends JPanel implements GamePanelInterface {
    protected GamePanelInterface wrappedPanel;

    public GamePanelDecorator(GamePanelInterface panel) {
        this.wrappedPanel = panel;
        // Jeśli chcemy, aby dekorator przejął wygląd owiniętego panelu:
        if (panel instanceof JPanel) {
            this.setLayout(new BorderLayout());
            this.add((JPanel)panel, BorderLayout.CENTER);
        }
    }

    @Override
    public void display() {
        wrappedPanel.display();
    }
}
