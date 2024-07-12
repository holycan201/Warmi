package com.example.warmi.adapter.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.controllers.ProductsController;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.orders.OrderItems;
import com.example.warmi.models.products.ProductItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentOrderRecycleViewAdapter extends RecyclerView.Adapter<RecentOrderRecycleViewAdapter.RecentOrderViewHolder> {
    private final List<OrderItems> orderItemsList;

    public RecentOrderRecycleViewAdapter(List<OrderItems> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    @NonNull
    @Override
    public RecentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_order, parent, false);
        return new RecentOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderViewHolder holder, int position) {
        OrderItems orderItems = orderItemsList.get(position);
        fetchProduct(orderItems, holder);
    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }

    public static class RecentOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleProduct, tvDateOrder, tvTotalOrder;

        public RecentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleProduct = itemView.findViewById(R.id.title_recent_order);
            tvDateOrder = itemView.findViewById(R.id.desc_recent_order);
            tvTotalOrder = itemView.findViewById(R.id.price_recent_order);
        }
    }

    private void fetchProduct(OrderItems orderItems, RecentOrderViewHolder holder) {
        ProductsController productsController = new ProductsController();
        productsController.fetchProductById(orderItems.getCartItems().get(0).getProduct_id(), new FetchCallback.FetchProductItemCallback() {
            @Override
            public void onProductItemFetched(ProductItems productItems) {
                if (productItems != null) {
                    long timestamp = orderItems.getTimestamp() * 1000L;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("id", "ID"));
                    String date = "Diterima Pada " + sdf.format(new Date(timestamp));

                    holder.tvTitleProduct.setText(productItems.getName());
                    holder.tvDateOrder.setText(date);
                    holder.tvTotalOrder.setText(String.format(new Locale("id", "ID"), "Rp.%d", orderItems.getTotal_order()));
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
