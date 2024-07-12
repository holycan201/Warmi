package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.PaymentsModel;
import com.example.warmi.models.payment_methode.PaymentMethodeItems;
import com.example.warmi.models.payment_methode.PaymentMethodeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodeController {
    private static final String DATABASE_REFERENCE = "payment_methode";
    DatabaseReference paymentMethodeRef;

    public PaymentMethodeController() {
        this.paymentMethodeRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addPaymentMethode(PaymentMethodeItems paymentMethodeItems, DataInterface.DataStatus dataStatus) {
        String paymentMethodeId = paymentMethodeRef.push().getKey();
        if (paymentMethodeId != null) {
            paymentMethodeItems.setId(paymentMethodeId);
            paymentMethodeRef.child(paymentMethodeId).setValue(paymentMethodeItems)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        }
    }

    public void fetchPaymentMethode(FetchCallback.FetchPaymentMethodeCallback callback) {
        paymentMethodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PaymentMethodeItems> paymentMethodeItemsList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PaymentMethodeItems paymentMethodeItems = snapshot.getValue(PaymentMethodeItems.class);
                    paymentMethodeItemsList.add(paymentMethodeItems);
                }

                PaymentMethodeModel paymentMethodeModel = new PaymentMethodeModel(paymentMethodeItemsList);

                callback.onPaymentMethodeFetched(paymentMethodeModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void fetchPaymentsMethodeById(String paymentMethodeId, FetchCallback.FetchPaymentMethodeItemCallback callback) {
        paymentMethodeRef.child(paymentMethodeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PaymentMethodeItems paymentMethodeItems = dataSnapshot.getValue(PaymentMethodeItems.class);
                callback.onPaymentMethodeItemFetched(paymentMethodeItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }
}
