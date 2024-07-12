package com.example.warmi.models;

public class UsersModel {
    private String user_uid;
    private String username;
    private String full_name;
    private String image;

    public UsersModel() {
    }

    public UsersModel(String username, String full_name, String image){
        this.username = username;
        this.full_name = full_name;
        this.image = image;
    }

    public String getUsername(){
        return username;
    }

    public String getFull_name(){
        return full_name;
    }

    public String getImage(){
        return image;
    }

    public void setUser_uid(){
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setFull_name(String full_name){
        this.full_name = full_name;
    }

    public void setImage(String image){
        this.image = image;
    }
}
