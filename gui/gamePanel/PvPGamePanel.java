package gui.gamePanel;

public class PvPGamePanel extends GamePanelDecorator {

    public PvPGamePanel(GamePanelInterface panel) {
        super(panel);
    }

    @Override
    public void display() {
        System.out.println("PvP Game Panel Behavior");
        super.display();
    }
}
