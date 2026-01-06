package gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel() {
        this.setLayout(new BorderLayout());
        JLabel label = new JLabel("Panel Rozgrywki - w budowie");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, BorderLayout.CENTER);

        JButton button0 = new JButton("Wstecz");
        this.add(button0, BorderLayout.SOUTH);

        button0.addActionListener(e -> {
            // Akcja przycisku "Wstecz" - powrót do menu głównego
            Container parent = this.getParent();
            if (parent instanceof JPanel) {
                CardLayout layout = (CardLayout) parent.getLayout();
                layout.show(parent, "MENU");
            }
        });
    }
}
