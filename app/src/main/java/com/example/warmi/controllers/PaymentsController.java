package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.PaymentsModel;
import com.example.warmi.models.TransactionsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentsController {
    private static final String DATABASE_REFERENCE = "payments";
    DatabaseReference paymentRef;

    public PaymentsController() {
        this.paymentRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addPayments(PaymentsModel paymentsModel, DataInterface.DataStatus dataStatus) {
        String paymentId = paymentRef.push().getKey();
        if (paymentId != null) {
            paymentRef.child(paymentId).setValue(paymentsModel)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        } else {
            dataStatus.DataOperationFailed(new Exception("Failed to generate unique key"));
        }
    }

    public void fetchPayments(FetchCallback.FetchPaymentCallback callback) {
        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String paymentId = snapshot.getKey();
                    PaymentsModel paymentsModel = snapshot.getValue(PaymentsModel.class);
                    if (paymentsModel != null) {
                        paymentsModel.setId(paymentId);
                        callback.onPaymentFetched(paymentsModel);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }
}
