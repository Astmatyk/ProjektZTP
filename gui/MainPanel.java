package gui;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends GuiPanel {
    public MainPanel(MainGUI parent) {
        // Ustawienie układu dla samego panelu menu
        this.setLayout(new GridLayout(3, 1, 10, 20));

        JButton button0 = createTextButton("Graj", () -> parent.showView("GAME_CONFIG"));
        JButton button1 = createTextButton("Osiągnięcia", () -> parent.showView("ACHIEVEMENT"));
        JButton button2 = createTextButton("Rankingi", () -> parent.showView("RANKING"));
        JButton button3 = createTextButton("Wyjdź", () -> System.exit(0));

        // Preferowany rozmiar przycisków
        button0.setPreferredSize(new Dimension(300, 50));
        button1.setPreferredSize(new Dimension(300, 50));
        button2.setPreferredSize(new Dimension(300, 50));
        button3.setPreferredSize(new Dimension(300, 50));

        // Sygnały wysyłane do MainGUI
        button0.addActionListener(e -> parent.showView("GAME_CONFIG"));
        button1.addActionListener(e -> parent.showView("ACHIEVEMENT"));
        button2.addActionListener(e -> parent.showView("RANKING"));
        button3.addActionListener(e -> System.exit(0));

        this.add(button0);
        this.add(button1);  
        this.add(button2);
        this.add(button3);
    }
}