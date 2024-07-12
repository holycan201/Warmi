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
import com.example.warmi.adapter.recycleview.OrderRecycleViewAdapter;
import com.example.warmi.controllers.AddressController;
import com.example.warmi.controllers.CartController;
import com.example.warmi.controllers.OrderController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.models.orders.OrderModel;
import com.example.warmi.views.activity.address.AddressActivity;
import com.example.warmi.views.activity.payments.PaymentMethodeActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity implements OrderRecycleViewAdapter.UpdateListener {
    Button changeAddressBtn, SelectPaymentMethodeBtn;
    ImageView ivBackArrow;
    TextView tvReceiverName, tvAddress, tvDetailAddress, tvTotalPrice, tvShipmentPrice, tvTotalOrder;
    RecyclerView recyclerView;
    OrderRecycleViewAdapter orderRecycleViewAdapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    OrderController orderController;
    List<CartItems> cartItemsList;
    String orderId;
    int totalPrice = 0, totalOrder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initClickListener();
        fetchAddress();
        fetchCart();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void initView() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        orderController = new OrderController();

        cartItemsList = new ArrayList<>();

        recyclerView = findViewById(R.id.checkout_recycle_view);

        ivBackArrow = findViewById(R.id.back_arrow_cart);

        tvReceiverName = findViewById(R.id.tv_receiver_name);
        tvAddress = findViewById(R.id.address_checkout);
        tvDetailAddress = findViewById(R.id.detail_address_checkout);
        tvTotalPrice = findViewById(R.id.tv_total_bill);
        tvShipmentPrice = findViewById(R.id.delivery_price_checkout);
        tvTotalOrder = findViewById(R.id.total_order_price_checkout);

        changeAddressBtn = findViewById(R.id.change_address_btn);
        SelectPaymentMethodeBtn = findViewById(R.id.cart_checkout_btn);
    }

    void initClickListener() {
        ivBackArrow.setOnClickListener(this::onClick);
        changeAddressBtn.setOnClickListener(this::onClick);
        SelectPaymentMethodeBtn.setOnClickListener(this::onClick);
    }

    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow_cart:
                finish();
                break;
            case R.id.change_address_btn:
                startActivity(new Intent(OrderActivity.this, AddressActivity.class));
                break;
            case R.id.cart_checkout_btn:
                fetchOrder();
                Intent intent = new Intent(OrderActivity.this, PaymentMethodeActivity.class);
                intent.putExtra("totalOrder", totalOrder);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
                break;
        }
    }

    private void fetchAddress() {
        AddressController addressController = new AddressController();
        if (currentUser != null) {
            addressController.fetchAddress(new FetchCallback.FetchAddressCallback() {
                @Override
                public void onAddressFetched(AddressModel addressModel) {
                    if (addressModel != null) {
                        for (AddressItems addressItems : addressModel.getItems()) {
                            tvReceiverName.setText(addressItems.getReceiver_name());
                            tvAddress.setText(addressItems.getAddress());
                            tvDetailAddress.setText(addressItems.getDetail());
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    private void fetchCart() {
        CartController cartController = new CartController();
        cartController.fetchCart(new FetchCallback.FetchCartCallback() {
            @Override
            public void onCartFetched(CartModel cartModel) {
                if (cartModel != null) {
                    for (CartItems cartItems : cartModel.getItems()) {
                        if (currentUser.getUid().equals(cartItems.getUser_id())) {
                            cartItemsList.add(cartItems);
                        }
                    }

                    orderRecycleViewAdapter = new OrderRecycleViewAdapter(cartItemsList, OrderActivity.this);
                    recyclerView.setAdapter(orderRecycleViewAdapter);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public void fetchOrder() {
        orderController.fetchOrder(new FetchCallback.FetchOrderCallback() {
            @Override
            public void onOrderFetched(OrderModel orderModel) {
                addOrder(orderModel);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void addOrder(OrderModel orderModel) {
        OrderItems orderItems = new OrderItems(currentUser.getUid(), Timestamp.now().getSeconds(), totalOrder, cartItemsList, OrderItems.Status.Belum_Bayar);
        boolean isOrderExists = false;

        if (!orderModel.getOrderItems().isEmpty()) {
            for (OrderItems items : orderModel.getOrderItems()) {
                if (isSameCartItems(items.getCartItems(), cartItemsList) && items.getStatus().equals(orderItems.getStatus())) {
                    orderId = items.getId();
                    isOrderExists = true;
                    break;
                }
            }
        }

        if (!isOrderExists) {
            orderController.addOrder(orderItems, new DataInterface.DataStatus() {
                @Override
                public void DataIsInserted() {
                    orderController.fetchOrder(new FetchCallback.FetchOrderCallback() {
                        @Override
                        public void onOrderFetched(OrderModel orderModel) {
                            for (OrderItems items : orderModel.getOrderItems()) {
                                if (isSameCartItems(items.getCartItems(), cartItemsList) && items.getStatus().equals(orderItems.getStatus())) {
                                    orderId = items.getId();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                @Override
                public void DataIsUpdated() {
                }

                @Override
                public void DataOperationFailed(Exception e) {
                }
            });
        }
    }

    private boolean isSameCartItems(List<CartItems> list1, List<CartItems> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            CartItems item1 = list1.get(i);
            CartItems item2 = list2.get(i);
            if (!Objects.equals(item1.getUser_id(), item2.getUser_id()) ||
                    !Objects.equals(item1.getProduct_id(), item2.getProduct_id()) ||
                    item1.getAmount() != item2.getAmount()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAmountUpdate(int price) {
//        TODO shipment (5000 = Shipment Fee)
        totalPrice += price;
        totalOrder = totalPrice + 5000;
        tvTotalPrice.setText(String.format(new Locale("id", "ID"), "Rp.%d", totalPrice));
        tvTotalOrder.setText(String.format(new Locale("id", "ID"), "Rp.%d", totalOrder));
    }
}