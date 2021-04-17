package me.aaaa.com;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.util.*;

public class Main {
    private static Connection connection;
    public static String host, database, username, password, table;
    public static int port;
    public static MySQLSetterGetter msg;

    public static String currentUser;

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
        URL resource = Resources.getResource(Main.class, "tables.sql");
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

    public static void newMemberRegistration(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("New Member Registration\n");
        System.out.println("Enter first name: ");
        String fName = keyboard.nextLine();
        System.out.println("Enter last name: ");
        String lName = keyboard.nextLine();
        System.out.println("Enter street address: ");
        String addr = keyboard.nextLine();
        System.out.println("Enter city: ");
        String city = keyboard.nextLine();
        System.out.println("Enter state: ");
        String state = keyboard.nextLine();
        System.out.println("Enter zip: ");
        int zip = keyboard.nextInt();
        System.out.println("Enter phone: ");
        String phone = keyboard.nextLine();
        System.out.println("Enter email address: ");
        String email = keyboard.nextLine();
        System.out.println("Enter userID: ");
        String uid = keyboard.nextLine();
        System.out.println("Enter password: ");
        String pass = keyboard.nextLine();
        System.out.println("Do you wish to store credit card information(y/n): ");
        String option = keyboard.nextLine();

        //TODO: Add if/else statement for registration success/failure
        String ccType = "None";
        String ccNum = "0000000000000000";
        if(option.equalsIgnoreCase("y")) {
            while (true) {
                System.out.println("Enter type of Credit Card (amex/discover/mc/visa): ");
                ccType = keyboard.nextLine().toLowerCase();
                if(ccType.equals("amex") || ccType.equals("discover") || ccType.equals("mc") || ccType.equals("visa")) {
                    break;
                }
            }

            if (ccType.equals("amex"))
            {
                do{
                    System.out.println("Enter Credit Card Number: ");
                    ccNum = keyboard.nextLine();

                    if(ccNum.length() != 15)
                    {
                        System.out.println("Invalid Entry.");
                    }
                }while(ccNum.length() != 15);
            } else {
                do{
                    System.out.println("Enter Credit Card Number: ");
                    ccNum = keyboard.nextLine();

                    if(ccNum.length() != 16)
                    {
                        System.out.println("Invalid Entry.");
                    }
                }while(ccNum.length() != 16);
            }
        }

        msg.addMember(new Person(fName, lName, addr, city, state, zip, phone, email, uid, pass, ccType, ccNum));
        
        System.out.printf("You have registered successfully.\n");
        System.out.printf("Name:%-30.30s %s\n", fName, lName);
        System.out.printf("Address:%-30.30s\n", addr);
        System.out.printf("City:%-30.30s, %s %d\n", city, state, zip);
        System.out.printf("Phone:%-30.30s\n", phone);
        System.out.printf("Email:%-30.30s\n", email);
        System.out.printf("UserID:%-30.30s\n", uid);
        System.out.printf("Password:%-30.30s\n", pass);
        System.out.printf("CreditCard Type:%-30.30s\n", ccType);
        System.out.printf("CreditCard Number:%-30.30s\n", ccNum); 
        System.out.println("\n");          
    }
    
    public static boolean memberLogin(String userid, String password){
                //verify user login info
        Person p = msg.getMember(userid);
        
        return password.equals(p.getPass());
    }

