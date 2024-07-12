package com.example.warmi.controllers;

import androidx.annotation.NonNull;

import com.example.warmi.database.Config;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.UsersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersController {
    private static final String DATABASE_REFERENCE = "users";
    DatabaseReference userRef;

    public UsersController(){
        this.userRef = FirebaseDatabase.getInstance(Config.getDatabaseUrl()).getReference(DATABASE_REFERENCE);
    }

    public void addUser(String userUid, UsersModel user, DataInterface.DataStatus dataStatus) {
        userRef.child(userUid).setValue(user)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }

    public void fetchUser(String userUid, FetchCallback.FetchUserCallback callback) {
        userRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersModel user = dataSnapshot.getValue(UsersModel.class);
                callback.onUserFetched(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }


    public void updateUser(String userUid, UsersModel updatedUser, DataInterface.DataStatus dataStatus) {
        userRef.child(userUid).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated())
                .addOnFailureListener(dataStatus::DataOperationFailed);
    }
}
