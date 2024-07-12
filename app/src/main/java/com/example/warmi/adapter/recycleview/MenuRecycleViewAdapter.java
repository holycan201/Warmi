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
import com.example.warmi.controllers.CartController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.example.warmi.models.products.ProductItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class MenuRecycleViewAdapter extends RecyclerView.Adapter<MenuRecycleViewAdapter.MenuViewHolder> {
    private final List<ProductItems> productItemsList;
    private FirebaseAuth mAuth;
    private Button cartBtn;

    public MenuRecycleViewAdapter(List<ProductItems> productItemsList, Button cartBtn) {
        this.productItemsList = productItemsList;
        this.cartBtn = cartBtn;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        ProductItems productItems = productItemsList.get(position);
        holder.tvTitleProduct.setText(productItems.getName());
        holder.tvDescProduct.setText(productItems.getDesc());
        holder.productPriceBtn.setText(String.format(new Locale("id", "ID"), "Add Rp.%d", productItems.getPrice()));
        holder.productPriceBtn.setOnClickListener(v -> addToCart(productItems));
    }

    @Override
    public int getItemCount() {
        return productItemsList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleProduct, tvDescProduct;
        ImageView ivProductImage;
        Button productPriceBtn;
        CardView cardViewProduct, cardViewProductImage;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewProduct = itemView.findViewById(R.id.card_view_product);
            cardViewProductImage = itemView.findViewById(R.id.card_view_product_image);
            tvTitleProduct = itemView.findViewById(R.id.title_product);
            tvDescProduct = itemView.findViewById(R.id.desc_product);
            ivProductImage = itemView.findViewById(R.id.img_product_menu);
            productPriceBtn = itemView.findViewById(R.id.reorder_product_btn);
        }
    }

    private void addToCart(ProductItems productItems) {
        CartController cartController = new CartController();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            cartController.fetchCart(new FetchCallback.FetchCartCallback() {
                @Override
                public void onCartFetched(CartModel cartModel) {
                    boolean isItemFound = false;

                    if (!cartModel.getItems().isEmpty()) {
                        for (CartItems cartItems : cartModel.getItems()) {
                            if (productItems.getId().equals(cartItems.getProduct_id())) {
                                cartItems.setAmount(cartItems.getAmount() + 1);
                                cartController.updateCart(cartItems.getId(), cartItems, new DataInterface.DataStatus() {
                                    @Override
                                    public void DataIsInserted() {
                                    }

                                    @Override
                                    public void DataIsUpdated() {
                                        updateCartButton(cartController, currentUser.getUid());
                                    }

                                    @Override
                                    public void DataOperationFailed(Exception e) {
                                    }
                                });
                                isItemFound = true;
                                break;
                            }
                        }
                    }

                    if (!isItemFound) {
                        CartItems cartItems = new CartItems(currentUser.getUid(), productItems.getId(), 1);
                        cartController.addCart(cartItems, new DataInterface.DataStatus() {
                            @Override
                            public void DataIsInserted() {
                                updateCartButton(cartController, currentUser.getUid());
                            }

                            @Override
                            public void DataIsUpdated() {
                            }

                            @Override
                            public void DataOperationFailed(Exception e) {
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

    }


    private void updateCartButton(CartController cartController, String userId) {
        cartController.fetchCart(new FetchCallback.FetchCartCallback() {
            @Override
            public void onCartFetched(CartModel cartModel) {
                if (cartModel != null) {
                    cartBtn.setText(String.format(new Locale("id", "ID"), "Keranjang Belanja (%d)", cartModel.getItems().size()));
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}
