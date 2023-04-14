import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Table extends JFrame {
    String databaseURL = "jdbc:ucanaccess://Cinema.accdb";
    DefaultTableModel tableModel;
    JTable table;
    Table() {
        System.out.println(GetRows());
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
        PrintDatabase();
//        table.setFillsViewportHeight(true);
//        container.add(table.getTableHeader(), BorderLayout.PAGE_START);
//        container.add(table, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.CENTER);
//        add(container, BorderLayout.CENTER);

        JButton deleteInf = new JButton("Delete");
        deleteInf.addActionListener(e -> delete());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addInf);
        buttonPanel.add(deleteInf);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }



    void PrintDatabase() {
        String databaseURL = "jdbc:ucanaccess://Cinema.accdb";
        try (Connection connection = DriverManager.getConnection(databaseURL)) {

            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            int i = 0;

            while (result.next()) {
                int id = result.getInt("ID");
                String firstName = result.getString("First_Name");
                String lastName = result.getString("Last_Name");
                int row = result.getInt("Row_Place");
                int place = result.getInt("Place");
                tableModel.addRow(new Object[]{id, firstName, lastName, row, place});
                i++;
                System.out.println(id + ". " + firstName + "   " + lastName + "   " + row + "   " + place);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void delete() {
        int[] selectedRows = table.getSelectedRows();
        for (int row : selectedRows) {
            int id = (int) table.getValueAt(row, 0);
            tableModel.removeRow(row);
            deleteInfo(id);
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
            tableModel.addRow(new Object[]{GetFreeID(), firstName, lastName, row, place});
        }
        AddNewInfo(firstName, lastName, row, place);
    }

    int GetFreeID() {
        int id = 0;
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                id = result.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id+1;
    }

    int GetRows() {
        int i = 0;
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    void AddNewInfo(String firstName, String lastName, int row, int place) {
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "INSERT INTO Places (First_Name, Last_Name, Row_Place, Place) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, String.valueOf(row));
            preparedStatement.setString(4, String.valueOf(place));
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("A row has been inserted successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void deleteInfo(int ID) {
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "DELETE FROM Places where ID=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, String.valueOf(ID));
            pst.execute();
            System.out.println("Deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
