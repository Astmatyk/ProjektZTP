package gui.gamePanel;

import gamelogic.Game;

public class EvEGamePanel extends GamePanelDecorator {
    GamePanelInterface wrappe;

    public EvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("EvE Game Panel Behavior");
    }

    @Override
    public void display() {
        wrappe.display();
    }

    public void bindGame(Game game){
        wrappe.bindGame(game);
    }
}
