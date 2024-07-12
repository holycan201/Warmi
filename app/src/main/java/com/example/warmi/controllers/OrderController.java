package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.models.orders.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private static final String DATABASE_REFERENCE = "orders";
    DatabaseReference orderRef;

    public OrderController() {
        this.orderRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addOrder(OrderItems orderItems, DataInterface.DataStatus dataStatus) {
        String orderId = orderRef.push().getKey();
        if (orderId != null) {
            orderItems.setId(orderId);
            orderRef.child(orderId).setValue(orderItems)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        }
    }

    public void fetchOrder(FetchCallback.FetchOrderCallback callback) {
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<OrderItems> orderItemsList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderItems orderItems = snapshot.getValue(OrderItems.class);
                    orderItemsList.add(orderItems);
                }

                OrderModel orderModel = new OrderModel(orderItemsList);

                callback.onOrderFetched(orderModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }
}
