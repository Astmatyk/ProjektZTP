package gui.gamePanel;

public class EvEGamePanel extends GamePanelDecorator {
    public EvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("EvE Game Panel Behavior");
    }

    @Override
    public void display() {
        
        super.display();
    }
}
