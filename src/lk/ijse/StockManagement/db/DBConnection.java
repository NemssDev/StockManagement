package lk.ijse.StockManagement.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock", "root", "");
        System.out.println("✅ Database connection established!");

    }

    public static DBConnection getInstance() throws ClassNotFoundException, SQLException {

        return dbConnection == null ? dbConnection = new DBConnection() : dbConnection;
    }
}
