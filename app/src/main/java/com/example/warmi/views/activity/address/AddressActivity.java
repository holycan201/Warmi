package com.example.warmi.views.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.adapter.recycleview.AddressRecycleViewAdapter;
import com.example.warmi.adapter.recycleview.OrderRecycleViewAdapter;
import com.example.warmi.controllers.AddressController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.views.activity.OrderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    ImageView ivBackArrow;
    Button addAddressBtn;
    RecyclerView recyclerView;
    AddressRecycleViewAdapter addressRecycleViewAdapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();
        initClickListener();

        fetchAddress();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.address_recycle_view);
        ivBackArrow = findViewById(R.id.back_arrow_address_page);
        addAddressBtn = findViewById(R.id.add_address_btn);
    }

    private void initClickListener() {
        ivBackArrow.setOnClickListener(this::onClick);
        addAddressBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow_address_page:
                finish();
                break;
            case R.id.add_address_btn:
                startActivity(new Intent(AddressActivity.this, CrudAddressActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchAddress();
    }

    public void fetchAddress() {
        AddressController addressController = new AddressController();
        addressController.fetchAddress(new FetchCallback.FetchAddressCallback() {
            @Override
            public void onAddressFetched(AddressModel addressModel) {
                if (addressModel != null) {
                    if (!addressModel.getItems().isEmpty()) {
                        List<AddressItems> addressItemsList = new ArrayList<>();

                        for (AddressItems addressItems : addressModel.getItems()) {
                            if (addressItems.getUser_id().equals(currentUser.getUid())) {
                                addressItemsList.add(addressItems);
                            }
                        }

                        addressRecycleViewAdapter = new AddressRecycleViewAdapter(addressItemsList, AddressActivity.this);
                        recyclerView.setAdapter(addressRecycleViewAdapter);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public void reCreate(){
        recreate();
    }
}