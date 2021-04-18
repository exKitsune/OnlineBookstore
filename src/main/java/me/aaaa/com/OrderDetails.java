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

    public String toString(String title) {
        return String.format("%10s    %-60s      %,10.2f    %-5d %,-8.2f", ISBN, title, price, qty, qty*price);
    }
}
