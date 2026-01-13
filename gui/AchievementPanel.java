package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AchievementPanel extends JPanel {
    public AchievementPanel(MainGUI mainGUI) {
        // Zmieniamy BorderLayout na GridBagLayout w głównym panelu, aby lepiej kontrolować przestrzeń
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // 1. TYTUŁ
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel titleLabel = new JLabel("Osiągnięcia", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        this.add(titleLabel, gbc);

        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel("Lista osiągnięć", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Nazwa osiągnięcia", "Zdobyte"};
        Object[][] data = {{"Przegraj", "Tak"}, {"Zestrzel 10 statków", "Nie"}};

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // To wymusza, żeby ScrollPane miał od czego zacząć rysowanie
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300, 300)); 
        
        panel.add(scroll, BorderLayout.CENTER);
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        this.add(panel, gbc);

        // 3. PRZYCISK WSTECZ
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton backButton = new JButton("Wstecz");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> mainGUI.showView("MENU"));
        this.add(backButton, gbc);
    }
}
