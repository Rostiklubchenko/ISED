import java.sql.*;

public class DataConnect {
    String databaseURL;
    public DataConnect(String databaseURL){
        this.databaseURL = databaseURL;
    }
    Object[][] getDatabase() {
        String databaseURL = "jdbc:ucanaccess://Cinema.accdb";
        int id;
        int row;
        int place;
        String firstName;
        String lastName;
        Object[][] data = new Object[GetRows()][5];
        try (Connection connection = DriverManager.getConnection(databaseURL)) {

            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            int i = 0;

            while (result.next()) {
                id = result.getInt("ID");
                firstName = result.getString("First_Name");
                lastName = result.getString("Last_Name");
                row = result.getInt("Row_Place");
                place = result.getInt("Place");
                System.out.println(id + ". " + firstName + "   " + lastName + "   " + row + "   " + place);
                data[i][0] = id;
                data[i][1] = firstName;
                data[i][2] = lastName;
                data[i][3] = row;
                data[i][4] = place;
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
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