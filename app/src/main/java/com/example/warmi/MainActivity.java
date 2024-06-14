package com.example.warmi;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomMenu;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomMenu = findViewById(R.id.bottom_menu);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();

        bottomMenu.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.home){
            fragment = new HomeFragment();
        } else if (menuItem.getItemId() == R.id.menu) {
            fragment = new MenuFragment();
        } else if (menuItem.getItemId() == R.id.transactions) {
            fragment = new TransactionFragment();
        } else {
            fragment = new AccountFragment();
        }

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_container, fragment).commit();
        return true;
    }
}