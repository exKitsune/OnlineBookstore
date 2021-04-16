
public class Book {
    private String ISBN, author, title, subject;
    private double price;

    public Book() {
        this.ISBN = "0000000000";
        this.author = "null";
        this.title = "null";
        this.price = 0;
        this.subject = "null";
    }

    public Book(String ISBN, String author, String title, double price, String subject) {
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.price = price;
        this.subject = subject;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getSubject() {
        return subject;
    }
}