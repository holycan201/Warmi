package com.example.warmi.models.payment_methode;


import java.util.ArrayList;
import java.util.List;

public class PaymentMethodeModel {
    private List<PaymentMethodeItems> paymentMethodeItems;

    public PaymentMethodeModel() {
        this.paymentMethodeItems = new ArrayList<>();
    }

    public PaymentMethodeModel(List<PaymentMethodeItems> paymentMethodeItems) {
        this.paymentMethodeItems = paymentMethodeItems;
    }

    public List<PaymentMethodeItems> getPaymentMethodeItems() {
        return paymentMethodeItems;
    }

    public void setPaymentMethodeItems(List<PaymentMethodeItems> paymentMethodeItems) {
        this.paymentMethodeItems = paymentMethodeItems;
    }
}
