package gui.gamePanel;

public abstract class GamePanelDecorator implements GamePanelInterface {

    protected GamePanelInterface wrappedPanel;

    public GamePanelDecorator(GamePanelInterface panel) {
        this.wrappedPanel = panel;
    }

    @Override
    public void display() {
        wrappedPanel.display();
    }
}
