package com.example.cardscannertwo.ui.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.response.ReportDetails;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailAdapter extends RecyclerView.Adapter<ReportDetailAdapter.DataHolder> {

    private ArrayList<ReportDetails> mReportDetailsArrayList;
    private Context context;


    public ReportDetailAdapter(Context context) {
        mReportDetailsArrayList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_layout_two, parent, false);


        DataHolder myViewHolder = new DataHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int listPosition) {


        holder.usernameNoTv.setText(mReportDetailsArrayList.get(listPosition).getUserName());
        holder.regTv.setText(mReportDetailsArrayList.get(listPosition).getRegistrationNo());
        holder.qtyTv.setText(mReportDetailsArrayList.get(listPosition).getQuantity()+" Ltrs.");
    }

    @Override
    public int getItemCount() {
        return mReportDetailsArrayList.size();

    }

    public void add(ReportDetails data) {
        mReportDetailsArrayList.add(data);


        notifyItemInserted(mReportDetailsArrayList.size() - 1);
    }

    public void addAll(List<ReportDetails> reportDetailsList) {

        for (ReportDetails td : reportDetailsList) {
            add(td);
        }


    }

    public void clear() {

        if (!mReportDetailsArrayList.isEmpty()) {
            mReportDetailsArrayList.clear();
            notifyDataSetChanged();
        }
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        private TextView usernameNoTv;
        private TextView regTv;
        private TextView qtyTv;


        public DataHolder(View itemView) {
            super(itemView);
            usernameNoTv = itemView.findViewById(R.id.username_no_tv);
            regTv = itemView.findViewById(R.id.reg_tv);
            qtyTv = itemView.findViewById(R.id.qty_tv);


        }

    }


}






