import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Book;

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

    public Book getBookDetails(String ISBN) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE isbn=?");
            statement.setString(1, ISBN);

            ResultSet results = statement.executeQuery();
            if (!(results.next())) { //if book does not exist, then add
                Book book = new Book(results.getString("isbn"), results.getString("author"), results.getString("title"), results.getDouble("price"), results.getString("subject"));
                
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addMember(String fName, String lName, String addr, String city, String state, int zip, String phone, String email, String uid, String pass, String ccType, int ccNum) {
        try {
            //Figure out what this does and how to use this in addMember
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM members WHERE uid=?");
            statement.setString(1, uid);

            ResultSet results = statement.executeQuery();
            if (!(results.next())) { //if member does not exist, then add
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO members (fName, lName, addr, city, state, zip, phone, email, uid, pass, ccType, ccNum) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                insert.setString(1, fName);
                insert.setString(2, lName);
                insert.setString(3, addr);
                insert.setString(4, city);
                insert.setString(5, state);
                insert.setInt(6, zip);
                insert.setString(7, phone);
                insert.setString(8, email);
                insert.setString(9, uid);
                insert.setString(10, pass);
                insert.setString(11, ccType);
                insert.setInt(12, ccNum);
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
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books");
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

    public int searchBooks(String userChoice, String userInput) {
        try {
            if(userChoice == 1){ // if user wants to search by author
                PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE author=?");
                statement.setString(1, userInput)
                ResultSet results = statement.executeQuery();
                int count = 0;
                while (results.next()) {
                    if(userInput == results.getString("author")){ // check for the user-provided author's name 
                        System.out.println("\nBook found...\n")
                        System.out.println("Author: " + results.getString(2) +
                                "\nTitle: " + results.getString(3) +
                                "\nISBN: " + results.getString(1) +
                                "\nPrice: $" + results.getDouble(4) +
                                "\nSubject: " + results.getString(5));
                        count += 1;
                    }                    
                }
                return count;
            }
            if(userChoice == 2){ // if user wants to search by title
                PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE title=?");
                statement.setString(1, userInput)
                ResultSet results = statement.executeQuery();
                int count = 0;
                while (results.next()) {
                    if(userInput == results.getString("title")){ // check for the user-provided title 
                        System.out.println("Books found\n")
                        System.out.println("Author: " + results.getString(2) +
                                "\nTitle: " + results.getString(3) +
                                "\nISBN: " + results.getString(1) +
                                "\nPrice: $" + results.getDouble(4) +
                                "\nSubject: " + results.getString(5));
                        count += 1;
                    }
                }
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void listSubject(String subject) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE subject=?");
            statement.setString(1, subject);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                System.out.println("ISBN: " + results.getString(1) +
                        "\nAuthor: " + results.getString(2) +
                        "\nTitle: " + results.getString(3) +
                        "\nPrice: $" + results.getDouble(4) +
                        "\nSubject: " + results.getString(5));
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet listSubjects(){
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT subject FROM books ORDER BY subject");
            ResultSet results = statement.executeQuery();

            int curr = 1;
            while (results.next()) {
                System.out.println(curr.toString + ": " + results.getString(1));
                curr++;
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean memberCheck(String uid, String psswd){
        try{
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM members");
            ResultSet results = statement.executeQuery();

            while (results.next()){
                String userid = results.getString(9);
                String password = results.getString(10);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        // check if given uid and password match
        if ((uid == userid) && (psswd == password)){
            ystem.out.println("Login Successfull");
            return True;
        }
        else{
            System.out.println("Either password or usnername that was entered was wrong");
            return False;
        }
    }

    public boolean addToCart
}
