package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.TransactionsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionsController {
    private static final String DATABASE_REFERENCE = "transactions";
    DatabaseReference transactionsRef;

    public TransactionsController() {
        this.transactionsRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addTransactions(TransactionsModel transactionsModel, DataInterface.DataStatus dataStatus) {
        String id = transactionsRef.push().getKey();
        if (id != null) {
            transactionsRef.child(id).setValue(transactionsModel)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        } else {
            dataStatus.DataOperationFailed(new Exception("Failed to generate unique key"));
        }
    }

    public void fetchTransactions(FetchCallback.FetchTransactionsCallback callback) {
        transactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String transactionsId = snapshot.getKey();
                    TransactionsModel transactionsModel = snapshot.getValue(TransactionsModel.class);
                    if (transactionsModel != null) {
                        transactionsModel.setId(transactionsId);
                        callback.onTransactionsFetched(transactionsModel);
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

    public void fetchTransactionsById(String transactionsId, FetchCallback.FetchTransactionsItemCallback callback) {
        transactionsRef.child(transactionsId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TransactionsModel transactionsModel = dataSnapshot.getValue(TransactionsModel.class);
                callback.onTransactionsItemFetched(transactionsModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }


    public void updateTransaction(String transactionId, TransactionsModel updateTransaction, DataInterface.DataStatus dataStatus) {
        transactionsRef.child(transactionId).setValue(updateTransaction)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }
}