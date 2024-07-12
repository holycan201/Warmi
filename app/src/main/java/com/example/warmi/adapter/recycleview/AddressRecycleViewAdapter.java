package com.example.warmi.adapter.recycleview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.warmi.controllers.AddressController;
import com.example.warmi.controllers.ProductsController;
import com.example.warmi.database.DataInterface;
import com.example.warmi.database.FetchCallback;
import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.products.ProductItems;
import com.example.warmi.views.activity.address.AddressActivity;
import com.example.warmi.views.activity.address.CrudAddressActivity;

import java.util.List;

public class AddressRecycleViewAdapter extends RecyclerView.Adapter<AddressRecycleViewAdapter.AddressViewHolder> {
    private final List<AddressItems> addressItemsList;
    private final Context context;

    public AddressRecycleViewAdapter(List<AddressItems> addressItemsList, Context context) {
        this.addressItemsList = addressItemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressItems addressItems = addressItemsList.get(position);
        holder.tvAddressName.setText(addressItems.getReceiver_name());
        holder.tvAddressDetail.setText(String.format("%s, %s, %s, %s", addressItems.getAddress(), addressItems.getDetail(), addressItems.getCity(), addressItems.getPost_code()));
        holder.ivEditAddress.setOnClickListener(v -> {
            Intent intent = new Intent(context, CrudAddressActivity.class);
            intent.putExtra("addressId", addressItems.getId());
            context.startActivity(intent);
        });

        holder.ivDeleteAddress.setOnClickListener(v -> {
            deleteAddress(addressItems);

        });
    }

    @Override
    public int getItemCount() {
        return addressItemsList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressName, tvAddressDetail;
        ImageView ivEditAddress, ivDeleteAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.address_name);
            tvAddressDetail = itemView.findViewById(R.id.address_detail);
            ivEditAddress = itemView.findViewById(R.id.iv_edit_address);
            ivDeleteAddress = itemView.findViewById(R.id.iv_delete_address);
        }
    }

    private void deleteAddress(AddressItems addressItems) {
        AddressController addressController = new AddressController();
        addressController.deleteAddress(addressItems.getId(), new DataInterface.DataStatus() {
            @Override
            public void DataIsInserted() {
            }

            @Override
            public void DataIsUpdated() {
                ((AddressActivity) context).reCreate();
            }

            @Override
            public void DataOperationFailed(Exception e) {
            }
        });
    }
}