    public static void browseBySubject(){
        List<String> subjects = msg.getBookSubjects();
        
        int curr = 0;
        for(String s : subjects) {
            System.out.println(curr + ". " + s);
            curr++;
        }

        String input;
        int choice;

        do {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter your choice: ");
            choice = keyboard.nextInt();

            if (subjects.size() < choice || choice < 0) {
                System.out.println("Please enter a valid number!");
            }
        } while (subjects.size() < choice || choice < 0);

        String subject = subjects.get(choice);

        List<Book> books = msg.getBooksByQuery("subject", subject);
        
        System.out.println("There are " + books.size() + " books on this subject.");

        int currentIdx = 0;
        int currentPage = 1;
        while (true){
            while(currentIdx < books.size() && currentIdx < currentPage * 2) {
                Book b = books.get(currentIdx);
                System.out.println("Author: " + b.getAuthor());
                System.out.println("Title: " + b.getTitle());
                System.out.println("ISBN: " + b.getISBN());
                System.out.println("Price: " + b.getPrice());
                System.out.println("Subject: " + b.getSubject() + "\n");
                currentIdx++;
            }

            System.out.println("Enter ISBN to add to cart or\n" +
                                "n ENTER to browse or\n" +
                                "ENTER to go back to menu:\n");
            input = keyboard.nextLine();
            if (input.equals("")){
                break;
            }
            else if (!input.equalsIgnoreCase("n")) {
                Book b = msg.getBook(input);
                if(!b.getISBN().equals("0000000000")) {
                    System.out.println("Enter Quantity: ");
                    int qty = keyboard.nextInt();
                    if(qty < 1) {
                        System.out.println("Invalid quantity.");
                    } else {
                        msg.addToCart(new CartEntry(currentUser, b.getISBN(), qty));
                    }
                } else {
                    System.out.println("Invalid ISBN.");
                }
            } else {
                currentPage++;
            }
        }
    }

    public static void search(){
        Scanner keyboard = new Scanner(System.in);
        //ResultSet res = msg.listBooks();
        int choice;
        do{
            System.out.println("1. Author Search")
            System.out.println("2. Title Search")
            System.out.println("3. Go back to Member Menu\n")
            choice = keyboard.nextInt();
            
            if(choice == 1)
            {
                System.out.println("\nEnter the Author's name: ");
                String author = keyboard.next();
                List<Book> books = msg.getBooksByQuery("author", author);
        
                System.out.println("There are " + books.size() + " books matching your query.");

                if(books.size() > 0) {
                    int currentIdx = 0;
                    int currentPage = 1;
                    while (true){
                        while(currentIdx < books.size() && currentIdx < currentPage * 2) {
                            Book b = books.get(currentIdx);
                            System.out.println("Author: " + b.getAuthor());
                            System.out.println("Title: " + b.getTitle());
                            System.out.println("ISBN: " + b.getISBN());
                            System.out.println("Price: " + b.getPrice());
                            System.out.println("Subject: " + b.getSubject() + "\n");
                            currentIdx++;
                        }

                        System.out.println("Enter ISBN to add to cart or\n" +
                                            "n ENTER to browse or\n" +
                                            "ENTER to go back to menu:\n");
                        input = keyboard.nextLine();
                        if (input.equals("")){
                            break;
                        }
                        else if (!input.equalsIgnoreCase("n")) {
                            Book b = msg.getBook(input);
                            if(!b.getISBN().equals("0000000000")) {
                                System.out.println("Enter Quantity: ");
                                int qty = keyboard.nextInt();
                                if(qty < 1) {
                                    System.out.println("Invalid quantity.");
                                } else {
                                    msg.addToCart(new CartEntry(currentUser, b.getISBN(), qty));
                                }
                            } else {
                                System.out.println("Invalid ISBN.");
                            }
                        } else {
                            currentPage++;
                        }
                    }
                }
            }
            else if(choice == 2){
                System.out.println("\nEnter the book's title: ");
                String title = keyboard.next();
                List<Book> books = msg.getBooksByQuery("title", title);
        
                System.out.println("There are " + books.size() + " books matching your query.");

                if(books.size() > 0) {
                    int currentIdx = 0;
                    int currentPage = 1;
                    while (true){
                        while(currentIdx < books.size() && currentIdx < currentPage * 2) {
                            Book b = books.get(currentIdx);
                            System.out.println("Author: " + b.getAuthor());
                            System.out.println("Title: " + b.getTitle());
                            System.out.println("ISBN: " + b.getISBN());
                            System.out.println("Price: " + b.getPrice());
                            System.out.println("Subject: " + b.getSubject() + "\n");
                            currentIdx++;
                        }

                        System.out.println("Enter ISBN to add to cart or\n" +
                                            "n ENTER to browse or\n" +
                                            "ENTER to go back to menu:\n");
                        input = keyboard.nextLine();
                        if (input.equals("")){
                            break;
                        }
                        else if (!input.equalsIgnoreCase("n")) {
                            Book b = msg.getBook(input);
                            if(!b.getISBN().equals("0000000000")) {
                                System.out.println("Enter Quantity: ");
                                int qty = keyboard.nextInt();
                                if(qty < 1) {
                                    System.out.println("Invalid quantity.");
                                } else {
                                    msg.addToCart(new CartEntry(currentUser, b.getISBN(), qty));
                                }
                            } else {
                                System.out.println("Invalid ISBN.");
                            }
                        } else {
                            currentPage++;
                        }
                    }
                }
            }
            else if(choice != 3){
                System.out.println("Invalid input\n")
            } 
        }while(choice != 3);
    }

