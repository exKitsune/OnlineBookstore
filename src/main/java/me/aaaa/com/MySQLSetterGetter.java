package me.aaaa.com;

import java.lang.reflect.Member;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.aaaa.com.Book;
import me.aaaa.com.Main;

public class MySQLSetterGetter {
    public boolean addBook(Book book) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE isbn=?");
            statement.setString(1, book.getISBN());

            ResultSet results = statement.executeQuery();
            if (!(results.next())) { //if book does not exist, then add
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO books (isbn,author,title,price,subject) VALUES (?,?,?,?,?)");
                insert.setString(1, book.getISBN());
                insert.setString(2, book.getAuthor());
                insert.setString(3, book.getTitle());
                insert.setDouble(4, book.getPrice());
                insert.setString(5, book.getSubject());
                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Book getBook(String ISBN) { 
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE isbn=?");
            statement.setString(1, ISBN);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Book book = new Book(results.getString("isbn"), results.getString("author"), results.getString("title"), results.getDouble("price"), results.getString("subject"));
                
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Book();
    }

    public List<Book> getBooksByQuery(String queryType, String query) { //queryTypes: "author", "title", "subject"
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM books WHERE " + queryType + "=?");
            statement.setString(1, query);

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Book book = new Book(results.getString("isbn"), results.getString("author"), results.getString("title"), results.getDouble("price"), results.getString("subject"));
                
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<String> getBookSubjects(){
        List<String> subjects = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT DISTINCT subject FROM books ORDER BY subject");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                subjects.add(results.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    public boolean addMember(Person person) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM members WHERE userid=?");
            statement.setString(1, person.getUID());

            ResultSet results = statement.executeQuery();
            if (!(results.next())) { //if member does not exist, then add
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO members (fname, lname, address, city, state, zip, phone, email, userid, password, creditcardtype, creditcardnumber) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                insert.setString(1, person.getFirstName());
                insert.setString(2, person.getLastName());
                insert.setString(3, person.getAddr());
                insert.setString(4, person.getCity());
                insert.setString(5, person.getState());
                insert.setInt(6, person.getZip());
                insert.setString(7, person.getPhone());
                insert.setString(8, person.getEmail());
                insert.setString(9, person.getUID());
                insert.setString(10, person.getPass());
                insert.setString(11, person.getCCType());
                insert.setString(12, person.getCCNum());
                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Person getMember(String userid) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM members WHERE userid=?");
            statement.setString(1, userid);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Person person = new Person(results.getString("fname"),
                        results.getString("lname"),
                        results.getString("address"),
                        results.getString("city"),
                        results.getString("state"),
                        results.getInt("zip"),
                        results.getString("phone"),
                        results.getString("email"),
                        results.getString("userid"),
                        results.getString("password"),
                        results.getString("creditcardtype"),
                        results.getString("creditcardnumber"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Person();
    }

    public boolean updateMember(Person person) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM members WHERE userid=?");
            statement.setString(1, person.getUID());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                PreparedStatement insert = Main.getConnection().prepareStatement("UPDATE members SET fname=?,lname=?,address=?,city=?,state=?,zip=?,phone=?,email=?,password=?,creditcardtype=?,creditcardnumber=? WHERE userid=?");
                insert.setString(1, person.getFirstName());
                insert.setString(2, person.getLastName());
                insert.setString(3, person.getAddr());
                insert.setString(4, person.getCity());
                insert.setString(5, person.getState());
                insert.setInt(6, person.getZip());
                insert.setString(7, person.getPhone());
                insert.setString(8, person.getEmail());
                insert.setString(9, person.getPass());
                insert.setString(10, person.getCCType());
                insert.setString(11, person.getCCNum());
                insert.setString(12, person.getUID());
                insert.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int currentOrderNum() {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT MAX(ono) AS top_ono FROM orders");

            ResultSet results = statement.executeQuery();
            if(results.next()) {
                return results.getInt("top_ono") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public boolean addOrder(Order order) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM orders WHERE ono=?");
            statement.setInt(1, order.getOrderNum());

            ResultSet results = statement.executeQuery();
            if (!(results.next())) {
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO orders (userid, ono, received, shipped, shipAddress, shipCity, shipState, shipZip) VALUES (?,?,?,?,?,?,?,?)");
                insert.setString(1, order.getUserid());
                insert.setInt(2, order.getOrderNum());
                insert.setDate(3, order.getOrderDate());
                insert.setDate(4, order.getShipDate());
                insert.setString(5, order.getAddr());
                insert.setString(6, order.getCity());
                insert.setString(7, order.getState());
                insert.setInt(8, order.getZip());

                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getAssociatedOrders(String userid) {
        List<Integer> orders = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT ono FROM orders WHERE userid=? ORDER BY ono");
            statement.setString(1, userid);

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                orders.add(results.getInt("ono"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public Order getOrder(int orderNum) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM orders WHERE ono=?");
            statement.setInt(1, orderNum);

            ResultSet results = statement.executeQuery();
            if (results.next()) { //if book does not exist, then add
                Order order = new Order(results.getString("userid"),
                        results.getInt("ono"),
                        results.getDate("received"),
                        results.getDate("shipped"),
                        results.getString("shipAddress"),
                        results.getString("shipCity"),
                        results.getString("shipState"),
                        results.getInt("shipZip"));

                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Order();
    }

    public boolean addOrderDetail(OrderDetails ode) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM odetails WHERE ono=? AND isbn=?");
            statement.setInt(1, ode.getOrderNum());
            statement.setString(2, ode.getISBN());

            ResultSet results = statement.executeQuery();
            if (!(results.next())) {
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO odetails (ono, isbn, qty, price) VALUES (?,?,?,?)");
                insert.setInt(1, ode.getOrderNum());
                insert.setString(2, ode.getISBN());
                insert.setInt(3, ode.getQty());
                insert.setDouble(4, ode.getPrice());

                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAssociatedOrderDetailISBNs(int orderNum) {
        List<String> isbns = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT DISTINCT isbn FROM odetails WHERE ono=? ORDER BY isbn");
            statement.setInt(1, orderNum);

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                isbns.add(results.getString("isbn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isbns;
    }

    public OrderDetails getOrderDetail(int orderNum, String ISBN) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM odetails WHERE ono=? AND isbn=?");
            statement.setInt(1, orderNum);
            statement.setString(2, ISBN);

            ResultSet results = statement.executeQuery();
            if (results.next()) { //if book does not exist, then add
                OrderDetails ode = new OrderDetails(results.getInt("ono"),
                        results.getString("isbn"),
                        results.getInt("qty"),
                        results.getDouble("price"));

                return ode;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new OrderDetails();
    }

    public boolean addToCart(CartEntry ce) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM cart WHERE userid=? AND isbn=?");
            statement.setString(1, ce.getUserid());
            statement.setString(2, ce.getISBN());

            ResultSet results = statement.executeQuery();
            if (!(results.next())) {
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO cart (userid, isbn, qty) VALUES (?,?,?)");
                insert.setString(1, ce.getUserid());
                insert.setString(2, ce.getISBN());
                insert.setInt(3, ce.getQty());

                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getAssociatedCartEntryISBNs(String userid) {
        List<String> isbns = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT isbn FROM cart WHERE userid=? ORDER BY isbn");
            statement.setString(1, userid);

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                isbns.add(results.getString("isbn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isbns;
    }

    public CartEntry getCartEntry(String userid, String ISBN) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM cart WHERE userid=? AND isbn=?");
            statement.setString(1, userid);
            statement.setString(2, ISBN);

            ResultSet results = statement.executeQuery();
            if (results.next()) { //if book does not exist, then add
                CartEntry ce = new CartEntry(results.getString("userid"),
                        results.getString("isbn"),
                        results.getInt("qty"));

                return ce;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new CartEntry();
    }

    public boolean editCartEntry(String userid, String ISBN, int newQty) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("SELECT * FROM cart WHERE userid=? AND isbn=?");
            statement.setString(1, userid);
            statement.setString(2, ISBN);

            ResultSet results = statement.executeQuery();
            if (results.next()) { //if book does not exist, then add
                PreparedStatement insert;
                if(newQty > 0) {
                    insert = Main.getConnection().prepareStatement("UPDATE cart SET qty=? WHERE userid=? AND isbn=?");
                    insert.setInt(1, newQty);
                    insert.setString(2, userid);
                    insert.setString(3, ISBN);
                } else {
                    insert = Main.getConnection().prepareStatement("DELETE FROM cart WHERE userid=? AND isbn=?");
                    insert.setString(1, userid);
                    insert.setString(2, ISBN);
                }

                insert.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean clearCart(String userid) {
        try {
            PreparedStatement statement = Main.getConnection().prepareStatement("DELETE FROM cart WHERE userid=?");
            statement.setString(1, userid);

            statement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
