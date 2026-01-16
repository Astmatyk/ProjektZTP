package gui;

import gui.gameconfig.*;
import java.awt.*;
import javax.swing.*;

public class GameConfigPanel extends JPanel {
    String modeFlag;        // globalny tryb gry
    int mapSize;            // globalny rozmiar planszy
    boolean boardsConfigured = false; // czy plansze zostały skonfigurowane (PvP/PvE)
    JButton playButton;

    String nick1, nick2;
    boolean board1[][], board2[][];

    public GameConfigPanel(MainGUI mainGUI) {
        //------------------------------------------------Ustawienia okna------------------------------------------------
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        //------------------------------------------------Tryb gry------------------------------------------------
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

        //------------------------------------------------Ustawienia mapy------------------------------------------------
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

        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(sizeTen); sizeGroup.add(sizeTwenty); sizeGroup.add(sizeThirty);

        boardPanel.add(sizeTen); boardPanel.add(sizeTwenty); boardPanel.add(sizeThirty);
        gbc.gridy = 3;
        this.add(boardPanel, gbc);

        // Ustawienia mapSize
        Runnable updateMapSettings = () -> {
            mapSize = sizeTen.isSelected()?10:sizeTwenty.isSelected()?20:30;
        };
        sizeTen.addActionListener(e->updateMapSettings.run());
        sizeTwenty.addActionListener(e->updateMapSettings.run());
        sizeThirty.addActionListener(e->updateMapSettings.run());
        updateMapSettings.run();

        //------------------------------------------------Nazwy graczy------------------------------------------------

        // Panel na nicki
        JPanel nickPanel = new JPanel(new GridBagLayout());
        GridBagConstraints ngbc = new GridBagConstraints();
        ngbc.insets = new Insets(5, 5, 5, 5);

        // Label: Gracz 1
        JLabel firstNick = new JLabel("Gracz 1");
        ngbc.gridx = 0;
        ngbc.gridy = 0;
        ngbc.weightx = 0;
        ngbc.fill = GridBagConstraints.NONE;
        ngbc.anchor = GridBagConstraints.EAST;
        nickPanel.add(firstNick, ngbc);

        // Pole tekstowe: Gracz 1
        JTextField firstText = new JTextField("Gracz 1", 15);
        ngbc.gridx = 1;
        ngbc.gridy = 0;
        ngbc.weightx = 1.0;
        ngbc.fill = GridBagConstraints.HORIZONTAL;
        nickPanel.add(firstText, ngbc);

        // (opcjonalnie) Gracz 2
        JLabel secondNick = new JLabel("Gracz 2");
        ngbc.gridx = 0;
        ngbc.gridy = 1;
        ngbc.weightx = 0;
        ngbc.fill = GridBagConstraints.NONE;
        ngbc.anchor = GridBagConstraints.EAST;
        nickPanel.add(secondNick, ngbc);

        JTextField secondText = new JTextField("Gracz 2", 15);
        ngbc.gridx = 1;
        ngbc.gridy = 1;
        ngbc.weightx = 1.0;
        ngbc.fill = GridBagConstraints.HORIZONTAL;
        nickPanel.add(secondText, ngbc);

        // Dodanie panelu do głównego layoutu
        gbc.gridx = 0;
        gbc.gridy = 4;          // wiersz w głównym GridBagLayout
        gbc.gridwidth = 2;      // zajmuje całą szerokość
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(nickPanel, gbc);

        //------------------------------------------------Konfiguracja plansz------------------------------------------------
        // Przycisk konfiguracji plansz
        JButton configBoardButton = new JButton("Konfiguracja plansz");
        gbc.gridy = 5; gbc.fill=GridBagConstraints.HORIZONTAL;
        this.add(configBoardButton, gbc);

        // Enable/disable przycisku
        Runnable updateConfigButton = () -> configBoardButton.setEnabled(pvp.isSelected() || pvc.isSelected());
        pvp.addActionListener(e->updateConfigButton.run());
        pvc.addActionListener(e->updateConfigButton.run());
        cvc.addActionListener(e->updateConfigButton.run());
        updateConfigButton.run();


        //------------------------------------------------Przyciski------------------------------------------------
        // Przycisk Graj / Wstecz
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        playButton = new JButton("Graj");
        JButton backButton = new JButton("Wstecz");
        actionPanel.add(playButton); actionPanel.add(backButton);
        gbc.gridy=6; gbc.fill=GridBagConstraints.NONE;
        this.add(actionPanel, gbc);

        //------------------------------------------------Akcje------------------------------------------------
        backButton.addActionListener(e -> mainGUI.showView("MENU"));

        //Akcje przełącznika trybu gry
        configBoardButton.addActionListener(e -> {
            switch(modeFlag){
                case "PVP" -> {
                    PvPBoardConfigurator configurator0 = new PvPBoardConfigurator(mapSize);
                    int result0 = JOptionPane.showConfirmDialog(
                            this,
                            configurator0,
                            "Konfiguracja planszy - PvE",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if(result0==JOptionPane.OK_OPTION) {
                        board1 = configurator0.getBoards().get(0);
                        board2 = configurator0.getBoards().get(1);
                        boardsConfigured = true;
                        updatePlayButtonState();
                    }
                }
                case "PVE" -> {
                    PvEBoardConfigurator configurator1 = new PvEBoardConfigurator(mapSize);
                    int result1 = JOptionPane.showConfirmDialog(
                            this,
                            configurator1,
                            "Konfiguracja planszy - PvE",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if(result1==JOptionPane.OK_OPTION) {
                        board1 = configurator1.getBoards().get(0);
                        board2 = configurator1.getBoards().get(1);
                        boardsConfigured = true;
                        updatePlayButtonState();
                    }
                }
                default -> System.out.println("Błąd modeFlag!");
            }
        });

        //Akcje przycisku "Graj"
        playButton.addActionListener(e -> {
            if(modeFlag.equals("EVE")) {
                System.out.println("Tworzę automatyczne plansze dla EvE");
                EvEBoardConfigurator configurator2 = new EvEBoardConfigurator(mapSize);
                board1 = configurator2.getBoards().get(0);
                board2 = configurator2.getBoards().get(1);
                boardsConfigured = true;
                    updatePlayButtonState();
            } else if(!boardsConfigured) {
                System.out.println("Najpierw skonfiguruj plansze!");
                return;
            }
            System.out.println("Wybrany tryb: "+modeFlag);
            System.out.println("Rozmiar planszy: "+mapSize);

            String p1 = firstText.getText();
            String p2 = secondText.getText();
            test();
            //Powinien być 
            mainGUI.launchGame(mapSize, modeFlag, p1, p2);
        });
        updatePlayButtonState();
    }

    //Przełączanie możliwości naciśnięcia "Graj"
    private void updatePlayButtonState() {
        if(modeFlag.equals("EVE")) playButton.setEnabled(true);
        else playButton.setEnabled(boardsConfigured);
    }

    private void test(){
        System.out.println("Tryb gry: "+modeFlag);
        System.out.println("Rozmiar mapy: "+mapSize);

        System.out.println("Nick 1:"+nick1);
        for (boolean[] row : board1) {
            for (boolean cell : row) {
                System.out.print(cell ? "S " : ". ");
            }
            System.out.println();
        }

        System.out.println("Nick 2:"+nick2);
        for (boolean[] row : board2) {
            for (boolean cell : row) {
                System.out.print(cell ? "S " : ". ");
            }
            System.out.println();
        }
    }
}