    public static void viewEditCart(){
        Scanner keyboard = new Scanner(System.in);
        while(true) {
            List<String> cartISBNs = getAssociatedCartEntryISBNs(currentUser);
            double total = 0.0;

            System.out.println("Current Cart Contents:                                                                                  ");
            System.out.println("");
            System.out.println("ISBN          Title                                                                      $  Qty    Total");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            //book info
            for(String isbn : cartISBNs) {
                CartEntry myCart = msg.getCartEntry(currentUser, isbn);
                Book currBook = msg.getBook(isbn);
                total += currBook.getPrice() * myCart.getQty();
                System.out.println(myCart.toString(currBook.getPrice(), currBook.getTitle()));
            }
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.printf("Total =                                                                                              $%d\n", total);
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("");

            System.out.println("Enter d to delete item");
            System.out.println("e to edit cart or");
            System.out.println("q to go back to Menu: ");
            String choice = keyboard.nextLine();
            if (choice.equalsIgnoreCase("e")) {
                System.out.println("Enter ISBN of the item: ");
                String isbn = keyboard.nextInt();
                System.out.println("Enter new Quantity: ");
                int quantity = keyboard.nextInt();
                msg.editCartEntry(currentUser, isbn, newQty);
                System.out.println("Edit Item Completed");
            } else if (choice.equalsIgnoreCase("d")){
                System.out.println("Enter ISBN of the item: ");
                String isbn = keyboard.nextInt();
                msg.editCartEntry(currentUser, isbn, 0);
                System.out.println("Delete Item Completed");
            } else if (choice.equalsIgnoreCase("q")) {
                retur;
            }
        }
    }

    public static void checkOrderStatus(){
        Scanner keyboard = new Scanner(System.in);

        System.out.printf("Orders placed by %s %s                                                                                  \n", msg.getMember(currentUser).getFirstName(), msg.getMember(currentUser).getLastName());
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("ORDER NO    RECEIVED DATE                                                             SHIPPED DATE      ");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        List<Integer> orders = msg.getAssociatedOrders(currentUser);
        for (Integer order : orders){
            Order currOrder = msg.getOrder(order);
            System.out.printf("%d          %s                                                                  %s          \n", order.getOrderNum(), order.getOrderDate().toString(), order.getShipDate().toString());
        }
        
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");
        while (true){
            System.out.println("Enter the Order No to diplay its details or (q) to quit: ");
            String choice = keyboard.nextLine();
            if (choice.equalsIgnoreCase("q")){
                return;
            }
            else{
                System.out.println(msg.getOrder(Integer.valueOf(choice)).toString());

                List<String> orderISBNs = msg.getAssociatedOrderDetailISBNs(Integer.valueOf(choice));
                double total = 0.0;
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.println("ISBN          Title                                                                      $  Qty    Total");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                //book info
                for(String isbn : orderISBNs) {
                    OrderDetails ode = msg.getOrderDetail(Integer.valueOf(choice), isbn);
                    Book book = msg.getBook(isbn);
                    total += book.getPrice() * ode.getQty();
                    System.out.println(ode.toString(currBook.getTitle()));
                }
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("Total =                                                                                              $%d\n", total);
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.println("");
                                
            }
        }
    }

