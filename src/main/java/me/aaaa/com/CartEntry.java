package me.aaaa.com;

public class CartEntry {
    private String userid, ISBN;
    private int qty;

    public CartEntry(String userid, String ISBN, int qty) {
        this.userid = userid;
        this.ISBN = ISBN;
        this.qty = qty;
    }

    public CartEntry() {}

    public String getUserid() {
        return userid;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getQty() {
        return qty;
    }

    public String toString(double price, String title) {
        return String.format("%10s    %-60s      %,10.2f    %-5d %,-8.2f", ISBN, title, price, qty, qty*price);
    }
}