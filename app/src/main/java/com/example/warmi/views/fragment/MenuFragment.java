package com.example.warmi.views.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.warmi.R;
import com.example.warmi.controllers.CartController;
import com.example.warmi.controllers.ProductsController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.example.warmi.models.products.ProductItems;
import com.example.warmi.models.products.ProductsModel;
import com.example.warmi.adapter.TabLayoutAdapter;
import com.example.warmi.adapter.ViewPagerAdapter;
import com.example.warmi.views.activity.CartActivity;
import com.example.warmi.views.fragment.tabs.TabMenuFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class MenuFragment extends Fragment {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    Button cartBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        initView(view);
        setClickListener();
        fetchProduct();
        fetchCart();

        return view;
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.menu_tab_layout);
        viewPager2 = view.findViewById(R.id.menu_view_pager);
        cartBtn = view.findViewById(R.id.cart_btn);
    }

    private void setClickListener() {
        cartBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.cart_btn) {
            startActivity(new Intent(getActivity(), CartActivity.class));
        }
    }

    private void fetchProduct() {
        ProductsController productsController = new ProductsController();
        productsController.fetchProduct(new FetchCallback.FetchProductCallback() {
            @Override
            public void onProductFetched(ProductsModel productsModel) {
                if (productsModel != null) {
                    updateUIWithProducts(productsModel);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }


    private void fetchCart() {
        CartController cartController = new CartController();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            cartController.fetchCart(new FetchCallback.FetchCartCallback() {
                @Override
                public void onCartFetched(CartModel cartModel) {
                    if (cartModel != null) {
                        List<CartItems> cartItemsList = new ArrayList<>();

                        for (CartItems cartItems : cartModel.getItems()){
                            if (currentUser.getUid().equals(cartItems.getUser_id())){
                                cartItemsList.add(cartItems);
                            }
                        }

                        cartBtn.setText(String.format(new Locale("id", "ID"), "Keranjang Belanja (%d)", cartItemsList.size()));
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    private void updateUIWithProducts(ProductsModel productsModel) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();
        HashSet<String> categories = new HashSet<>();

        for (ProductItems productItems : productsModel.getProductList()) {
            categories.add(productItems.getCategory());
        }

        for (String category : categories) {
            List<ProductItems> filteredProductList = new ArrayList<>();
            for (ProductItems productItems : productsModel.getProductList()) {
                if (productItems.getCategory().equals(category)) {
                    filteredProductList.add(productItems);
                }
            }
            fragments.add(new TabMenuFragment(filteredProductList, cartBtn));
            fragmentTitles.add(category);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, fragments, fragmentTitles);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.removeAllTabs();

        for (String title : fragmentTitles) {
            tabLayout.addTab(tabLayout.newTab().setText(Character.toUpperCase(title.charAt(0)) + title.substring(1).toLowerCase()));
        }

        TabLayoutAdapter.setupTabLayoutWithViewPager2(tabLayout, viewPager2);
    }
}

