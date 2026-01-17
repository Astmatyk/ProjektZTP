package gui.gamePanel;

import gamelogic.Game;

public class PvPGamePanel extends GamePanelDecorator {
public PvPGamePanel(GamePanelInterface panel) {
        super(panel);
        System.out.println("PvP Game Panel Behavior");
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

