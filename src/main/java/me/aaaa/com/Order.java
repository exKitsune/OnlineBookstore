package me.aaaa.com;

import java.sql.Date;
import java.util.HashMap;
import java.time.LocalDateTime;    

public class Order {
    private Date orderDate, shipDate;
    private String userid, addr, city, state;
    private int orderNum, zip;

    public Order(String userid, int orderNum, String addr, String city, String state, int zip) {
        LocalDateTime p_orderDate = LocalDateTime.now();
        LocalDateTime p_shipDate = p_orderDate.plusDays(2);

        orderDate = Date.valueOf(p_orderDate.toLocalDate());
        shipDate = Date.valueOf(p_shipDate.toLocalDate());


        this.userid = userid;
        this.orderNum = orderNum;
        this.addr = addr;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Order(String userid, int orderNum, Date orderDate, Date shipDate, String addr, String city, String state, int zip) {
        this.userid = userid;
        this.orderNum = orderNum;
        this.orderDate = orderDate;
        this.shipDate = shipDate;
        this.addr = addr;
        this.city = city;
        this.state = state;
        this.zip = zip;

    }

    public Order() {}

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public String getUserid() {
        return userid;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getAddr() {
        return addr;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }

    public String toString() {
        return String.format("Order Number: %d\nAddress: %s\nCity: %s\nState: %s\nZip: %d\nOrder Date: %s\nShip Date: %s", orderNum, addr, city, state, zip, orderDate.toString(), shipDate.toString());
    }
}