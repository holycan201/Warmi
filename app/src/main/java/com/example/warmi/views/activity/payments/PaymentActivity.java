package com.example.warmi.views.activity.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warmi.R;
import com.example.warmi.controllers.PaymentMethodeController;
import com.example.warmi.controllers.PaymentsController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.PaymentsModel;
import com.example.warmi.models.payment_methode.PaymentMethodeItems;
import com.example.warmi.views.activity.MainActivity;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PaymentActivity extends AppCompatActivity {
    ImageView ivClose;
    TextView tvLimitHour, tvLimitMinute, tvLimitSecond, tvTotalBill;
    PaymentsController paymentsController;
    String paymentMethodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initView();
        setClickListener();

        Intent intent = getIntent();
        paymentMethodeId = intent.getStringExtra("paymentMethodeId");

        addPayment(intent);

        fetchPayment();

        fetchPaymentMethode();
    }

    private void initView() {
        paymentsController = new PaymentsController();
        ivClose = findViewById(R.id.close_payment_page);
        tvTotalBill = findViewById(R.id.tv_total_bill_price);
        tvLimitHour = findViewById(R.id.text_count_hour);
        tvLimitMinute = findViewById(R.id.text_count_minute);
        tvLimitSecond = findViewById(R.id.text_count_second);
    }

    private void setClickListener() {
        ivClose.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_payment_page:
                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                break;
        }
    }

    private void addPayment(Intent intent) {
        PaymentsModel paymentsModel = new PaymentsModel(intent.getStringExtra("orderId"), paymentMethodeId, Timestamp.now().getSeconds() + 7200, intent.getIntExtra("totalOrder", 0), PaymentsModel.Status.Pending);
        paymentsController.addPayments(paymentsModel, new DataInterface.DataStatus() {
            @Override
            public void DataIsInserted() {
            }

            @Override
            public void DataIsUpdated() {
            }

            @Override
            public void DataOperationFailed(Exception e) {
            }
        });
    }

    private void fetchPayment() {
        paymentsController.fetchPayments(new FetchCallback.FetchPaymentCallback() {
            @Override
            public void onPaymentFetched(PaymentsModel paymentsModel) {
                long limitPayment = paymentsModel.getLimit();
                Date date = new Date(limitPayment * 1000L);

                SimpleDateFormat sdfHour = new SimpleDateFormat("HH", Locale.getDefault());
                sdfHour.setTimeZone(TimeZone.getDefault());

                SimpleDateFormat sdfMinute = new SimpleDateFormat("MM", Locale.getDefault());
                sdfHour.setTimeZone(TimeZone.getDefault());

                SimpleDateFormat sdfSecond = new SimpleDateFormat("SS", Locale.getDefault());
                sdfHour.setTimeZone(TimeZone.getDefault());

                tvLimitHour.setText(sdfHour.format(date));
                tvLimitMinute.setText(sdfMinute.format(date));
                tvLimitSecond.setText(sdfSecond.format(date));
                tvTotalBill.setText(String.format(new Locale("id", "ID"), "Rp.%d", paymentsModel.getAmount()));
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void fetchPaymentMethode() {
        PaymentMethodeController paymentMethodeController = new PaymentMethodeController();
        paymentMethodeController.fetchPaymentsMethodeById(paymentMethodeId, new FetchCallback.FetchPaymentMethodeItemCallback() {
            @Override
            public void onPaymentMethodeItemFetched(PaymentMethodeItems paymentMethodeItems) {
                FrameLayout container = findViewById(R.id.payment_methode_container);
                LayoutInflater inflater = LayoutInflater.from(PaymentActivity.this);
                if (paymentMethodeItems.getType().contains("qr")) {
                    View view = inflater.inflate(R.layout.qr_payment, container, false);
                    container.addView(view);
                } else {
                    View view = inflater.inflate(R.layout.transfer_payment, container, false);
                    container.addView(view);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}