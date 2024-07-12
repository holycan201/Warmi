package com.example.warmi.models.products;

import java.util.ArrayList;
import java.util.List;

public class ProductsModel {
    private List<ProductItems> productList;

    public ProductsModel() {
        this.productList = new ArrayList<>();
    }

    public ProductsModel(List<ProductItems> productList) {
        this.productList = productList;
    }

    public List<ProductItems> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItems> productList) {
        this.productList = productList;
    }
}
