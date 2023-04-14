import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Table extends JFrame {
    DataConnect connection = new DataConnect("jdbc:ucanaccess://Cinema.accdb");
    DefaultTableModel tableModel;
    JTable table;
    Table() {
        System.out.println(connection.GetRows());
        setTitle("Це база друже");
        setPreferredSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        JButton addInf = new JButton("Add");
        addInf.addActionListener(e -> add());

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Row");
        tableModel.addColumn("Place");
        fillTable(connection.getDatabase());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton deleteInf = new JButton("Delete");
        deleteInf.addActionListener(e -> delete());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addInf);
        buttonPanel.add(deleteInf);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    void fillTable(Object[][] data){
        for (int i = 0; i < connection.GetRows(); i++){
            int id = (int) data[i][0];
            String firstName = (String) data[i][1];
            String lastName = (String) data[i][2];
            int row = (int) data[i][3];
            int place = (int) data[i][4];
            tableModel.addRow(new Object[]{id, firstName, lastName, row, place});
        }
    }

    void delete() {
        int[] selectedRows = table.getSelectedRows();
        for (int row : selectedRows) {
            int id = (int) table.getValueAt(row, 0);
            tableModel.removeRow(row);
            connection.deleteInfo(id);
        }
    }

    void add() {
        int row;
        int place;
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String tx_row;
        while (true){
            tx_row = JOptionPane.showInputDialog("Enter row:");
            try {
                row = Integer.parseInt(tx_row);
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Table.this,
                        "Row must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        String tx_place;
        while (true){
            tx_place = JOptionPane.showInputDialog("Enter place:");
            try {
                place = Integer.parseInt(tx_place);
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Table.this,
                        "Place must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (firstName != null && lastName != null && row != 0 && place != 0) {
            tableModel.addRow(new Object[]{connection.GetFreeID(), firstName, lastName, row, place});
        }
        connection.AddNewInfo(firstName, lastName, row, place);
    }
}
