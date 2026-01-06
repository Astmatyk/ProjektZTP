package gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(MainGUI parent) {
        // Ustawienie układu dla samego panelu menu
        this.setLayout(new GridLayout(3, 1, 10, 20));

        JButton button0 = new JButton("Graj");
        JButton button1 = new JButton("Osiągnięcia");
        JButton button2 = new JButton("Wyjdź");

        // Preferowany rozmiar przycisków
        button0.setPreferredSize(new Dimension(300, 50));
        button1.setPreferredSize(new Dimension(300, 50));
        button2.setPreferredSize(new Dimension(300, 50));

        // Sygnały wysyłane do MainGUI
        button0.addActionListener(e -> parent.showView("GAME"));
        button1.addActionListener(e -> parent.showView("ACHIEVEMENTS"));
        button2.addActionListener(e -> System.exit(0));

        this.add(button0);
        this.add(button1);  
        this.add(button2);
    }
}