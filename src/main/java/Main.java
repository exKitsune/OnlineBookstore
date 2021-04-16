import java.io.IOException;
import java.net.URL;
import java.sql.*;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.HashMap;
import java.util.List;
import Thread;

import Person;
import Book;
import Order;

public class Main {
    private static Connection connection;
    public static String host, database, username, password, table;
    public static int port;
    MySQLSetterGetter msg;

    public static HashMap<String, Integer> usercart;
    public static List<Order> orders;

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
        char option = keyboard.nextLine();

        //TODO: Add if/else statement for registration success/failure

        if(option == 'y')
        {
            System.out.println("Enter type of Credit Card(amex/visa): ");
            String ccType = keyboard.nextLine();

            if (ccType == 'amex') 
            {
                do{
                    System.out.println("Enter Credit Card Number: ");
                    int ccNum = keyboard.nextLine();
                    int ccNumLength = String.valueOf(ccNum).length();

                    if(ccNumLength != 15)
                    {
                        System.out.println("Invalid Entry.");
                    }
                }while(ccNumLength != 15);
            }

            if (ccType == 'visa') 
            {
                do{
                    System.out.println("Enter Credit Card Number: ");
                    int ccNum = keyboard.nextLine();
                    int ccNumLength = String.valueOf(ccNum).length();

                    if(ccNumLength != 16)
                    {
                        System.out.println("Invalid Entry.");
                    }
                }while(ccNumLength != 16);
            }
            msg.addMember(fName, lName, addr, city, state, zip, phone, email, uid, pass, ccType, ccNum);
        }
        else if(option == 'n'){
            String ccType = 'None'
            int ccNum = 0000000000000000
            msg.addMember(fName, lName, addr, city, state, zip, phone, email, uid, pass, ccType, ccNum);
        }
        
