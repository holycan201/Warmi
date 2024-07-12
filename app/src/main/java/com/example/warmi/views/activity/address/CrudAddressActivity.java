package com.example.warmi.views.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warmi.R;
import com.example.warmi.controllers.AddressController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrudAddressActivity extends AppCompatActivity {
    private EditText etReceiverName, etCity, etAddress, etAddressDetail, etPosCode;
    private Button confirmAddAddressBtn;
    private ImageView ivBackArrow;
    private FirebaseUser currentUser;
    private AddressController addressController;
    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_address);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        initView();
        initClickListener();

        Intent intent = getIntent();
        addressId = intent.getStringExtra("addressId");
        if (addressId != null) {
            fetchAddress(addressId);
        }
    }

    private void initView() {
        addressController = new AddressController();
        etReceiverName = findViewById(R.id.input_add_receiver_name);
        etCity = findViewById(R.id.input_add_city);
        etAddress = findViewById(R.id.input_add_address);
        etAddressDetail = findViewById(R.id.input_add_detail_address);
        etPosCode = findViewById(R.id.input_add_pos_code);
        ivBackArrow = findViewById(R.id.iv_back_arrow_add_address);
        confirmAddAddressBtn = findViewById(R.id.confirm_add_address_btn);
    }

    private void initClickListener() {
        ivBackArrow.setOnClickListener(this::onClick);
        confirmAddAddressBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_add_address_btn:
                addAddress();
                break;
            case R.id.iv_back_arrow_add_address:
                finish();
                break;
        }
    }

    private void addAddress() {
        AddressItems addressItems = new AddressItems(
                etReceiverName.getText().toString(),
                etCity.getText().toString(),
                etAddress.getText().toString(),
                etAddressDetail.getText().toString(),
                etPosCode.getText().toString()
        );
        if (addressId != null) {
            addressController.updateAddress(addressId, currentUser.getUid(), addressItems, new DataInterface.DataStatus() {
                @Override
                public void DataIsInserted() {
                }

                @Override
                public void DataIsUpdated() {
                    Toast.makeText(CrudAddressActivity.this, "Edit alamat berhasil", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataOperationFailed(Exception e) {
                }
            });
        } else {
            addressController.addAddress(currentUser.getUid(), addressItems, new DataInterface.DataStatus() {
                @Override
                public void DataIsInserted() {
                    finish();
                    Toast.makeText(CrudAddressActivity.this, "Tambah alamat berhasil", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataIsUpdated() {
                }

                @Override
                public void DataOperationFailed(Exception e) {
                    Toast.makeText(CrudAddressActivity.this, "Maaf, tambah alamat gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void fetchAddress(String addressId) {
        addressController.fetchAddressById(addressId, new FetchCallback.FetchAddressItemCallback() {
            @Override
            public void onAddressItemFetched(AddressItems addressItems) {
                etReceiverName.setText(addressItems.getReceiver_name());
                etCity.setText(addressItems.getCity());
                etAddress.setText(addressItems.getAddress());
                etAddressDetail.setText(addressItems.getDetail());
                etPosCode.setText(addressItems.getPost_code());
                confirmAddAddressBtn.setText("Konfirmasi Edit");
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}