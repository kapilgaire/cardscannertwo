package com.example.cardscannertwo.ui.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.response.CardDetails;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar ;
    private ArrayList<CardDetails> cardDetailsList;

    private TextView cardInfoTv;
    private RecyclerView vehicleListRv;
    private CheckBox ifOtherCb;
    private TextInputLayout vehicleNoTil;
    private EditText vehicleNoEt;
    private RadioGroup fuelTypeRg;
    private RadioButton petrolRb;
    private RadioButton dieselRb;
    private TextInputLayout qtyTil;
    private EditText qtyNoEt;
    private TextInputLayout meterReadingTil;
    private EditText meterReadingEt;
    private Button submitMbtn;
    private LinearLayout ifOtherCbLl;



    private UserDetailAdapter mUserDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cardDetailsList = getIntent().getParcelableArrayListExtra("CARD_DETAIL_LIST");

        initViews();
        setSupportActionBar(toolbar);
        initListener();
        mUserDetailAdapter = new UserDetailAdapter(this);

        setUpRecyclerView(cardDetailsList);


        updateView(cardDetailsList);


    }

    private void setUpRecyclerView(ArrayList<CardDetails> cardDetailsList) {

        vehicleListRv.setLayoutManager(new LinearLayoutManager(this));
        mUserDetailAdapter.addAll(cardDetailsList);
        mUserDetailAdapter.setNewDivisionAdapterListener(mUserDetailAdapterListener);

    }

    UserDetailAdapter.UserDetailAdapterListener mUserDetailAdapterListener = new UserDetailAdapter.UserDetailAdapterListener() {
        @Override
        public void onSelected(CardDetails cardDetails) {

        }
    };

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener =  new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()){
                case R.id.if_other_cb:
                    if(isChecked){
                        ifOtherCbLl.setVisibility(View.VISIBLE);
                    }else {
                        ifOtherCbLl.setVisibility(View.GONE);
                    }
                    break;
            }

        }
    };

    private void initListener() {
        ifOtherCb.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private void initViews() {
        toolbar=  findViewById(R.id.toolbar);
        cardInfoTv = findViewById(R.id.card_info_tv);
        vehicleListRv = findViewById(R.id.vehicle_list_rv);
        ifOtherCb = findViewById(R.id.if_other_cb);
        vehicleNoTil = findViewById(R.id.vehicle_no_til);
        vehicleNoEt = findViewById(R.id.vehicle_no_et);
        fuelTypeRg = findViewById(R.id.fuel_type_rg);
        petrolRb = findViewById(R.id.petrol_rb);
        dieselRb = findViewById(R.id.diesel_rb);
        qtyTil = findViewById(R.id.qty_til);
        qtyNoEt = findViewById(R.id.qty_no_et);
        meterReadingTil = findViewById(R.id.meter_reading_til);
        meterReadingEt = findViewById(R.id.meter_reading_et);
        submitMbtn = findViewById(R.id.submit_mbtn);
        ifOtherCbLl =  findViewById(R.id.if_other_cb_ll);

    }

    private void updateView(ArrayList<CardDetails> cardDetailsList) {

        if(cardDetailsList!=null){
            if(cardDetailsList.size()>0){

                cardInfoTv.setText(cardDetailsList.get(0).getFullName()+" / "+ cardDetailsList.get(0).getCardsNO());
            }
        }
    }

}
