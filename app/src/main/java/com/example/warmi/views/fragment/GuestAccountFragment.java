package com.example.warmi.views.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.warmi.R;
import com.example.warmi.views.activity.LoginActivity;

public class GuestAccountFragment extends Fragment {

    Button loginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_account, container, false);
        loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this::onCLick);
        return view;
    }

    private void onCLick(View view) {
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}