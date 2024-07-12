package com.example.warmi.views.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.warmi.R;
import com.example.warmi.views.fragment.AccountFragment;
import com.example.warmi.views.fragment.GuestAccountFragment;
import com.example.warmi.views.fragment.HomeFragment;
import com.example.warmi.views.fragment.MenuFragment;
import com.example.warmi.views.fragment.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomMenu = findViewById(R.id.bottom_menu);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();

        bottomMenu.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Fragment fragment;
        if (menuItem.getItemId() == R.id.home) {
            fragment = new HomeFragment();
        } else if (menuItem.getItemId() == R.id.menu) {
            fragment = new MenuFragment();
        } else if (menuItem.getItemId() == R.id.transactions) {
            fragment = new TransactionFragment();
        } else {
            fragment =  (currentUser != null) ? new AccountFragment() : new GuestAccountFragment();
        }

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_container, fragment).commit();
        return true;
    }
}