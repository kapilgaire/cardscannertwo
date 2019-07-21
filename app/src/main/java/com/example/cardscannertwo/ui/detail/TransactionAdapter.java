package com.example.cardscannertwo.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.response.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.DataHolder> {

    private ArrayList<TransactionDetails> mTransactionDetails;
    private Context context;


    public TransactionAdapter(Context context) {
        mTransactionDetails = new ArrayList<>();
        this.context = context;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fuel_transaction_layout, parent, false);


        DataHolder myViewHolder = new DataHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int listPosition) {


        holder.registrationNoTv.setText(mTransactionDetails.get(listPosition).getRegistrationNo());
        holder.dateTv.setText(mTransactionDetails.get(listPosition).getCrDate());
        holder.qtyTv.setText(mTransactionDetails.get(listPosition).getQuantity());

    }

    @Override
    public int getItemCount() {
        return mTransactionDetails.size();

    }

    private void add(TransactionDetails data) {
        mTransactionDetails.add(data);


        notifyItemInserted(mTransactionDetails.size() - 1);
    }

    public void addAll(List<TransactionDetails> cardDetails) {

        for (TransactionDetails zone : cardDetails) {
            add(zone);
        }


    }

    public void clear() {

        if (!mTransactionDetails.isEmpty()) {
            mTransactionDetails.clear();
            notifyDataSetChanged();
        }
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        private TextView registrationNoTv;
        private TextView dateTv;
        private TextView qtyTv;


        public DataHolder(View itemView) {
            super(itemView);
            registrationNoTv = itemView.findViewById(R.id.registration_no_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            qtyTv = itemView.findViewById(R.id.qty_tv);


        }

    }


}






