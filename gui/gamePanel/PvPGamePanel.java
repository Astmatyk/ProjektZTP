package gui.gamePanel;

public class PvPGamePanel extends GamePanelDecorator {

    public PvPGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvP Game Panel Behavior");
    }

    @Override
    public void display() {
        super.display();
    }
}
