package view;

import controller.TaskController;
import model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private TaskController controller = new TaskController();
    private JTable table;
    private DefaultTableModel tableModel;

    public MainFrame() {
        setTitle("Gerenciador de Tarefas");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Título", "Descrição", "Concluída"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField titleField = new JTextField(20);
        JTextField descField = new JTextField(20);
        inputPanel.add(new JLabel("Título:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Descrição:"));
        inputPanel.add(descField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addBtn = new JButton("Adicionar");
        JButton removeBtn = new JButton("Remover");
        JButton completeBtn = new JButton("Concluir/Pendência");
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(completeBtn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            if (!title.isEmpty()) {
                Task task = new Task(title, desc);
                controller.addTask(task);
                refreshTable();
                titleField.setText("");
                descField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Título não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int index = table.getSelectedRow();
            if (index >= 0) {
                controller.removeTask(index);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        completeBtn.addActionListener(e -> {
            int index = table.getSelectedRow();
            if (index >= 0) {
                Task task = controller.getTasks().get(index);
                controller.completeTask(index, !task.isCompleted());
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para alterar o status.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 2) {
                    boolean isCompleted = (boolean) table.getValueAt(row, col);
                    controller.completeTask(row, !isCompleted);
                    refreshTable();
                }
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Task t : controller.getTasks()) {
            tableModel.addRow(new Object[]{t.getTitle(), t.getDescription(), t.isCompleted()});
        }
    }
}
