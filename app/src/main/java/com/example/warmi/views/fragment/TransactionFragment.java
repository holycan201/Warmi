package com.example.warmi.views.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.warmi.R;
import com.example.warmi.adapter.TabLayoutAdapter;
import com.example.warmi.adapter.ViewPagerAdapter;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.views.fragment.tabs.TabTransactionsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        tabLayout = view.findViewById(R.id.transaction_tab_layout);
        viewPager2 = view.findViewById(R.id.transaction_view_pager);

        List<String> fragmentTitles = OrderItems.getAllStatuses();
        List<Fragment> fragments = new ArrayList<>();

        for (String title : fragmentTitles) {
            fragments.add(TabTransactionsFragment.newInstance(title));
        }

        viewPagerAdapter = new ViewPagerAdapter(this, fragments, fragmentTitles);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.removeAllTabs();

        for (String title : fragmentTitles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

        TabLayoutAdapter.setupTabLayoutWithViewPager2(tabLayout, viewPager2);

        return view;
    }
}
