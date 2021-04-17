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

}