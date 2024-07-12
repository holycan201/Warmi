package com.example.warmi.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warmi.R;
import com.example.warmi.controllers.AddressController;
import com.example.warmi.controllers.UsersController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.models.UsersModel;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private UsersController usersController;
    private AddressController addressController;
    private boolean isUserAdded = false,
            isAddressAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText username, fullName, address, addressDetail;
        Button confirmRegisterBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.input_username_regist);
        fullName = findViewById(R.id.input_fullname_regist);
        address = findViewById(R.id.input_address_regist);
        addressDetail = findViewById(R.id.input_detail_address_regist);
        confirmRegisterBtn = findViewById(R.id.confirm_regist_btn);

        Intent intent = getIntent();
        String userUid = intent.getStringExtra("userUid");


        String profileImage = "https://cdn.pixabay.com/photo/2023/12/07/11/04/girl-8435329_1280.png";

        usersController = new UsersController();
        addressController = new AddressController();


        confirmRegisterBtn.setOnClickListener(v -> {
            usersController.addUser(
                    userUid,
                    new UsersModel(username.getText().toString(), fullName.getText().toString(), profileImage),
                    new DataInterface.DataStatus() {
                        @Override
                        public void DataIsInserted() {
                            Log.d(TAG, "User added successfully");
                            isUserAdded = true;
                        }

                        @Override
                        public void DataIsUpdated() {
                        }

                        @Override
                        public void DataOperationFailed(Exception e) {
                            Log.e(TAG, "Error adding user", e);
                        }
                    }
            );

            addressController.addAddress(
                    userUid,
                    new AddressItems(address.getText().toString(), address.getText().toString(), address.getText().toString(), address.getText().toString(), addressDetail.getText().toString()),
                    new DataInterface.DataStatus() {
                        @Override
                        public void DataIsInserted() {
                            Log.d(TAG, "Address added successfully");
                            isAddressAdded = true;
                        }

                        @Override
                        public void DataIsUpdated() {
                        }

                        @Override
                        public void DataOperationFailed(Exception e) {
                            Log.e(TAG, "Error adding address", e);
                        }
                    }
            );

            if (isUserAdded && isAddressAdded){
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            } else {
                Log.d(TAG, "REGISTER FAILED");
            }
        });
    }
}