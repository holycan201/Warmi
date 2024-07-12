package com.example.warmi.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.adapter.recycleview.CartRecycleViewAdapter;
import com.example.warmi.controllers.CartController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartRecycleViewAdapter.UpdateListener {
    private Button checkoutBtn;
    private ImageView ivBackArrow, ivDecreaseBtn, ivIncreaseBtn;
    private TextView tvTotalPrice;
    private RecyclerView recyclerView;
    private CartRecycleViewAdapter cartRecycleViewAdapter;
    private CartController cartController;
    private FirebaseAuth mAuth;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        fetchCart();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setClickListener();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        cartController = new CartController();
        recyclerView = findViewById(R.id.cart_recycle_view);
        ivBackArrow = findViewById(R.id.back_arrow_cart);
        ivDecreaseBtn = findViewById(R.id.iv_decrease_amount_product);
        ivIncreaseBtn = findViewById(R.id.iv_increase_amount_product);
        checkoutBtn = findViewById(R.id.cart_checkout_btn);
        tvTotalPrice = findViewById(R.id.total_shop);
    }

    private void setClickListener() {
        ivBackArrow.setOnClickListener(this::onClick);
        checkoutBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow_cart:
                finish();
                break;
            case R.id.cart_checkout_btn:
                startActivity(new Intent(CartActivity.this, OrderActivity.class));
                break;
        }
    }

    private void fetchCart() {
        CartController cartController = new CartController();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            cartController.fetchCart(new FetchCallback.FetchCartCallback() {
                @Override
                public void onCartFetched(CartModel cartModel) {
                    if (cartModel != null) {
                        List<CartItems> cartItemsList = cartModel.getItems();
                        cartRecycleViewAdapter = new CartRecycleViewAdapter(cartItemsList, CartActivity.this);
                        totalPrice = 0;
                        recyclerView.setAdapter(cartRecycleViewAdapter);
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    private void updateTotalPrice() {
        tvTotalPrice.setText(String.format(new Locale("id", "ID"), "Rp.%d", totalPrice));
    }

    @Override
    public void onPriceUpdate(int price) {
        totalPrice += price;
        updateTotalPrice();
    }

    @Override
    public void onDecreaseAmount(CartItems cartItems, int price) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (cartItems.getAmount() > 1) {
                cartItems.setAmount(cartItems.getAmount() - 1);
                cartController.updateCart(cartItems.getId(), cartItems, new DataInterface.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                    }

                    @Override
                    public void DataIsUpdated() {
                        totalPrice -= price;
                        updateTotalPrice();
                        fetchCart();
                    }

                    @Override
                    public void DataOperationFailed(Exception e) {
                    }
                });
            } else {
                cartController.deleteCart(cartItems.getId(), new DataInterface.DataStatus() {
                    @Override
                    public void DataIsInserted() {
                    }

                    @Override
                    public void DataIsUpdated() {
                        totalPrice -= price;
                        updateTotalPrice();
                        fetchCart();
                    }

                    @Override
                    public void DataOperationFailed(Exception e) {
                    }
                });
            }
        }
    }

    @Override
    public void onIncreaseAmount(CartItems cartItems, int price) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            cartItems.setAmount(cartItems.getAmount() + 1);
            cartController.updateCart(cartItems.getId(), cartItems, new DataInterface.DataStatus() {
                @Override
                public void DataIsInserted() {
                }

                @Override
                public void DataIsUpdated() {
                        totalPrice -= price;
                        updateTotalPrice();
                        fetchCart();
                }

                @Override
                public void DataOperationFailed(Exception e) {
                }
            });
        }
    }
}