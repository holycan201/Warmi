package com.example.warmi.models;

public class PaymentsModel {
    private String id;
    private String order_id;
    private String payment_methode_id;
    private long limit;
    private int amount;
    private String status;
    public enum Status {
        Pending,
        Completed,
        Failed
    }

    public PaymentsModel() {
    }

    public PaymentsModel(String order_id, String payment_methode_id, long limit, int amount, Status status) {
        this.order_id = order_id;
        this.payment_methode_id = payment_methode_id;
        this.limit = limit;
        this.amount = amount;
        this.status = status.name();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_methode_id() {
        return payment_methode_id;
    }

    public void setPayment_methode_id(String payment_methode_id) {
        this.payment_methode_id = payment_methode_id;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
