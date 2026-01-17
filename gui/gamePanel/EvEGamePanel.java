package gui.gamePanel;

import gamelogic.Game;

public class EvEGamePanel extends GamePanelDecorator {

    public EvEGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("EvE Game Panel Behavior");
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
