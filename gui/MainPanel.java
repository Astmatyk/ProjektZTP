package gui;

import gamelogic.AudioManager;
import java.awt.*;
import javax.swing.*;

public class MainPanel extends GuiPanel {
    private AudioManager audioManager;

    public MainPanel(MainGUI parent) {
        // Inicjalizuj AudioManager i odtwórz muzykę
        audioManager = new AudioManager();
        audioManager.playAudioLoop("background.wav");

        // panelem menu jest gridbag z przyciskami
        this.setLayout(new GridBagLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 20));

        JButton button0 = createTextButton("Graj", () -> parent.showView("GAME_CONFIG"));
        JButton button1 = createTextButton("Osiągnięcia", () -> parent.showView("ACHIEVEMENT"));
        JButton button2 = createTextButton("Rankingi", () -> parent.showView("RANKING"));
        JButton button3 = createTextButton("Wyjdź", () -> System.exit(0));

        // Preferowany rozmiar przycisków
        Dimension btn_dims = new Dimension(300, 50);
        button0.setPreferredSize(btn_dims);
        button1.setPreferredSize(btn_dims);
        button2.setPreferredSize(btn_dims);
        button3.setPreferredSize(btn_dims);

        // Sygnały wysyłane do MainGUI
        button0.addActionListener(e -> parent.showView("GAME_CONFIG"));
        button1.addActionListener(e -> parent.showView("ACHIEVEMENT"));
        button2.addActionListener(e -> parent.showView("RANKING"));
        button3.addActionListener(e -> System.exit(0));

        buttonsPanel.add(button0);
        buttonsPanel.add(button1);  
        buttonsPanel.add(button2);
        buttonsPanel.add(button3);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Dodajemy wycentrowany panel z przyciskami do głównego panelu
        this.add(buttonsPanel, gbc);
    }
}