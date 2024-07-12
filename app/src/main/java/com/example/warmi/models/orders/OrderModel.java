package com.example.warmi.models.orders;

import com.example.warmi.models.products.ProductItems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderModel implements Serializable {
    private List<OrderItems> orderItems;

    public OrderModel() {
        this.orderItems = new ArrayList<>();
    }

    public OrderModel(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }
}
