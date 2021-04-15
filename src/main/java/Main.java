import java.io.IOException;
import java.net.URL;
import java.sql.*;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Main {
    private static Connection connection;
    public static String host, database, username, password, table;
    public static int port;

    public static void mysqlSetup() {

        host = "localhost";
        port = 3306;
        database = "bookstore";
        username = "root";
        password = "root";

        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db" );
//            Statement a = connection.createStatement();
//            a.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);

            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() throws IOException, SQLException {
        URL resource = Resources.getResource(Main.class,"tables.sql");
        String[] databaseStructure = Resources.toString(resource, Charsets.UTF_8).split(";");

        if (databaseStructure.length == 0) {
            return;
        }

        Statement statement = null;

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            for (String query : databaseStructure) {
                query = query.trim();

                if (query.isEmpty()) {
                    continue;
                }

                statement.execute(query);
            }

            connection.commit();

        } finally {
            connection.setAutoCommit(true);

            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        mysqlSetup();
        MySQLSetterGetter msg = new MySQLSetterGetter();
        msg.addBook("1234567890", "fruit", "hello world", 420.69, "fruit stuff");
        msg.addBook("0987654321", "aaaa", "packer instructions", 100.00, "blank page");
        msg.listBooks();
        return;
    }
}
