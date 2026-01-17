package gui.gamePanel;

import gamelogic.Game;

public class PvEGamePanel extends GamePanelDecorator{

    public PvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvE Game Panel Behavior");
    }

    @Override
    public void display() {
        wrappedPanel.display();
    }

    @Override
    public void bindGame(Game game){
        wrappedPanel.bindGame(game);
    }
}
