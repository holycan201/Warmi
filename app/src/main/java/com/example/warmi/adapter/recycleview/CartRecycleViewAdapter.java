package com.example.warmi.adapter.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.controllers.ProductsController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.products.ProductItems;

import java.util.List;

public class CartRecycleViewAdapter extends RecyclerView.Adapter<CartRecycleViewAdapter.CartViewHolder> {
    private final List<CartItems> cartItemsList;
    private final UpdateListener updateListener;

    public CartRecycleViewAdapter(List<CartItems> cartItemsList, UpdateListener updateListener) {
        this.cartItemsList = cartItemsList;
        this.updateListener = updateListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItems cartItems = cartItemsList.get(position);
        fetchProduct(cartItems, holder);
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleProduct, tvPriceProduct, tvAmountProduct;
        ImageView ivProductImage, ivDecreaseProduct, ivIncreaseProduct;
        CardView cardViewProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewProduct = itemView.findViewById(R.id.cv_product);
            tvTitleProduct = itemView.findViewById(R.id.tv_title_product);
            tvPriceProduct = itemView.findViewById(R.id.tv_price_product);
            tvAmountProduct = itemView.findViewById(R.id.tv__amount_product);
            ivProductImage = itemView.findViewById(R.id.iv_image_product);
            ivDecreaseProduct = itemView.findViewById(R.id.iv_decrease_amount_product);
            ivIncreaseProduct = itemView.findViewById(R.id.iv_increase_amount_product);
        }
    }

    private void fetchProduct(CartItems cartItems, CartViewHolder holder) {
        ProductsController productsController = new ProductsController();
        productsController.fetchProductById(cartItems.getProduct_id(), new FetchCallback.FetchProductItemCallback() {

            @Override
            public void onProductItemFetched(ProductItems productItems) {
                if (productItems != null) {
                    holder.tvTitleProduct.setText(productItems.getName());
                    holder.tvPriceProduct.setText(String.valueOf(productItems.getPrice()));
                    holder.tvAmountProduct.setText(String.valueOf(cartItems.getAmount()));

                    if (updateListener != null) {
                        updateListener.onPriceUpdate(productItems.getPrice() * cartItems.getAmount());
                    }

                    holder.ivDecreaseProduct.setOnClickListener(view -> {
                        if (updateListener != null) {
                            updateListener.onDecreaseAmount(cartItems, productItems.getPrice());
                        }
                    });

                    holder.ivIncreaseProduct.setOnClickListener(view -> {
                        if (updateListener != null) {
                            updateListener.onIncreaseAmount(cartItems, productItems.getPrice());
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public interface UpdateListener {
        void onPriceUpdate(int price);
        void onDecreaseAmount(CartItems cartItems, int price);

        void onIncreaseAmount(CartItems cartItems, int price);
    }
}
