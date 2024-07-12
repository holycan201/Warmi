package com.example.warmi.models;

import java.lang.reflect.Array;

public class TransactionsModel {
    private String id;
    private String user_id;
    private String address_id;
    private String payment_id;
    private Array products;
    private boolean status;

    public TransactionsModel() {
    }

    public TransactionsModel(String id, String user_id, String address_id, String payment_id, Array products, boolean status) {
        this.user_id = user_id;
        this.id = id;
        this.address_id = address_id;
        this.payment_id = payment_id;
        this.products = products;
        this.status = status;
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

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public Array getProducts() {
        return products;
    }

    public void setProducts(Array products) {
        this.products = products;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class PaymentsModel {
        private String id;
        private String payment_methode_id;
        private String proof_image;
        private boolean status;
        private boolean approval;

        public PaymentsModel() {
        }

        public PaymentsModel(String id, String payment_methode_id, String proof_image, boolean status, boolean approval) {
            this.id = id;
            this.payment_methode_id = payment_methode_id;
            this.proof_image = proof_image;
            this.status = status;
            this.approval = approval;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPayment_methode_id() {
            return payment_methode_id;
        }

        public void setPayment_methode_id(String payment_methode_id) {
            this.payment_methode_id = payment_methode_id;
        }

        public String getProof_image() {
            return proof_image;
        }

        public void setProof_image(String proof_image) {
            this.proof_image = proof_image;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public boolean isApproval() {
            return approval;
        }

        public void setApproval(boolean approval) {
            this.approval = approval;
        }

    }
}