        System.out.printf("You have registered successfully.");
        System.out.printf("Name:%-30.30s %s", fName, lName);
        System.out.printf("Address:%-30.30s", addr);
        System.out.printf("City:%-30.30s, %s %d", city, state, zip);
        System.out.printf("Phone:%-30.30s", phone);
        System.out.printf("Email:%-30.30s", email);
        System.out.printf("UserID:%-30.30s", uid);
        System.out.printf("Password:%-30.30s", pass);
        System.out.printf("CreditCard Type:%-30.30s", ccType);
        System.out.printf("CreditCard Number:%-30.30s", ccNum); 
        System.out.println("\n");          
    }
    
    public static boolean memberLogin(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter userID: ");
        String userid = keyboard.nextLine();
        System.out.println("Enter password: ");
        String password = keyboard.nextLine();

        //verify user login info
        return msg.memberCheck(userid, password);
    }

    public static void browseBySubject(){
        ResultSet res = msg.listSubjects();
        
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter your choice: ");
        String choice = keyboard.nextLine();
        int quantity;

        if (res.getArray("subject").length < Integer.parseInt(choice[0])){
            msg.listSubject(res.getArray("subject")[Integer.parseInt(choice[0])]);
        }
        
        while (true){
            System.out.println("Enter ISBN to add to cart or ENTER to return to member menu.");
            choice = keyboard.nextLine();
            if (choice.equals("\n")){
                break;
            }
            else{
                System.out.println("Enter Quantity: ");
                quantity = keyboard.nextInt();
                addBookToCart(choice, quantity);
            }
        }
    }

    public static void search(){
        Scanner keyboard = new Scanner(System.in);
        //ResultSet res = msg.listBooks();
        do{
            System.out.println("1. Author Search")
            System.out.println("2. Title Search")
            System.out.println("3. Go back to Member Menu\n")
            char choice = keyboard.nextInt();
            
            if(choice == 1)
            {
                System.out.println("\nEnter the Author's name: ");
                String author = keyboard.next();
                ResultSet countAuthors = msg.searchBooks(choice, author);
                if(countAuthors > 0){
                    System.out.println("Enter ISBN to add to cart or" +
                                        "\n\t\tEnter to browse or" +
                                        "\n\t\t\'n\' to return to menu: ");
                    char addToCart = keyboard.next();

                    if(addToCart == '\r'){
                        break; 
                    }
                    else if(addToCart == 'n'){
                        return;
                    }
                    else{
                        System.out.println("Enter quantity: ");
                        int quantity = keyboard.nextInt();
                        addBookToCart(addToCart, quantity);
                    }                    
                }
            }
            else if(choice == 2){
                System.out.println("\nEnter the title of the book: ");
                String title = keyboard.next();
                ResultSet countTitle = msg.searchBooks(choice, title);
                if(countTitle > 0){
                    System.out.println("Enter ISBN to add to cart or" +
                                        "\n\t\tEnter to browse or" +
                                        "\n\t\t\'n\' to return to menu: ");
                    char addToCart = keyboard.next();

                    if(addToCart == '\r'){
                        break; 
                    }
                    else if(addToCart == 'n'){
                        return;
                    }
                    else{
                        System.out.println("Enter quantity: ");
                        int quantity = keyboard.nextInt();
                        addBookToCart(addToCart, quantity);
                    }                    
                }
            }
            else if(choice != 3){
                System.out.println("Invalid input\n")
            } 
        }while(choice != 3);
    }

    public static void printCartInfo() {
        Iterator it = usercart.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Book book = msg.getBookDetails(pair.getKey()); // get book from ISBN
            int quantity = pair.getValue();
            System.out.println(book.getISBN() + "\t" + book.getTitle() + "\t" + book.getPrice() + "\t" + quantity + "\t" + book.getPrice() * quantity);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static void viewEditCart(){
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Current Cart Contents:                                                                                  ");
        System.out.println("");
        System.out.println("ISBN          Title                                                                      $  Qty    Total");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //book info
        printCartInfo();
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Total =                                                                                              $%d", total);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");

        System.out.println("Enter d to delete item");
        System.out.println("e to edit cart or");
        System.out.println("q to go back to Menu: ");
        String choice = keyboard.nextLine();
        if (choice == 'e'){
            System.out.println("Enter ISBN of the item: ");
            int isbn = keyboard.nextInt();
            System.out.println("Enter new Quantity: ");
            int quantity = keyboard.nextInt();
            System.out.println("Edit Item Completed");
        }
        else if (choice == 'd'){
            System.out.println("Enter ISBN of the item: ");
            int isbn = keyboard.nextInt();
            System.out.println("Delete Item Completed");
        }
        System.out.println("Press enter to go back to Menu");
        return;
    }

    public static void addBookToCart(String isbn, int quantity){
        usercart.put(isbn, usercart.getOrDefault(isbn, 0) + quantity);
    }

    public static void checkOrderStatus(){
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Orders placed by %s %s                                                                                  ", fname, lname);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("ORDER NO    RECEIVED DATE                                                             SHIPPED DATE      ");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("%d          %d-%d-%d                                                                  %d-%d-%d          ");// ono, received, shipped
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("Enter the Order No to diplay its details or (q) to quit: ");//maybe lets just quit
        String choice = keyboard.nextLine();
        if (choice =='q'){
            return;
        }
    }

    public static void checkout(){
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Current Cart Contents:                                                                                  ");
        System.out.println("");
        System.out.println("ISBN          Title                                                                      $  Qty    Total");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //book info
        printCartInfo();
        System.out.println("--------------------------------------------------------------------------------------------------------");
        int total; // add book totals
        System.out.println("Total =                                                                                              $%d", total);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");

        System.out.println("Proceed to checkout (Y/N): ");
        String choice = keyboard.nextLine();
        if ((choice == 'y') || (choice == 'Y')){
            System.out.println("do you want to enter a new shipping address (Y/N): ");
            String choice = keyboard.nextLine();
            if ((choice = 'y') || (choice = 'Y')){
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
            }
            System.out.println("Do you want to enter a new CreditCard number (Y/N): ");
            String choice = keyboard.nextLine();
            if ((choice = 'y') || (choice = 'Y')){
                System.out.println("Enter Credit Card Number: ");
                int ccNum = keyboard.nextLine();
                int ccNumLength = String.valueOf(ccNum).length();

                    if(ccNumLength != 15)
                    {
                        System.out.println("Invalid Entry.");
                    }
                }while(ccNumLength != 15);
            }
            System.out.println("                              Invoice for Order No.666                                                  ");
            System.out.println("            Shipping Address                               Billing Address                              ");
            System.out.println("            Name: %s                                       Name: %s                                     ", fname, fname);
            System.out.println("            Address: %s                                    Address: %s                                  ", addr, addr);
            System.out.println("                     %s                                             %s                                  ", city, city);
            System.out.println("                     %s                                             %s                                  ", state, state);
            System.out.println("                     %d                                             %d                                  ", zip, zip);
            System.out.println("");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("ISBN          Title                                                                      $  Qty    Total");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            //book info
            printCartInfo();
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("Total =                                                                                              $%d", total);
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("Press enter to go back to Menu");
        }
        else{
            //return to menu 
            return;
        }
        return;
    }

    public static void oneClickCheckout(){

        System.out.println("                              Invoice for Order No.666                                                  ");
        System.out.println("            Shipping Address                               Billing Address                              ");
        System.out.println("            Name: %s                                       Name: %s                                     ", fname, fname);
        System.out.println("            Address: %s                                    Address: %s                                  ", addr, addr);
        System.out.println("                     %s                                             %s                                  ", city, city);
        System.out.println("                     %s                                             %s                                  ", state, state);
        System.out.println("                     %d                                             %d                                  ", zip, zip);
        System.out.println("");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("ISBN          Title                                                                      $  Qty    Total");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //book info
        printCartInfo();
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Total =                                                                                              $%d", total);
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("Press enter to go back to Menu");
        return;
    }

    public static void viewEditInfo(){

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
        System.out.println("                                      3. View/Edit Shopping Cart");
        System.out.println("                                      4. Check Order Status");
        System.out.println("                                      5. Check Out");
        System.out.println("                                      6. One Click Check Out");
        System.out.println("                                      7. View/Edit Personal Information");
        System.out.println("                                      8. Logout");
        System.out.println("Type your option: ");
    }

    public static void loggedIn(){
        Scanner keyboard = new Scanner(System.in);
        while(True){
            printBanner();
            printLoggedInOptions();
            String choice = keyboard.nextLine();
            switch (choice[0]){
                case "1":
                    browseBySubject();
                    break;
                case "2":
                    search();
                    break;
                case "3":
                    viewEditCart();
                    break;
                case "4":
                    checkOrderStatus();
                    break;
                case "5":
                    checkout();
                    break;
                case "6":
                    oneClickCheckout();
                    break;
                case "7":
                    viewEditInfo();
                    break;
                case "8":
                    return;
                default:
                    System.out.println("Choose a valid option.");
                    Thread.sleep(1000);
            }
            
        }
    }

    public static void main(String[] args) {
        mysqlSetup();
        msg = new MySQLSetterGetter();
        msg.addBook("0", "fruit", "hello world", 420.69, "Tough Reads");
        msg.addBook("1", "robert", "loader instructions", 100.00, "Documentation");
        msg.addBook("2", "ellie", "how to retire early", 9999.99, "Memoir");
        msg.addBook("3", "james", "boot boot boot boot", 0.01, "Memoir");
        msg.addBook("4", "mitch", "the ABCs of deleting other people's essay sections", 50.00, "Documentation");
        msg.addBook("5", "fruit", "how i became a god programmer", 1337.69, "Memoir");
        msg.addBook("6", "ellie", "god has left us", 10.00, "VMNames");
        msg.addBook("7", "ellie", "drop kick me through the academic field goal of success", 10.00, "VMNames");
        msg.addBook("8", "ellie", "you are not a clown your are the entire circus: an autobiography", 10.00, "VMNames");
        msg.addBook("9", "ellie", "what up i'm jared 19", 10.00, "VMNames");
        msg.addBook("10", "ellie", "wake me up", 10.00, "VMNames");
        msg.addBook("11", "ellie", "i can't wake up", 10.00, "VMNames");
        msg.addBook("12", "james", "cat in the hat: shakespeare's greatest work", 10101010.10101010, "Tough Reads");

        Scanner keyboard = new Scanner(System.in);
        boolean authenticated;
        while(true){
            printBanner();
            printMainMenu();
            String choice = keyboard.nextLine();
            switch (choice[0]) {
                case "1":
                    authenticated = memberLogin();
                    if(authenticated){
                        loggedIn();
                        System.out.println("You have successfully logged out.");
                        return;
                    }
                    else{
                        system.out.println("Unauthorized user.");
                        Thread.sleep(1000);
                    }
                    break;
                case "2":
                    newMemberRegistration();
                    break;
                case "q":
                    return;
                default:
                    System.out.println("Choose a valid option.");
                    Thread.sleep(1000);
            }
        }


        return;
    }
}
