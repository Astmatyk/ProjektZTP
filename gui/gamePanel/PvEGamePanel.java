package gui.gamePanel;

public class PvEGamePanel extends GamePanelDecorator{
    public PvEGamePanel(GamePanelInterface panel) {
        super(panel);
    }

    public void displayBehavior(){
        System.out.println("PvE Game Panel Behavior");
    }
}
