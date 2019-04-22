import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MakeConnection {
    private static Connection con = null;

    public Connection getConnection(){
        try {
            // db parameters
            String url = "jdbc:sqlite::memory:";
            // create a connection to the database
            con = DriverManager.getConnection(url);

            //System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public  Connection getCon() {
        return con;
    }
}
