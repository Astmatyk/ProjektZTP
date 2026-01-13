package gui.gamePanel;

public class EvEGamePanel extends GamePanelDecorator {
    public EvEGamePanel(GamePanelInterface panel) {
        super(panel);
    }

    @Override
    public void display() {
        System.out.println("EvE Game Panel Behavior");
        super.display();
    }
}
