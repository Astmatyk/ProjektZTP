package gui;
import java.awt.*;
import javax.swing.*;

import gui.gameconfig.*;
import gamelogic.*;
import gamelogic.enums.*;;

public class GameConfigPanel extends JPanel {
    String modeFlag = "PVP";         
    BotDifficulty difficultyFlag = BotDifficulty.NORMAL; // Zmiana na typ enum
    int mapSize;                
    boolean boardsConfigured = false; 
    JButton playButton;

    String nick1, nick2;
    boolean[][] board1, board2;

    GameBuilder gameBuilder;

    public GameConfigPanel(MainGUI mainGUI) {
        //------------------------------------------------Ustawienia okna------------------------------------------------
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //------------------------------------------------1. Tryb gry------------------------------------------------
        JLabel modeLabel = new JLabel("Tryb gry", SwingConstants.CENTER);
        modeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(modeLabel, gbc);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JRadioButton pvp = new JRadioButton("Gracz vs Gracz", true);
        pvp.setEnabled(false);
        JRadioButton pvc = new JRadioButton("Gracz vs Komputer");
        pvc.setEnabled(false);
        JRadioButton cvc = new JRadioButton("Komputer vs Komputer");
        cvc.setEnabled(false);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(pvp); modeGroup.add(pvc); modeGroup.add(cvc);
        modePanel.add(pvp); modePanel.add(pvc); modePanel.add(cvc);
        gbc.gridy = 1;
        this.add(modePanel, gbc);

        //------------------------------------------------2. Tryb Trudności------------------------------------------------
        JLabel diffLabel = new JLabel("Poziom trudności (dla AI)", SwingConstants.CENTER);
        diffLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridy = 2;
        this.add(diffLabel, gbc);

        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JRadioButton easy = new JRadioButton("Łatwy");
        JRadioButton normal = new JRadioButton("Normalny", true);
        JRadioButton hard = new JRadioButton("Trudny");

        ButtonGroup diffGroup = new ButtonGroup();
        diffGroup.add(easy); diffGroup.add(normal); diffGroup.add(hard);
        diffPanel.add(easy); diffPanel.add(normal); diffPanel.add(hard);
        gbc.gridy = 3;
        this.add(diffPanel, gbc);

        //------------------------------------------------3. Ustawienia mapy------------------------------------------------
        JLabel boardLabel = new JLabel("Ustawienia planszy", SwingConstants.CENTER);
        boardLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridy = 4;
        this.add(boardLabel, gbc);

        JPanel boardSetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JRadioButton sizeTen = new JRadioButton("10x10", true);
        sizeTen.setEnabled(false);
        JRadioButton sizeTwenty = new JRadioButton("20x20");
        sizeTwenty.setEnabled(false);
        JRadioButton sizeThirty = new JRadioButton("30x30");

        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(sizeTen); sizeGroup.add(sizeTwenty); sizeGroup.add(sizeThirty);
        boardSetPanel.add(sizeTen); boardSetPanel.add(sizeTwenty); boardSetPanel.add(sizeThirty);
        gbc.gridy = 5;
        this.add(boardSetPanel, gbc);

        //------------------------------------------------4. Nazwy graczy------------------------------------------------
        JPanel nickPanel = new JPanel(new GridBagLayout());
        GridBagConstraints ngbc = new GridBagConstraints();
        ngbc.insets = new Insets(5, 5, 5, 5);

        JLabel firstNickLabel = new JLabel("Gracz 1");
        ngbc.gridx = 0; ngbc.gridy = 0; ngbc.anchor = GridBagConstraints.EAST;
        nickPanel.add(firstNickLabel, ngbc);

        JTextField firstText = new JTextField("Gracz 1", 15);
        ngbc.gridx = 1; ngbc.weightx = 1.0; ngbc.fill = GridBagConstraints.HORIZONTAL;
        nickPanel.add(firstText, ngbc);

        JLabel secondNickLabel = new JLabel("Gracz 2");
        ngbc.gridx = 0; ngbc.gridy = 1; ngbc.weightx = 0; ngbc.fill = GridBagConstraints.NONE;
        nickPanel.add(secondNickLabel, ngbc);

        JTextField secondText = new JTextField("Gracz 2", 15);
        ngbc.gridx = 1; ngbc.weightx = 1.0; ngbc.fill = GridBagConstraints.HORIZONTAL;
        nickPanel.add(secondText, ngbc);

        gbc.gridy = 6;
        this.add(nickPanel, gbc);

        //------------------------------------------------5. Konfiguracja i Przyciski------------------------------------------------
        JButton configBoardButton = new JButton("Konfiguracja plansz");
        gbc.gridy = 7;
        this.add(configBoardButton, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        playButton = new JButton("Graj");
        JButton backButton = new JButton("Wstecz");
        actionPanel.add(playButton); actionPanel.add(backButton);
        gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE;
        this.add(actionPanel, gbc);

        //------------------------------------------------LOGIKA I LISTENERY------------------------------------------------
        
        // Listenery Trybu Gry
        pvp.addActionListener(e -> { modeFlag = "PVP"; updateUIState(easy, normal, hard, diffLabel, configBoardButton, pvp, pvc); });
        pvc.addActionListener(e -> { modeFlag = "PVE"; updateUIState(easy, normal, hard, diffLabel, configBoardButton, pvp, pvc); });
        cvc.addActionListener(e -> { modeFlag = "EVE"; updateUIState(easy, normal, hard, diffLabel, configBoardButton, pvp, pvc); });

        // Listenery Trudności - użycie ENUM
        easy.addActionListener(e -> difficultyFlag = BotDifficulty.EASY);
        normal.addActionListener(e -> difficultyFlag = BotDifficulty.NORMAL);
        hard.addActionListener(e -> difficultyFlag = BotDifficulty.HARD);

        // Rozmiar mapy
        Runnable updateMapSettings = () -> mapSize = sizeTen.isSelected() ? 10 : sizeTwenty.isSelected() ? 20 : 30;
        sizeTen.addActionListener(e -> updateMapSettings.run());
        sizeTwenty.addActionListener(e -> updateMapSettings.run());
        sizeThirty.addActionListener(e -> updateMapSettings.run());
        updateMapSettings.run();

        // Akcje przycisków
        backButton.addActionListener(e -> mainGUI.showView("MENU"));

        configBoardButton.addActionListener(e -> {
            if (modeFlag.equals("PVP")) {
                PvPBoardConfigurator configurator = new PvPBoardConfigurator(mapSize);
                int result = JOptionPane.showConfirmDialog(this, configurator, "Konfiguracja PVP", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    board1 = configurator.getBoards().get(0);
                    board2 = configurator.getBoards().get(1);
                    boardsConfigured = true;
                    updatePlayButtonState();
                }
            } else if (modeFlag.equals("PVE")) {
                PvEBoardConfigurator configurator = new PvEBoardConfigurator(mapSize);
                int result = JOptionPane.showConfirmDialog(this, configurator, "Konfiguracja PVE", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    board1 = configurator.getBoards().get(0);
                    board2 = configurator.getBoards().get(1);
                    boardsConfigured = true;
                    updatePlayButtonState();
                    gameBuilder = new GameBuilderPvE(mapSize, nick1, nick2, board1, board2, difficultyFlag);
                }
            }
        });

        playButton.addActionListener(e -> {
            if (modeFlag.equals("EVE")) {
                EvEBoardConfigurator configurator = new EvEBoardConfigurator(mapSize);
                board1 = configurator.getBoards().get(0);
                board2 = configurator.getBoards().get(1);
                boardsConfigured = true;
                //gameBuilder = new GameBuilderEvE(mapSize, difficultyFlag, board1, board2); -- do implementacji jak ogarnę konstruktor
            }
            
            nick1 = firstText.getText();
            nick2 = secondText.getText();
            test(); 
            
            // Przekazanie flagi trudności do mainGUI
            mainGUI.launchGame(mapSize, modeFlag, nick1, nick2);
        });

        updateUIState(easy, normal, hard, diffLabel, configBoardButton, pvp, pvc);
    }

    private void updateUIState(JRadioButton e, JRadioButton n, JRadioButton h, JLabel l, JButton configBtn, JRadioButton pvp, JRadioButton pvc) {
        boolean isAI = !modeFlag.equals("PVP");
        e.setEnabled(isAI);
        n.setEnabled(isAI);
        h.setEnabled(isAI);
        l.setEnabled(isAI);
        
        configBtn.setEnabled(pvp.isSelected() || pvc.isSelected());
        boardsConfigured = false; 
        updatePlayButtonState();
    }

    private void updatePlayButtonState() {
        if (modeFlag.equals("EVE")) playButton.setEnabled(true);
        else playButton.setEnabled(boardsConfigured);
    }

    private void test() {
        System.out.println("--- TEST KONFIGURACJI ---");
        System.out.println("Tryb: " + modeFlag + " | Trudność: " + difficultyFlag + " | Rozmiar: " + mapSize);
    }
}