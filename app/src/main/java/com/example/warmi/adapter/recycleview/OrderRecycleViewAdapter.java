package com.example.warmi.adapter.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.controllers.ProductsController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.products.ProductItems;

import java.util.List;

public class OrderRecycleViewAdapter extends RecyclerView.Adapter<OrderRecycleViewAdapter.OrderViewHolder> {
    private final List<CartItems> cartItemsList;
    private final UpdateListener updateListener;

    public OrderRecycleViewAdapter(List<CartItems> cartItemsList, UpdateListener updateListener) {
        this.cartItemsList = cartItemsList;
        this.updateListener = updateListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CartItems cartItems = cartItemsList.get(position);
        fetchProduct(cartItems, holder);
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleProduct, tvPriceProduct, tvAmountProduct;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleProduct = itemView.findViewById(R.id.title_checkout_product);
            tvPriceProduct = itemView.findViewById(R.id.price_checout_product);
            tvAmountProduct = itemView.findViewById(R.id.count_checkout_product);
        }
    }

    private void fetchProduct(CartItems cartItems, OrderViewHolder holder) {
        ProductsController productsController = new ProductsController();
        productsController.fetchProductById(cartItems.getProduct_id(), new FetchCallback.FetchProductItemCallback() {

            @Override
            public void onProductItemFetched(ProductItems productItems) {
                if (productItems != null) {
                    holder.tvTitleProduct.setText(productItems.getName());
                    holder.tvPriceProduct.setText(String.valueOf(productItems.getPrice()));
                    holder.tvAmountProduct.setText(String.valueOf(cartItems.getAmount()));

                    if (updateListener != null) {
                        updateListener.onAmountUpdate(productItems.getPrice() * cartItems.getAmount());
                    }
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public interface UpdateListener {
        void onAmountUpdate(int price);
    }
}
