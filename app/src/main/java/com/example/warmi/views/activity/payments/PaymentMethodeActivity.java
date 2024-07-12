package com.example.warmi.views.activity.payments;

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
import com.example.warmi.adapter.recycleview.PaymentMethodeRecycleViewAdapter;
import com.example.warmi.controllers.CartController;
import com.example.warmi.controllers.PaymentMethodeController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.example.warmi.models.payment_methode.PaymentMethodeItems;
import com.example.warmi.models.payment_methode.PaymentMethodeModel;
import com.example.warmi.views.activity.OrderActivity;

import java.util.List;
import java.util.Locale;

public class PaymentMethodeActivity extends AppCompatActivity {
    Button payBtn;
    ImageView ivBackArrow;
    TextView tvTotalBill;
    RecyclerView recyclerView;
    PaymentMethodeRecycleViewAdapter paymentMethodeRecycleViewAdapter;
    String orderId;
    int totalOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methode);

        initView();
        setClickListener();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        totalOrder = intent.getIntExtra("totalOrder", 0);
        tvTotalBill.setText(String.format(new Locale("id", "ID"), "Rp.%d", totalOrder));
        fetchPaymentMethode();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        recyclerView = findViewById(R.id.payment_methode_recycle_view);
        ivBackArrow = findViewById(R.id.back_arrow_payment_methode_page);
        tvTotalBill = findViewById(R.id.tv_total_bill);
        payBtn = findViewById(R.id.next_to_detail_payment_btn);
    }

    private void setClickListener() {
        ivBackArrow.setOnClickListener(this::onClick);
        payBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow_payment_methode_page:
                finish();
                break;
            case R.id.next_to_detail_payment_btn:
                String paymentMethodeId = paymentMethodeRecycleViewAdapter.getSelectedPaymentMethode();
                Intent intent = new Intent(PaymentMethodeActivity.this, PaymentActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("totalOrder", totalOrder);
                intent.putExtra("paymentMethodeId", paymentMethodeId);
                startActivity(intent);
                break;
        }
    }

    private void fetchPaymentMethode() {
        PaymentMethodeController paymentMethodeController = new PaymentMethodeController();
        paymentMethodeController.fetchPaymentMethode(new FetchCallback.FetchPaymentMethodeCallback() {
            @Override
            public void onPaymentMethodeFetched(PaymentMethodeModel paymentMethodeModel) {
                if (paymentMethodeModel != null) {
                    List<PaymentMethodeItems> paymentMethodeItems = paymentMethodeModel.getPaymentMethodeItems();

                    paymentMethodeRecycleViewAdapter = new PaymentMethodeRecycleViewAdapter(paymentMethodeItems);
                    recyclerView.setAdapter(paymentMethodeRecycleViewAdapter);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}