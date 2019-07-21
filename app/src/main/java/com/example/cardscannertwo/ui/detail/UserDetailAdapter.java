package com.example.cardscannertwo.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.response.CardDetails;

import java.util.ArrayList;
import java.util.List;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.DataHolder> {

    private ArrayList<CardDetails> mCardDetails;
    private Context context;
    private int row_index = -1;



    private UserDetailAdapterListener mUserDetailAdapterListener;

    public void setRow_index(int row_index) {
        this.row_index = row_index;
        notifyDataSetChanged();
    }

    public void setNewDivisionAdapterListener(UserDetailAdapterListener mUserDetailAdapterListener) {
        this.mUserDetailAdapterListener = mUserDetailAdapterListener;
    }


    public UserDetailAdapter(Context context) {
        mCardDetails = new ArrayList<>();
        this.context = context;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_detail_layout, parent, false);


        DataHolder myViewHolder = new DataHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( final DataHolder holder, final int listPosition) {
        holder.user_info_tv.setText(mCardDetails.get(listPosition).getRegistrationNo()+"/"+ mCardDetails.get(listPosition).getFuelType());

        if (row_index == listPosition) {
            holder.user_info_tv.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.user_info_tv.setTextColor((context.getResources().getColor(R.color.black)));
        } else {
            holder.user_info_tv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.user_info_tv.setTextColor((context.getResources().getColor(R.color.white)));
        }





    }

    @Override
    public int getItemCount() {
        return mCardDetails.size();

    }

    private void add(CardDetails data) {
        mCardDetails.add(data);


        notifyItemInserted(mCardDetails.size() - 1);
    }

    public void addAll(List<CardDetails> cardDetails) {

        for (CardDetails zone : cardDetails) {
            add(zone);
        }


    }
    public void clear() {

        if(!mCardDetails.isEmpty()) {
            mCardDetails.clear();
            notifyDataSetChanged();
        }
    }
    public class DataHolder extends RecyclerView.ViewHolder {

        TextView user_info_tv;


        public DataHolder(View itemView) {
            super(itemView);
            this.user_info_tv = itemView.findViewById(R.id.user_info_tv);

            user_info_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = getAdapterPosition();
                    if (mUserDetailAdapterListener != null) {
                        mUserDetailAdapterListener.onSelected(mCardDetails.get(getAdapterPosition()));
                        notifyDataSetChanged();

                    }
                }
            });
        }

    }

    public interface UserDetailAdapterListener {
        void onSelected(CardDetails cardDetails);
    }
}






