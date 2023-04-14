import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class Table extends JFrame {
    DataConnect connection = new DataConnect("jdbc:ucanaccess://Cinema.accdb");
    DefaultTableModel tableModel;
    JTable table;
    Table() {
        System.out.println(connection.getRows());
        setTitle("Це база друже, надягаю кавун");
        setPreferredSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Row");
        tableModel.addColumn("Place");
        fillTable(connection.getDatabase());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addInf = new JButton("Add");
        addInf.addActionListener(e -> add());

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
        for (int i = 0; i < connection.getRows(); i++){
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
        int row = 0;
        int place = 0;
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String tx_row;
        tx_row = JOptionPane.showInputDialog("Enter row:");
        try {
            row = Integer.parseInt(tx_row);
        } catch (NumberFormatException ignored) {}
        String tx_place;
        tx_place = JOptionPane.showInputDialog("Enter place:");
        try {
            place = Integer.parseInt(tx_place);
        } catch (NumberFormatException ignored) {}
        if (!Objects.equals(firstName, "") && !Objects.equals(lastName, "") && row != 0 && place != 0) {
            tableModel.addRow(new Object[]{connection.getFreeID(), firstName, lastName, row, place});
            connection.addNewInfo(firstName, lastName, row, place);
        } else {
            JOptionPane.showMessageDialog(Table.this,
                    "Something went wrong. Row was not added", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}