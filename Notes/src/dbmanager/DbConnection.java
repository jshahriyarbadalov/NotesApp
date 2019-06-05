package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class DbConnection {

    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    public void connect() throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "1234";
        String URL = "jdbc:mysql://localhost:3306/notes";
        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);
        connection = DriverManager.getConnection(URL, username, password);
    }

    public void disconnect() {
        try {
            if(resultSet!=null){
                resultSet.close();
            }
            if(preparedStatement!= null){
                preparedStatement.close();
            }
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        } 
    }

}
