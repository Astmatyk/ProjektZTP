package gui.gamePanel;

import javax.swing.*;
import java.awt.*;

public class ConGamePanel extends JPanel implements GamePanelInterface {

    public ConGamePanel() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Panel Rozgrywki - w budowie");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("Wstecz");
        add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void display() {
        // Domyślne renderowanie
        setVisible(true);
    }
}
