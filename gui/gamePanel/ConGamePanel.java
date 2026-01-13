package gui.gamePanel;

import gui.MainGUI;
import java.awt.*;
import javax.swing.*;

public class ConGamePanel extends JPanel implements GamePanelInterface {

    public ConGamePanel(MainGUI mainGUI) {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Panel Rozgrywki - w budowie");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("Wstecz");
        backButton.addActionListener(e -> mainGUI.showView("MENU"));
        add(backButton, BorderLayout.SOUTH);
    }

    @Override
    public void display() {
        // Domyślne renderowanie
        setVisible(true);
    }
}
