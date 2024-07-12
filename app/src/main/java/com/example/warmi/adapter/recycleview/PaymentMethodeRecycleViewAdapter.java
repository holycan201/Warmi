package com.example.warmi.adapter.recycleview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmi.R;
import com.example.warmi.models.payment_methode.PaymentMethodeItems;

import java.util.List;

public class PaymentMethodeRecycleViewAdapter extends RecyclerView.Adapter<PaymentMethodeRecycleViewAdapter.PaymentMethodeViewHolder> {
    private final List<PaymentMethodeItems> paymentMethodeItemsList;
    private int selectPaymentMethode = RecyclerView.NO_POSITION;
    private String paymentMethodeId;

    public PaymentMethodeRecycleViewAdapter(List<PaymentMethodeItems> paymentMethodeItemsList) {
        this.paymentMethodeItemsList = paymentMethodeItemsList;
    }

    @NonNull
    @Override
    public PaymentMethodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentMethodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodeViewHolder holder, int position) {
        PaymentMethodeItems paymentMethodeItems = paymentMethodeItemsList.get(position);
        holder.selectMethodeBtn.setText(paymentMethodeItems.getType());

        holder.selectMethodeBtn.setChecked(position == selectPaymentMethode);
        holder.selectMethodeBtn.setOnClickListener(v -> {
            selectPaymentMethode = holder.getAdapterPosition();
            paymentMethodeId = paymentMethodeItems.getId();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethodeItemsList.size();
    }

    public static class PaymentMethodeViewHolder extends RecyclerView.ViewHolder {
        RadioButton selectMethodeBtn;

        public PaymentMethodeViewHolder(@NonNull View itemView) {
            super(itemView);
            selectMethodeBtn = itemView.findViewById(R.id.select_payment_methode);
        }
    }

    public String getSelectedPaymentMethode(){
        return paymentMethodeId;
    }
}
