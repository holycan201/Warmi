package com.example.warmi.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.warmi.R;
import com.example.warmi.controllers.UsersController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {
    private static final String TAG = "VerifyNumberActivity";
    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private TextView changePhoneNumberBtn, resendCode, textViewPhoneNumber;
    private UsersController usersController;
    private EditText otpCode1, otpCode2, otpCode3, otpCode4, otpCode5, otpCode6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_number);

        mAuth = FirebaseAuth.getInstance();
        usersController = new UsersController();

        initView();
        setupTextWatchers();
        setupCallbacks();

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        textViewPhoneNumber.setText(phoneNumber);


        startPhoneNumberVerification(phoneNumber);
    }

    private void initView() {
        ImageView arrowBack = findViewById(R.id.arrow_back_verif);
        textViewPhoneNumber = findViewById(R.id.number_verif);
        changePhoneNumberBtn = findViewById(R.id.edit_phone_number_verif);
        resendCode = findViewById(R.id.resend_code);
        otpCode1 = findViewById(R.id.input_otp_code1);
        otpCode2 = findViewById(R.id.input_otp_code2);
        otpCode3 = findViewById(R.id.input_otp_code3);
        otpCode4 = findViewById(R.id.input_otp_code4);
        otpCode5 = findViewById(R.id.input_otp_code5);
        otpCode6 = findViewById(R.id.input_otp_code6);

        arrowBack.setOnClickListener(this::onClick);
        changePhoneNumberBtn.setOnClickListener(this::onClick);
        resendCode.setOnClickListener(this::onClick);
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        otpCode1.addTextChangedListener(textWatcher);
        otpCode2.addTextChangedListener(textWatcher);
        otpCode3.addTextChangedListener(textWatcher);
        otpCode4.addTextChangedListener(textWatcher);
        otpCode5.addTextChangedListener(textWatcher);
        otpCode6.addTextChangedListener(textWatcher);
    }

    private void setupCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void checkAllInputs() {
        String input1 = otpCode1.getText().toString().trim();
        String input2 = otpCode2.getText().toString().trim();
        String input3 = otpCode3.getText().toString().trim();
        String input4 = otpCode4.getText().toString().trim();
        String input5 = otpCode5.getText().toString().trim();
        String input6 = otpCode6.getText().toString().trim();

        if (!input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty() && !input4.isEmpty() && !input5.isEmpty() && !input6.isEmpty()) {
            String otpCode = input1 + input2 + input3 + input4 + input5 + input6;
            verifyPhoneNumberWithCode(mVerificationId, otpCode);
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            String userUid = mAuth.getUid();

                            fetchUser(userUid);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d(TAG, "Error:" + task.getException());
                            }
                        }
                    }
                });
    }

    private void fetchUser(String userUid) {
        usersController.fetchUser(userUid, new FetchCallback.FetchUserCallback() {
            @Override
            public void onUserFetched(UsersModel usersModel) {
                if (usersModel != null) {
                    startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
                } else {
                    Intent intent = new Intent(PhoneAuthActivity.this, RegisterActivity.class);
                    intent.putExtra("userUid", userUid);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.w(TAG, "Error:" + e);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_phone_number_verif:
                startActivity(new Intent(PhoneAuthActivity.this, LoginActivity.class));
                break;
            case R.id.arrow_back_verif:
                finish();
                break;
            case R.id.resend_code:
                Toast.makeText(PhoneAuthActivity.this, "Kode Telah Dikirim Ulang", Toast.LENGTH_SHORT).show();
                resendVerificationCode(textViewPhoneNumber.getText().toString(), mResendToken);
                break;
        }
    }
}