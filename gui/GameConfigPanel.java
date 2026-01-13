package gui;

import javax.swing.*;
import java.awt.*;

import gui.gameconfig.PvEBoardConfigurator;

public class GameConfigPanel extends JPanel {
    String modeFlag;        // globalny tryb gry
    int mapSize;            // globalny rozmiar planszy
    boolean useIslands;     // globalna flaga wysp
    boolean boardsConfigured = false; // czy plansze zostały skonfigurowane (PvP/PvE)
    JButton playButton;

    public GameConfigPanel(MainGUI mainGUI) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nagłówek trybu gry
        JLabel modeLabel = new JLabel("Tryb gry", SwingConstants.CENTER);
        modeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(modeLabel, gbc);

        // Panel trybu gry
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        JRadioButton pvp = new JRadioButton("Gracz vs Gracz", true);
        JRadioButton pvc = new JRadioButton("Gracz vs Komputer");
        JRadioButton cvc = new JRadioButton("Komputer vs Komputer");

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(pvp); modeGroup.add(pvc); modeGroup.add(cvc);

        modePanel.add(pvp); modePanel.add(pvc); modePanel.add(cvc);
        gbc.gridy = 1;
        this.add(modePanel, gbc);

        // Aktualizacja flagi trybu
        pvp.addActionListener(e -> { modeFlag="PVP"; updatePlayButtonState(); });
        pvc.addActionListener(e -> { modeFlag="PVE"; updatePlayButtonState(); });
        cvc.addActionListener(e -> { modeFlag="EVE"; updatePlayButtonState(); });
        modeFlag="PVP";

        // Nagłówek ustawień planszy
        JLabel boardLabel = new JLabel("Ustawienia planszy", SwingConstants.CENTER);
        boardLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridy = 2;
        this.add(boardLabel, gbc);

        // Panel ustawień planszy
        JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        JRadioButton sizeTen = new JRadioButton("10x10", true);
        JRadioButton sizeTwenty = new JRadioButton("20x20");
        JRadioButton sizeThirty = new JRadioButton("30x30");
        JCheckBox islands = new JCheckBox("Wyspy");

        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(sizeTen); sizeGroup.add(sizeTwenty); sizeGroup.add(sizeThirty);

        boardPanel.add(sizeTen); boardPanel.add(sizeTwenty); boardPanel.add(sizeThirty); boardPanel.add(islands);
        gbc.gridy = 3;
        this.add(boardPanel, gbc);

        // Ustawienia mapSize i useIslands
        Runnable updateMapSettings = () -> {
            mapSize = sizeTen.isSelected()?10:sizeTwenty.isSelected()?20:30;
            useIslands = islands.isSelected();
        };
        sizeTen.addActionListener(e->updateMapSettings.run());
        sizeTwenty.addActionListener(e->updateMapSettings.run());
        sizeThirty.addActionListener(e->updateMapSettings.run());
        islands.addActionListener(e->updateMapSettings.run());
        updateMapSettings.run();

        // Przycisk konfiguracji plansz
        JButton configBoardButton = new JButton("Konfiguracja plansz");
        gbc.gridy = 4; gbc.fill=GridBagConstraints.HORIZONTAL;
        this.add(configBoardButton, gbc);

        // Enable/disable przycisku
        Runnable updateConfigButton = () -> configBoardButton.setEnabled(pvp.isSelected() || pvc.isSelected());
        pvp.addActionListener(e->updateConfigButton.run());
        pvc.addActionListener(e->updateConfigButton.run());
        cvc.addActionListener(e->updateConfigButton.run());
        updateConfigButton.run();

        // Przycisk Graj / Wstecz
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        playButton = new JButton("Graj");
        JButton backButton = new JButton("Wstecz");
        actionPanel.add(playButton); actionPanel.add(backButton);
        gbc.gridy=5; gbc.fill=GridBagConstraints.NONE;
        this.add(actionPanel, gbc);

        // Akcje
        backButton.addActionListener(e -> mainGUI.showView("MENU"));

        configBoardButton.addActionListener(e -> {
            if(modeFlag.equals("PVE")) {
                PvEBoardConfigurator configurator = new PvEBoardConfigurator(mapSize);
                int result = JOptionPane.showConfirmDialog(
                        this,
                        configurator,
                        "Konfiguracja planszy - PvE",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if(result==JOptionPane.OK_OPTION) {
                    boolean[][] playerBoard = configurator.getPlayerBoard();
                    boardsConfigured = true;
                    updatePlayButtonState();
                    System.out.println("Plansza gracza PvE:");
                    printBoard(playerBoard);
                    System.out.println("Plansza komputera PvE będzie generowana w Builderze");
                }
            } else if(modeFlag.equals("PVP")) {
                System.out.println("Tu uruchamiamy konfigurator PvP");
                boardsConfigured=true;
                updatePlayButtonState();
            }
        });

        playButton.addActionListener(e -> {
            if(modeFlag.equals("EVE")) {
                System.out.println("Tworzę automatyczne plansze dla EvE");
            } else if(!boardsConfigured) {
                System.out.println("Najpierw skonfiguruj plansze!");
                return;
            }
            System.out.println("Wybrany tryb: "+modeFlag);
            System.out.println("Rozmiar planszy: "+mapSize);
            System.out.println("Wyspy: "+useIslands);

            mainGUI.showView("GAME");
        });

        updatePlayButtonState();
    }

    private void updatePlayButtonState() {
        if(modeFlag.equals("EVE")) playButton.setEnabled(true);
        else playButton.setEnabled(boardsConfigured);
    }

    private void printBoard(boolean[][] board){
        for(boolean[] row:board){
            for(boolean cell:row) System.out.print(cell?"S ":" .");
            System.out.println();
        }
    }
}
