package me.aaaa.com;

public class OrderDetails {
    private int orderNum, qty;
    private double price;
    private String ISBN;

    public OrderDetails(int orderNum, String ISBN, int qty, double price) {
        this.orderNum = orderNum;
        this.ISBN = ISBN;
        this.qty = qty;
        this.price = price;
    }

    public OrderDetails() {}

    public int getOrderNum() {
        return orderNum;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }
}
