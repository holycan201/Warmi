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

public class TransactionsRecycleViewAdapter extends RecyclerView.Adapter<TransactionsRecycleViewAdapter.TransactionsViewHolder> {
    private final List<OrderItems> orderItemsList;

    public TransactionsRecycleViewAdapter(List<OrderItems> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        OrderItems orderItems = orderItemsList.get(position);
        fetchProduct(orderItems, holder);
    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleProduct, tvDateOrder, tvTotalOrder;
        ImageView ivImageProduct;

        public TransactionsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleProduct = itemView.findViewById(R.id.title_tx1);
            tvDateOrder = itemView.findViewById(R.id.date_tx1);
            tvTotalOrder = itemView.findViewById(R.id.price_tx1);
            ivImageProduct = itemView.findViewById(R.id.img_tx);
        }
    }

    private void fetchProduct(OrderItems orderItems, TransactionsViewHolder holder) {
        ProductsController productsController = new ProductsController();
        productsController.fetchProductById(orderItems.getCartItems().get(0).getProduct_id(), new FetchCallback.FetchProductItemCallback() {
            @Override
            public void onProductItemFetched(ProductItems productItems) {
                if (productItems != null) {
                    long timestamp = orderItems.getTimestamp() * 1000L;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("id", "ID"));
                    String date = sdf.format(new Date(timestamp));

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
