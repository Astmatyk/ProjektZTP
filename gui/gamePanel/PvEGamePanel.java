package gui.gamePanel;

public class PvEGamePanel extends GamePanelDecorator{
    public PvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvE Game Panel Behavior");
    }

    public void displayBehavior(){
        super.display();
    }
}
