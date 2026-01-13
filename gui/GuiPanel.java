package gui;

import javax.swing.*;

public class GuiPanel extends JPanel {
    public JButton createTextButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addActionListener(e -> action.run());
        return button;
    }
}
