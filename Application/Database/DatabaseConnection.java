package Application.Database;

import java.sql.*;

public class DatabaseConnection {
    public Connection conn;
    public PreparedStatement pSt;

    // change these to connect to your local database:
    public static final String DB_USER = "sam";
    public static final String DB_NAME = "prisoners";
    public static final String DB_PASSWORD = "sam";
    public static final String PORT = "3306";
    public static final String DB_HOST = "localhost";
    public static final String DB = "mariadb";

    public DatabaseConnection() {
        connect();
    }


    private void connect() {
        try {
            // format example: "jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword"
            conn = DriverManager.getConnection("jdbc:" + DB + "://" +  DB_HOST + ":" + PORT + "/" + DB_NAME
            + "?user=" + DB_USER + "&password=" + DB_PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (pSt != null) {
                pSt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
