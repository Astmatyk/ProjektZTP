package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AchievementPanel extends JPanel {

    // --- Klasa przycisku pozostaje bez zmian ---
    class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, java.awt.event.ActionListener, javax.swing.table.TableCellEditor {
        private final JButton renderButton;
        private final JButton editButton;
        private final java.util.function.Consumer<ActionEvent> onAction;

        public ButtonColumn(JTable table, int column, java.util.function.Consumer<ActionEvent> onAction) {
            this.onAction = onAction;
            this.renderButton = new JButton();
            this.editButton = new JButton();
            this.editButton.addActionListener(this);
            table.getColumnModel().getColumn(column).setCellRenderer(this);
            table.getColumnModel().getColumn(column).setCellEditor(this);
        }
        @Override public void actionPerformed(ActionEvent e) { fireEditingStopped(); onAction.accept(e); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            renderButton.setText(v == null ? "" : v.toString());
            return renderButton;
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            editButton.setText(v == null ? "" : v.toString());
            return editButton;
        }
        @Override public Object getCellEditorValue() { return editButton.getText(); }
    }

    public AchievementPanel(MainGUI mainGUI) {
        // Zmieniamy BorderLayout na GridBagLayout w głównym panelu, aby lepiej kontrolować przestrzeń
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // 1. TYTUŁ
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel titleLabel = new JLabel("Statystyki i Powtórki", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        this.add(titleLabel, gbc);

        // 2. KONTENER TABEL
        // Używamy GridLayout, aby obie miały tyle samo miejsca
        JPanel tablesContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesContainer.add(createRankingTable());
        tablesContainer.add(createReplayTable());

        gbc.gridy = 1;
        gbc.weighty = 1.0; // To sprawi, że środek wypełni całe wolne miejsce
        gbc.fill = GridBagConstraints.BOTH;
        this.add(tablesContainer, gbc);

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

    private JPanel createRankingTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel("Ranking Graczy", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Poz.", "Gracz", "Wygrane"};
        Object[][] data = {{"1", "Gracz A", "25"}, {"2", "Gracz B", "18"}, {"3", "Gracz C", "10"}};

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // To wymusza, żeby ScrollPane miał od czego zacząć rysowanie
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300, 300)); 
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReplayTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel("Lista Powtórek", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Data", "Mecz", "Akcja"};
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {{"2023-10-25", "A vs Komputer", "Obejrzyj"}}, columns
        );

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);

        new ButtonColumn(table, 2, (e) -> System.out.println("Kliknięto obejrzyj"));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300, 300));
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}