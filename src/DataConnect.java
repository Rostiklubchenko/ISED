import java.sql.*;

public class DataConnect {
    String databaseURL;
    public DataConnect(String databaseURL) {
        this.databaseURL = databaseURL;
    }
    Object[][] getDatabase() {
        Object[][] data = new Object[getRows()][5];
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            int i = 0;
            while (result.next()) {
                data[i][0] = result.getInt("ID");
                data[i][1] = result.getString("First_Name");
                data[i][2] = result.getString("Last_Name");
                data[i][3] = result.getInt("Row_Place");
                data[i][4] = result.getInt("Place");
                System.out.println(data[i][0] + ". " + data[i][1] + "   " + data[i][2] + "   " + data[i][3] + "   " + data[i][4]);
                i++;
            }
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    int getFreeID() {
        int id = 0;
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                id = result.getInt("ID");
            }
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id + 1;
    }
    int getRows() {
        int i = 0;
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT * FROM Places";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                i++;
            }
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
    void addNewInfo(String firstName, String lastName, int row, int place) {
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
            connection.close();
            preparedStatement.close();
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
            connection.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}