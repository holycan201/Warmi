package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
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

public class AddressController {
    private static final String DATABASE_REFERENCE = "address";
    DatabaseReference addressRef;

    public AddressController() {
        this.addressRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addAddress(String userUid, AddressItems addressItems, DataInterface.DataStatus dataStatus) {
        String addressId = addressRef.push().getKey();
        if (addressId != null) {
            addressItems.setId(addressId);
            addressItems.setUser_id(userUid);
            addressRef.child(addressId).setValue(addressItems)
                    .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                    .addOnFailureListener(dataStatus::DataOperationFailed);
        } else {
            dataStatus.DataOperationFailed(new Exception("Failed to generate address ID"));
        }
    }

    public void fetchAddress(FetchCallback.FetchAddressCallback callback) {
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AddressItems> addressItemsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AddressItems addressItems = snapshot.getValue(AddressItems.class);
                    addressItemsList.add(addressItems);
                }
                AddressModel addressModel = new AddressModel(addressItemsList);
                callback.onAddressFetched(addressModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void fetchAddressById(String addressId, FetchCallback.FetchAddressItemCallback callback) {
        addressRef.child(addressId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddressItems addressItems = dataSnapshot.getValue(AddressItems.class);
                callback.onAddressItemFetched(addressItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void updateAddress(String addressId, String userId, AddressItems updateAddress, DataInterface.DataStatus dataStatus) {
        updateAddress.setId(addressId);
        updateAddress.setUser_id(userId);
        addressRef.child(addressId).setValue(updateAddress)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }

    public void deleteAddress(String addressId, DataInterface.DataStatus dataStatus) {
        addressRef.child(addressId).removeValue()
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }
}
