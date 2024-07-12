package com.example.warmi.views.fragment.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.warmi.R;
import com.example.warmi.adapter.recycleview.CartRecycleViewAdapter;
import com.example.warmi.adapter.recycleview.TransactionsRecycleViewAdapter;
import com.example.warmi.controllers.OrderController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.models.orders.OrderModel;
import com.example.warmi.views.activity.CartActivity;
import com.example.warmi.views.activity.payments.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

public class TabTransactionsFragment extends Fragment {
    private static final String ARG_STATUS = "status";
    private RecyclerView recyclerView;
    private TransactionsRecycleViewAdapter transactionsRecycleViewAdapter;
    private String status;

    public static TabTransactionsFragment newInstance(String status) {
        TabTransactionsFragment fragment = new TabTransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            setStatus(getArguments().getString(ARG_STATUS));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_transactions, container, false);


        recyclerView = view.findViewById(R.id.tab_transactions_recycle_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void fetchOrder() {
        OrderController orderController = new OrderController();
        orderController.fetchOrder(new FetchCallback.FetchOrderCallback() {
            @Override
            public void onOrderFetched(OrderModel orderModel) {
                if (!orderModel.getOrderItems().isEmpty()) {
                    List<OrderItems> orderItemsList = orderModel.getOrderItems();
                    List<OrderItems> filteredList = filterByStatus(orderItemsList, status);
                    transactionsRecycleViewAdapter = new TransactionsRecycleViewAdapter(filteredList);
                    recyclerView.setAdapter(transactionsRecycleViewAdapter);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TabTransactions", "Error fetching orders: " + e.getMessage());
            }
        });
    }

    private List<OrderItems> filterByStatus(List<OrderItems> orderItemsList, String status) {
        List<OrderItems> filteredList = new ArrayList<>();
        for (OrderItems orderItems : orderItemsList) {
            if (orderItems.getStatus().equals(status)) {
                filteredList.add(orderItems);
            }
        }
        return filteredList;
    }

    private void setStatus(String status) {
        this.status = status;
        fetchOrder();
    }
}