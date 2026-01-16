package gui.gamePanel;

import gamelogic.Game;

public class PvEGamePanel extends GamePanelDecorator{
    GamePanelInterface wrappe;

    public PvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvE Game Panel Behavior");
    }

    public void displayBehavior(){
        wrappe.display();
    }

   public void bindGame(Game game){
        wrappe.bindGame(game);
    }
}
