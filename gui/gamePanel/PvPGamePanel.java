package gui.gamePanel;

import gamelogic.Game;

public class PvPGamePanel extends GamePanelDecorator {
    GamePanelInterface wrappe;

    public PvPGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvP Game Panel Behavior");
    }

    @Override
    public void display() {
        wrappe.display();
    }

    public void bindGame(Game game){
        wrappe.bindGame(game);
    }
}
