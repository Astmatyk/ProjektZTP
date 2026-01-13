package gui;

import javax.swing.*;

import gui.gamePanel.ConGamePanel;
import gui.gamePanel.EvEGamePanel;
import gui.gamePanel.GamePanelInterface;
import gui.gamePanel.PvEGamePanel;
import gui.gamePanel.PvPGamePanel;

import java.awt.*;

public class GameConfigPanel extends JPanel {

    public GameConfigPanel(MainGUI mainGUI) {
        // Ustawiamy GridBagLayout, aby mieć pełną kontrolę nad siatką
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Marginesy między elementami
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. MENU WYBORU (Góra) ---
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JRadioButton pvp = new JRadioButton("Gracz vs Gracz", true);
        JRadioButton pvc = new JRadioButton("Gracz vs Komputer");
        JRadioButton cvc = new JRadioButton("Komputer vs Komputer");
        
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(pvp);
        modeGroup.add(pvc);
        modeGroup.add(cvc);
        
        modePanel.add(pvp);
        modePanel.add(pvc);
        modePanel.add(cvc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Rozciągamy na dwie kolumny
        this.add(modePanel, gbc);

        // --- 2. PANELE KONFIGURACJI (Środek) ---
        // Lewy panel
        JPanel leftConfig = createConfigBox("Konfiguracja Gracza 1");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        this.add(leftConfig, gbc);

        // Prawy panel
        JPanel rightConfig = createConfigBox("Konfiguracja Gracza 2");
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(rightConfig, gbc);

        // --- 3. PRZYCISKI NA DOLE ---
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JButton playButton = new JButton("Graj");
        JButton backButton = new JButton("Wstecz");

        // Rozmiar przycisków
        playButton.setPreferredSize(new Dimension(150, 40));
        backButton.setPreferredSize(new Dimension(150, 40));

        actionPanel.add(playButton);
        actionPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        this.add(actionPanel, gbc);

        // --- AKCJE ---
        backButton.addActionListener(e -> mainGUI.showView("MENU"));
        
        playButton.addActionListener(e -> {
            GamePanelInterface basePanel = new ConGamePanel();
            GamePanelInterface decoratedPanel;

            if (pvp.isSelected()) {
                decoratedPanel = new PvPGamePanel(basePanel);
            } else if (pvc.isSelected()) {
                decoratedPanel = new PvEGamePanel(basePanel);
            } else {
                decoratedPanel = new EvEGamePanel(basePanel);
            }   
            // Wywołanie specyficznego zachowania
            decoratedPanel.display();

            // Wyświetlenie w GUI
            mainGUI.showView("GAME"); 
        });
    }

    // Metoda pomocnicza do tworzenia ramek konfiguracji
    private JPanel createConfigBox(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setPreferredSize(new Dimension(250, 150));
        // Tutaj możesz dodać np. JComboBox dla wyboru koloru lub pola tekstowe
        panel.add(new JLabel("Opcje planszy..."));
        return panel;
    }
}