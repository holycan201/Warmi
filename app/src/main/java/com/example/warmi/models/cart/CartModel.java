package com.example.warmi.models.cart;

import java.util.ArrayList;
import java.util.List;

public class CartModel {
    private List<CartItems> items;

    public CartModel() {
    }

    public CartModel(List<CartItems> items) {
        this.items = items;
    }


    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }
}
