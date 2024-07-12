package com.example.warmi.models.payment_methode;


public class PaymentMethodeItems {
    private String id;
    private String bank_name;
    private String account_name;
    private String account_number;
    private String type;
    private String qr_image;

    public PaymentMethodeItems() {
    }

    public PaymentMethodeItems(String id, String bank_name, String account_name, String type, String account_number) {
        this.id = id;
        this.bank_name = bank_name;
        this.account_name = account_name;
        this.type = type;
        this.account_number = account_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQr_image() {
        return qr_image;
    }

    public void setQr_image(String qr_image) {
        this.qr_image = qr_image;
    }
}
