package com.example.warmi.models.orders;

import com.example.warmi.models.cart.CartItems;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderItems implements Serializable {
    private String id;
    private String user_id;
    private long timestamp;
    private int total_order;
    private List<CartItems> cartItems;;
    private String status;

    public enum Status {
        Belum_Bayar,
        Diproses,
        Dikirim,
        Selesai,
        Dibatalkan,
    };

    public OrderItems() {
    }

    public OrderItems(String user_id, long timestamp, int total_order, List<CartItems> cartItems, Status status) {
        this.user_id = user_id;
        this.cartItems = cartItems;
        this.timestamp = timestamp;
        this.total_order = total_order;
        this.status = status.name();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotal_order() {
        return total_order;
    }

    public void setTotal_order(int total_order) {
        this.total_order = total_order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        for (Status status : Status.values()) {
            statuses.add(status.name());
        }
        return statuses;
    }
}