    public static void checkout(){
        Scanner keyboard = new Scanner(System.in);
        double total = 0.0;
        List<String> cartISBNs = getAssociatedCartEntryISBNs(currentUser);

        System.out.println("Current Cart Contents:                                                                                  ");
        System.out.println("");
        System.out.println("ISBN          Title                                                                      $  Qty    Total");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //book info
        for(String isbn : cartISBNs) {
            CartEntry myCart = msg.getCartEntry(currentUser, isbn);
            Book currBook = msg.getBook(isbn);
            total += currBook.getPrice() * myCart.getQty();
            System.out.println(myCart.toString(currBook.getPrice(), currBook.getTitle()));
        }
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("Total =                                                                                              $%d\n", total);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");

        System.out.println("Proceed to checkout (Y/N): ");
        String choice = keyboard.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            System.out.println("Do you want to enter a new shipping address (Y/N): ");
            choice = keyboard.nextLine();
            if (choice.equalsIgnoreCase("y")){
                System.out.println("Enter first name: ");
                String fName = keyboard.nextLine();
                System.out.println("Enter last name: ");
                String lName = keyboard.nextLine();
                System.out.println("Enter street address: ");
                String addr = keyboard.nextLine();
                System.out.println("Enter city: ");
                String city = keyboard.nextLine();
                System.out.println("Enter state: ");
                String state = keyboard.nextLine();
                System.out.println("Enter zip: ");
                int zip = keyboard.nextInt();

                Person currUser = msg.getMember(currentUser);
                currUser.updateFirstName(fName);
                currUser.updateLastName(lName);
                currUser.updateAddr(addr);
                currUser.updateCity(city);
                currUser.updateState(state);
                msg.updateMember(currUser);
            }
            System.out.println("Do you want to enter a new CreditCard number (Y/N): ");
            choice = keyboard.nextLine();
            
            String ccType = "";
            String ccNum = "0000000000000000";

            if (choice.equalsIgnoreCase("y")){
                while (true) {
                    System.out.println("Enter type of Credit Card (amex/discover/mc/visa): ");
                    ccType = keyboard.nextLine().toLowerCase();
                    if(ccType.equals("amex") || ccType.equals("discover") || ccType.equals("mc") || ccType.equals("visa")) {
                        break;
                    }
                }

                if (ccType.equals("amex"))
                {
                    do{
                        System.out.println("Enter Credit Card Number: ");
                        ccNum = keyboard.nextLine();

                        if(ccNum.length() != 15)
                        {
                            System.out.println("Invalid Entry.");
                        }
                    }while(ccNum.length() != 15);
                } else {
                    do{
                        System.out.println("Enter Credit Card Number: ");
                        ccNum = keyboard.nextLine();

                        if(ccNum.length() != 16)
                        {
                            System.out.println("Invalid Entry.");
                        }
                    }while(ccNum.length() != 16);
                }

                Person currUser = msg.getMember(currentUser);
                currUser.updateCCType(ccType);
                currUser.updateCCNum(ccNum);
                msg.updateMember(currUser);
            }
            
            int currOrderNumber = msg.currentOrderNum();
            Person currUser = msg.getMember(currentUser);
            Book currBook;
            CartEntry currCart;
            OrderDetails currOrderDetails;
            Order currOrder = new Order(currentUser, currOrderNumber, currUser.getAddr(), currUser.getCity(), currUser.getState(), currUser.getZip());
            for (String isbn : cartISBNs) {
                currBook = msg.getBook(isbn);
                currCart = msg.getCartEntry(currentUser, isbn);
                currOrderDetails = new OrderDetails(currOrderNumber, isbn, currCart.getQty(), currBook.getPrice());
                msg.addOrderDetail(currOrderDetails);
            }
            msg.addOrder(currOrder);
            
            System.out.printf("                              Invoice for Order No.%d                                                  \n", orderNum);
            System.out.println("            Shipping Address                               Billing Address                              ");
            System.out.printf("            Name: %s                                       Name: %s                                     \n", fname, fname);
            System.out.printf("            Address: %s                                    Address: %s                                  \n", addr, addr);
            System.out.printf("                     %s                                             %s                                  \n", city, city);
            System.out.printf("                     %s                                             %s                                  \n", state, state);
            System.out.printf("                     %d                                             %d                                  \n", zip, zip);
            System.out.println("");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("ISBN          Title                                                                      $  Qty    Total");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            //book info
            for(String isbn : cartISBNs) {
                CartEntry myCart = msg.getCartEntry(currentUser, isbn);
                Book currBook = msg.getBook(isbn);
                System.out.println(myCart.toString(currBook.getPrice(), currBook.getTitle()));
            }
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.printf("Total =                                                                                              $%d\n", total);
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("Press enter to go back to Menu");

            msg.clearCart(currentUser);
        }
        return;
    }

    public static void oneClickCheckout(){
        double total = 0.0;

        System.out.printf("                              Invoice for Order No. %s                                                  \n", orderNum);
        System.out.println("            Shipping Address                               Billing Address                              ");
        System.out.printf("            Name: %s                                       Name: %s                                     \n", fname, fname);
        System.out.printf("            Address: %s                                    Address: %s                                  \n", addr, addr);
        System.out.printf("                     %s                                             %s                                  \n", city, city);
        System.out.printf("                     %s                                             %s                                  \n", state, state);
        System.out.printf("                     %d                                             %d                                  \n", zip, zip);
        System.out.println("");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("ISBN          Title                                                                      $  Qty    Total");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //book info
        List<String> cartISBNs = getAssociatedCartEntryISBNs(currentUser);
        for(String isbn : cartISBNs) {
            CartEntry myCart = msg.getCartEntry(currentUser, isbn);
            Book currBook = msg.getBook(isbn);
            total += currBook.getPrice() * myCart.getQty();
            System.out.println(myCart.toString(currBook.getPrice(), currBook.getTitle()));
        }
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("Total =                                                                                              $%d\n", total);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");

        int currOrderNumber = msg.currentOrderNum();
        Person currUser = msg.getMember(currentUser);
        Book currBook;
        CartEntry currCart;
        OrderDetails currOrderDetails;
        Order currOrder = new Order(currentUser, currOrderNumber, currUser.getAddr(), currUser.getCity(), currUser.getState(), currUser.getZip());
        for (String isbn : cartISBNs) {
            currBook = msg.getBook(isbn);
            currCart = msg.getCartEntry(currentUser, isbn);
            currOrderDetails = new OrderDetails(currOrderNumber, isbn, currCart.getQty(), currBook.getPrice());
            msg.addOrderDetail(currOrderDetails);
        }
        msg.addOrder(currOrder);
        msg.clearCart(currentUser);
        return;
    }

    public static void viewEditInfo(){
        Scanner keyboard = new Scanner(System.in);
        while (true){
            System.out.println("1: View information.");
            System.out.println("2: Edit information.");
            System.out.println("q: Quit.");
            System.out.println("Type your option: ");
            Person member;
            String choice = keyboard.nextLine()[0];
            switch (choice){
                case "1":
                    System.out.println("Enter UID: ");
                    String uid = keyboard.nextLine().replaceAll("\n", "");
                    msg.getMember(uid);
                    System.out.printf("Name:%-30.30s %s\n", member.getFirstName(), member.getLastName());
                    System.out.printf("Address:%-30.30s\n", member.getAddr());
                    System.out.printf("City:%-30.30s, %s %d\n", member.getCity(), member.getState(), mmeber.getZip());
                    System.out.printf("Phone:%-30.30s\n", member.getPhone());
                    System.out.printf("Email:%-30.30s\n", member.getEmail());
                    System.out.printf("UserID:%-30.30s\n", member.getUID());
                    System.out.printf("Password:%-30.30s\n", member.getPass());
                    System.out.printf("CreditCard Type:%-30.30s\n", member.getCCType());
                    System.out.printf("CreditCard Number:%-30.30s\n", member.getCCNum());
                    System.out.println("\n");
                    break;
                case "2":
                    System.out.println("Enter first name: ");
                    member.updateFirstName(keyboard.nextLine());
                    System.out.println("Enter last name: ");
                    member.updateLastName(keyboard.nextLine());
                    System.out.println("Enter street address: ");
                    member.updateAddr(keyboard.nextLine());
                    System.out.println("Enter city: ");
                    member.updateCity(keyboard.nextLine());
                    System.out.println("Enter state: ");
                    member.updateState(keyboard.nextLine());
                    System.out.println("Enter zip: ");
                    member.updateZip(keyboard.nextInt());
                    System.out.println("Enter phone: ");
                    member.updatePhone(keyboard.nextLine());
                    System.out.println("Enter email address: ");
                    member.updateEmail(keyboard.nextLine());
                    System.out.println("Enter password: ");
                    member.updatePass(keyboard.nextLine());
                    System.out.println("Do you wish to update credit card information (y/n): ");
                    String option = keyboard.nextLine();

                    String ccType = "None";
                    String ccNum = "0000000000000000";
                    if(option.equals('y')) {
                        while (true) {
                            System.out.println("Enter type of Credit Card (amex/discover/mc/visa): ");
                            ccType = keyboard.nextLine().toLowerCase();
                            if(ccType.equals("amex") || ccType.equals("discover") || ccType.equals("mc") || ccType.equals("visa")) {
                                break;
                            }
                        }

                        if (ccType.equals("amex"))
                        {
                            do{
                                System.out.println("Enter Credit Card Number: ");
                                ccNum = keyboard.nextLine();

                                if(ccNum.length() != 15)
                                {
                                    System.out.println("Invalid Entry.");
                                }
                            }while(ccNum.length() != 15);
                        } else {
                            do{
                                System.out.println("Enter Credit Card Number: ");
                                ccNum = keyboard.nextLine();

                                if(ccNum.length() != 16)
                                {
                                    System.out.println("Invalid Entry.");
                                }
                            }while(ccNum.length() != 16);
                        }

                        member.updateCCType(ccType);
                        member.updateCCNum(ccNum);

                    }

                    msg.updateMember(member);
                    
                    System.out.printf("Name:%-30.30s %s\n", member.getFirstName(), member.getLastName());
                    System.out.printf("Address:%-30.30s\n", member.getAddr());
                    System.out.printf("City:%-30.30s, %s %d\n", member.getCity(), member.getState(), mmeber.getZip());
                    System.out.printf("Phone:%-30.30s\n", member.getPhone());
                    System.out.printf("Email:%-30.30s\n", member.getEmail());
                    System.out.printf("UserID:%-30.30s\n", member.getUID());
                    System.out.printf("Password:%-30.30s\n", member.getPass());
                    System.out.printf("CreditCard Type:%-30.30s\n", member.getCCType());
                    System.out.printf("CreditCard Number:%-30.30s\n", member.getCCNum());
                    System.out.println("\n");
                    break;
                case "q":
                    return;
            }
            
        }
        
    }

    public static void printMainMenu(){
        System.out.println("********************************************************************************************************");
        System.out.println("***                                   1. Member Login                                                ***");
        System.out.println("***                                   2. New Member Registration                                     ***");
        System.out.println("***                                   q. Quit                                                        ***");
        System.out.println("********************************************************************************************************");
        System.out.println("");
        System.out.println("Type your option: ");
    }
    
    public static void printBanner(){
        System.out.println("********************************************************************************************************");
        System.out.println("***                                                                                                  ***");
        System.out.println("***                              Welcome to the Online Book Store                                    ***");
        System.out.println("***                                                                                                  ***");
        System.out.println("********************************************************************************************************");
    }

    public static void printLoggedInOptions(){
        System.out.println("                                      1. Browse by Subject");
        System.out.println("                                      2. Search by Author/Title/Subject");
        System.out.println("                                      3. View/Edit Shopping CartEntry");
        System.out.println("                                      4. Check Order Status");
        System.out.println("                                      5. Check Out");
        System.out.println("                                      6. One Click Check Out");
        System.out.println("                                      7. View/Edit Personal Information");
        System.out.println("                                      8. Logout");
        System.out.println("Type your option: ");
    }

    public static void loggedIn(String userid){
        currentUser = userid;
        Scanner keyboard = new Scanner(System.in);
        while(true){
            printBanner();
            printLoggedInOptions();
            String choice = keyboard.nextLine();
            switch (choice.charAt(0)){
                case '1':
                    browseBySubject();
                    break;
                case '2':
                    search();
                    break;
                case '3':
                    viewEditCart();
                    break;
                case '4':
                    checkOrderStatus();
                    break;
                case '5':
                    checkout();
                    break;
                case '6':
                    oneClickCheckout();
                    break;
                case '7':
                    viewEditInfo();
                    break;
                case '8':
                currentUser = "null";
                    return;
                default:
                    System.out.println("Choose a valid option.");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
            }
            
        }
    }

    public static void main(String[] args) {
        mysqlSetup();
        msg = new MySQLSetterGetter();
        msg.addBook(new Book("0000000000", "fruit", "hello world", 420.69, "Tough Reads"));
        msg.addBook(new Book("0000000001", "robert", "loader instructions", 100.00, "Documentation"));
        msg.addBook(new Book("0000000002", "ellie", "how to retire early", 9999.99, "Memoir"));
        msg.addBook(new Book("0000000003", "james", "boot boot boot boot", 0.01, "Memoir"));
        msg.addBook(new Book("0000000004", "james", "robert doesn't know what a boot is ???", 0.02, "Memoir"));
        msg.addBook(new Book("0000000005", "mitch", "the ABCs of deleting other people's essay sections", 50.00, "Documentation"));
        msg.addBook(new Book("0000000006", "fruit", "how i became a god programmer", 1337.69, "Memoir"));
        msg.addBook(new Book("0000000007", "ellie", "god has left us", 10.00, "VMNames"));
        msg.addBook(new Book("0000000008", "ellie", "drop kick me through the academic field goal of success", 10.00, "VMNames"));
        msg.addBook(new Book("0000000009", "ellie", "you are not a clown your are the entire circus: an autobiography", 10.00, "VMNames"));
        msg.addBook(new Book("0000000010", "ellie", "what up i'm jared 19", 10.00, "VMNames"));
        msg.addBook(new Book("0000000011", "ellie", "wake me up", 10.00, "VMNames"));
        msg.addBook(new Book("0000000012", "ellie", "i can't wake up", 10.00, "VMNames"));
        msg.addBook(new Book("0000000013", "james", "cat in the hat: shakespeare's greatest work", 10101.10, "Tough Reads"));

        Scanner keyboard = new Scanner(System.in);
        boolean authenticated;
        while(true){
            printBanner();
            printMainMenu();
            String choice = keyboard.nextLine();
            switch (choice.charAt(0)) {
                case '1':
                    System.out.println("Enter userID: ");
                    String userid = keyboard.nextLine();
                    System.out.println("Enter password: ");
                    String password = keyboard.nextLine();

                    if(memberLogin(userid, password)){
                        loggedIn(userid);
                        System.out.println("You have successfully logged out.");
                        return;
                    }
                    else{
                        System.out.println("Unauthorized user.");
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    break;
                case '2':
                    newMemberRegistration();
                    break;
                case 'q':
                    return;
                default:
                    System.out.println("Choose a valid option.");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
            }
        }
    }
}
