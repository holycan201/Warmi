package com.example.warmi.models.products;

public class ProductItems {
    private String id;
    private String name;
    private String desc;
    private String image;
    private String category;
    private String tag;
    private int price;

    public ProductItems() {
    }

    public ProductItems(String id, String name, String desc, String image, String category, String tag, int price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.category = category;
        this.tag = tag;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public int getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
