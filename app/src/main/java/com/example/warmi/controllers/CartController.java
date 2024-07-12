package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartController {
    private static final String DATABASE_REFERENCE = "cart";
    DatabaseReference cartRef;

    public CartController() {
        this.cartRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addCart(CartItems cartItems, DataInterface.DataStatus dataStatus) {
        String cartId = cartRef.push().getKey();
        if (cartId != null) {
            cartItems.setId(cartId);
            cartRef.child(cartId).setValue(cartItems)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        }
    }

    public void fetchCart(FetchCallback.FetchCartCallback callback) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CartItems> cartItemsList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItems cartItems = snapshot.getValue(CartItems.class);
                    cartItemsList.add(cartItems);
                }

                CartModel cartModel = new CartModel(cartItemsList);

                callback.onCartFetched(cartModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void fetchCartById(String cartId, FetchCallback.FetchCartItemCallback callback) {
        cartRef.child(cartId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CartItems cartItems = dataSnapshot.getValue(CartItems.class);
                callback.onCartItemFetched(cartItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }


    public void updateCart(String cartId, CartItems cartItems, DataInterface.DataStatus dataStatus) {
        cartRef.child(cartId).setValue(cartItems)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }


    public void deleteCart(String cartId, DataInterface.DataStatus dataStatus) {
        cartRef.child(cartId).removeValue()
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }
}
