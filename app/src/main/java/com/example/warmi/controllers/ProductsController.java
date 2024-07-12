package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.models.products.ProductItems;
import com.example.warmi.models.products.ProductsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsController {
    private static final String DATABASE_REFERENCE = "products";
    DatabaseReference productRef;

    public ProductsController() {
        this.productRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addProduct(ProductsModel productsModel, DataInterface.DataStatus dataStatus) {
        String productId = productRef.push().getKey();
        if (productId != null) {
            productRef.child(productId).setValue(productsModel)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        } else {
            dataStatus.DataOperationFailed(new Exception("Failed to generate address ID"));
        }
    }

    public void fetchProduct(FetchCallback.FetchProductCallback callback) {
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ProductItems> products = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductItems productItems = snapshot.getValue(ProductItems.class);
                    products.add(productItems);
                }
                ProductsModel productsModel = new ProductsModel(products);
                callback.onProductFetched(productsModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void fetchProductById(String productId, FetchCallback.FetchProductItemCallback callback) {
        productRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductItems productItems = dataSnapshot.getValue(ProductItems.class);
                callback.onProductItemFetched(productItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }


    public void updateProduct(String productId, AddressModel updateAddress, DataInterface.DataStatus dataStatus) {
        productRef.child(productId).setValue(updateAddress)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }
}
