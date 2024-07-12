package com.example.warmi.models.cart;

public class CartItems {
    private String id;
    private String user_id;
    private String product_id;
    private int amount;

    public CartItems() {
    }

    public CartItems(String user_id, String product_id, int amount) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.amount = amount;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

