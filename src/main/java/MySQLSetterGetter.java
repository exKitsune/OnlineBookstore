import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSetterGetter {
    public boolean addBook(String ISBN, String author, String title, double price, String subject) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE isbn=?");
            statement.setString(1, ISBN);

            ResultSet results = statement.executeQuery();
            if (!(results.next())) { //if book does not exist, then add
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO books (isbn,author,title,price,subject) VALUES (?,?,?,?,?)");
                insert.setString(1, ISBN);
                insert.setString(2, author);
                insert.setString(3, title);
                insert.setDouble(4, price);
                insert.setString(5, subject);
                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void listBooks() {
        try {
            PreparedStatement statement =Main.getConnection().prepareStatement("SELECT * FROM books");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                System.out.println("ISBN: " + results.getString(1) +
                        "\nAuthor: " + results.getString(2) +
                        "\nTitle: " + results.getString(3) +
                        "\nPrice: $" + results.getDouble(4) +
                        "\nSubject: " + results.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
