package gui.gamePanel;

public class PvEGamePanel extends GamePanelDecorator {

    public PvEGamePanel(GamePanelInterface panel) {
        super(panel);
    }

    @Override
    public void display() {
        System.out.println("PvE Game Panel Behavior");
        super.display();
    }
}
