import java.util.HashMap;
import java.time.LocalDateTime;    

public class Order {
    private LocalDateTime orderDate, shipDate;
    private HashMap<String, Integer> books;
    private int orderNum;

    public Order(HashMap<String, Integer> books, int orderNum) {
        orderDate = LocalDateTime.now();  
        shipDate = orderDate.plusDays(2);

        this.books = books;
    }

    public HashMap<String, Integer> getBooks() {
        return books;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }
}