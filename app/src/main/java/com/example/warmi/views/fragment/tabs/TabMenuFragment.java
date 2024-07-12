package com.example.warmi.views.fragment.tabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.warmi.R;
import com.example.warmi.adapter.recycleview.MenuRecycleViewAdapter;
import com.example.warmi.models.products.ProductItems;

import java.util.List;


public class TabMenuFragment extends Fragment {
    private List<ProductItems> productList;
    private RecyclerView recyclerView;
    private MenuRecycleViewAdapter menuRecycleViewAdapter;
    private Button cartBtn;

    public TabMenuFragment(List<ProductItems> productList, Button cartBtn) {
        this.productList = productList;
        this.cartBtn = cartBtn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_menu, container, false);

        recyclerView = view.findViewById(R.id.menu_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        menuRecycleViewAdapter = new MenuRecycleViewAdapter(productList, cartBtn);
        recyclerView.setAdapter(menuRecycleViewAdapter);

        return view;
    }
}