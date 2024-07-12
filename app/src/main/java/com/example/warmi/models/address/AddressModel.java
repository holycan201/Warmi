package com.example.warmi.models.address;

import com.example.warmi.models.cart.CartItems;

import java.util.List;

public class AddressModel {
    private List<AddressItems> items;

    public AddressModel() {
    }

    public AddressModel(List<AddressItems> items) {
        this.items = items;
    }


    public List<AddressItems> getItems() {
        return items;
    }

    public void setItems(List<AddressItems> items) {
        this.items = items;
    }
}
