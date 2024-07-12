package com.example.warmi.views.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.warmi.R;
import com.example.warmi.adapter.recycleview.RecentOrderRecycleViewAdapter;
import com.example.warmi.controllers.AddressController;
import com.example.warmi.controllers.OrderController;
import com.example.warmi.controllers.UsersController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.models.UsersModel;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.models.orders.OrderModel;
import com.example.warmi.views.activity.address.AddressActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private UsersModel user;
    private AddressModel address;
    private TextView textUsername, textPhoneNumber, textAddress, textDescAddress;
    private ImageView ivAddressArrowRight;
    private Button logoutBtn;
    private RecyclerView recyclerView;
    private RecentOrderRecycleViewAdapter recentOrderRecycleViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initView(view);
        initListener();

        if (currentUser != null) {
            fetchUser();
            fetchAddress();
        }

        fetchOrder();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    private void initView(View view) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.recent_order_recycle_view);

        textUsername = view.findViewById(R.id.text_profile_username);
        textPhoneNumber = view.findViewById(R.id.text_profile_phone);
        textAddress = view.findViewById(R.id.text_profile_address);
        textDescAddress = view.findViewById(R.id.text_profile_desc_address);

        ivAddressArrowRight = view.findViewById(R.id.arrow_right_profile);

        logoutBtn = view.findViewById(R.id.logout_account_btn);
    }

    private void initListener() {
        logoutBtn.setOnClickListener(this::onClick);
        ivAddressArrowRight.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_account_btn:
                FirebaseAuth.getInstance().signOut();
                updateUIAfterSignOut();
                break;
            case R.id.arrow_right_profile:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
        }
    }

    private void fetchUser() {
        UsersController usersController = new UsersController();
        usersController.fetchUser(currentUser.getUid(), new FetchCallback.FetchUserCallback() {
            @Override
            public void onUserFetched(UsersModel usersModel) {
                if (usersModel != null) {
                    user = usersModel;
                    textUsername.setText(user.getUsername());
                    textPhoneNumber.setText(currentUser.getPhoneNumber());
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void fetchAddress() {
        AddressController addressController = new AddressController();
        addressController.fetchAddress(new FetchCallback.FetchAddressCallback() {
            @Override
            public void onAddressFetched(AddressModel addressModel) {
                if (addressModel != null) {
                    for (AddressItems addressItems : addressModel.getItems()) {
                        if (addressItems.getUser_id().equals(currentUser.getUid())) {
                            textAddress.setText(addressItems.getAddress());
                            textDescAddress.setText(String.format("%s, %s, %s", addressItems.getDetail(), addressItems.getCity(), addressItems.getPost_code()));
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void fetchOrder() {
        OrderController orderController = new OrderController();
        orderController.fetchOrder(new FetchCallback.FetchOrderCallback() {
            @Override
            public void onOrderFetched(OrderModel orderModel) {
                if (!orderModel.getOrderItems().isEmpty()) {
                    List<OrderItems> orderItemsList = orderModel.getOrderItems();
                    List<OrderItems> filteredList = new ArrayList<>();

                    for (OrderItems orderItems : orderItemsList) {
                        if (orderItems.getStatus().equals("Selesai")) {
                            filteredList.add(orderItems);
                        }
                    }

                    recentOrderRecycleViewAdapter = new RecentOrderRecycleViewAdapter(filteredList);
                    recyclerView.setAdapter(recentOrderRecycleViewAdapter);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TabTransactions", "Error fetching orders: " + e.getMessage());
            }
        });
    }

    private void updateUIAfterSignOut() {
        Fragment guestFragment = new GuestAccountFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, guestFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}