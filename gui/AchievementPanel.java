package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AchievementPanel extends JPanel {

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

    @Override public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        onAction.accept(e);
    }

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
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. NAGŁÓWEK ---
        JLabel titleLabel = new JLabel("Statystyki i Powtórki");
        titleLabel.setFont(this.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel, BorderLayout.NORTH);

        // --- 2. KONTENER NA TABELE (Środek) ---
        JPanel tablesContainer = new JPanel(new GridLayout(1, 2, 20, 0));

        // Tabela 1: Ranking Graczy
        tablesContainer.add(createRankingTable());

        // Tabela 2: Powtórki Rozgrywek
        tablesContainer.add(createReplayTable());

        this.add(tablesContainer, BorderLayout.CENTER);

        // --- 3. PRZYCISK WSTECZ (Dół) ---
        JButton backButton = new JButton("Wstecz");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> mainGUI.showView("MENU"));
        
        // Panel dla przycisku, aby nie rozciągał się na całą szerokość
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(backButton);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel createRankingTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Ranking Graczy", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Pozycja", "Gracz", "Wygrane"};
        Object[][] data = {
            {"1", "Gracz A", "25"},
            {"2", "Gracz B", "18"},
            {"3", "Gracz C", "10"}
        }; // Placeholder danych JSON

        JTable table = new JTable(new DefaultTableModel(data, columns));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReplayTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Lista Powtórek", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Data", "Przeciwnicy", "Akcja"};
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {
                {"2023-10-25", "Gracz A vs Komputer", "Obejrzyj"},
                {"2023-10-24", "Gracz B vs Gracz C", "Obejrzyj"}
            }, columns
        );

        JTable table = new JTable(model);

        // Dodanie przycisku do tabeli
        new ButtonColumn(table, 2, (e) -> {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            System.out.println("Uruchamianie powtórki z wiersza: " + row);
            // Tutaj logika wczytywania konkretnego JSONa z powtórką
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}