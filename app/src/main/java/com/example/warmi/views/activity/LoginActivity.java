package com.example.warmi.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.warmi.R;

public class LoginActivity extends AppCompatActivity {
    Button confimLoginBtn;
    EditText inputPhoneNumberLogin;
    ImageView ivBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setClickListener();
    }

    private void initView() {
        ivBackArrow = findViewById(R.id.back_arrow_login);
        confimLoginBtn = findViewById(R.id.confirm_btn_login);
        inputPhoneNumberLogin = findViewById(R.id.text_inp_phone);
    }

    private void setClickListener() {
        ivBackArrow.setOnClickListener(this::onCLick);
        confimLoginBtn.setOnClickListener(this::onCLick);
    }

    private void onCLick(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn_login:
                String phoneNumber = inputPhoneNumberLogin.getText().toString();
                Intent intent = new Intent(LoginActivity.this, PhoneAuthActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;

            case R.id.back_arrow_login:
                finish();
                break;
        }
    }
}