package com.example.cardscannertwo.ui.detail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.request.RequestBuilder;
import com.example.cardscannertwo.data.response.CardDetails;
import com.example.cardscannertwo.data.response.SuccessResponse;
import com.example.cardscannertwo.data.response.TransactionDetails;
import com.example.cardscannertwo.util.AppUtil;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.SharedPrefUtil;
import com.example.cardscannertwo.util.SimpleDividerItemDecoration;
import com.example.cardscannertwo.util.Toaster;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<CardDetails> cardDetailsList;

    private TextView cardInfoTv;
    private RecyclerView vehicleListRv;
    private CheckBox ifOtherCb;
    private TextInputLayout vehicleNoTil;
    private EditText vehicleNoEt;
    private RadioGroup fuelTypeRg;
    private RadioButton petrolRb;
    private RadioButton dieselRb, fuelTypeRb;
    private TextInputLayout qtyTil;
    private EditText qtyNoEt;
    private TextInputLayout meterReadingTil;
    private EditText meterReadingEt;
    private Button submitMbtn;
    private LinearLayout ifOtherCbLl;
    private RecyclerView fuelTransactionListRv;

    private Button reportBtn;


    private UserDetailAdapter mUserDetailAdapter;
    private TransactionAdapter mTransactionAdapter;

    private static final String TAG = "DetailActivity";

    private String cardNo = null;
    private String fuelType = null;
    private String registrationNo = null;

    private CustomDialog mCustomDialog;
//    private Button homeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cardDetailsList = getIntent().getParcelableArrayListExtra("CARD_DETAIL_LIST");

        initViews();
        toolbar.setTitle(AppUtil.setTypeFaceToolbar(cardDetailsList.get(0).getCardsNO()));
        setSupportActionBar(toolbar);

        initListener();
        mUserDetailAdapter = new UserDetailAdapter(this);
        mCustomDialog = new CustomDialog(this);
        mTransactionAdapter = new TransactionAdapter(this);
        fuelTransactionListRv.setLayoutManager(new LinearLayoutManager(this));
        fuelTransactionListRv.setAdapter(mTransactionAdapter);
        fuelTransactionListRv.addItemDecoration(new SimpleDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));


        setUpRecyclerView(cardDetailsList);


        updateView(cardDetailsList);

        getFuelDetail();

    }


    private void setUpRecyclerView(ArrayList<CardDetails> cardDetailsList) {

        Log.e(TAG, "setUpRecyclerView: " + cardDetailsList.toString());

        vehicleListRv.setLayoutManager(new LinearLayoutManager(this));
        mUserDetailAdapter.addAll(cardDetailsList);
        vehicleListRv.setAdapter(mUserDetailAdapter);
        mUserDetailAdapter.setNewDivisionAdapterListener(mUserDetailAdapterListener);


    }

    UserDetailAdapter.UserDetailAdapterListener mUserDetailAdapterListener = new UserDetailAdapter.UserDetailAdapterListener() {
        @Override
        public void onSelected(CardDetails cardDetails) {

            cardNo = cardDetails.getCardsNO();
            fuelType = cardDetails.getFuelType();
            registrationNo = cardDetails.getRegistrationNo();

            ifOtherCb.setChecked(false);


        }
    };

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.if_other_cb:
                    if (isChecked) {
                        mUserDetailAdapter.setRow_index(-1);
                        ifOtherCbLl.setVisibility(View.VISIBLE);
                    } else {
                        ifOtherCbLl.setVisibility(View.GONE);
                    }
                    break;
            }

        }
    };
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.submit_mbtn:
                    submit();
                    break;
//                case R.id.report_btn:
//
//                    Intent intent = new Intent(DetailActivity.this, ReportActivity.class);
//                    startActivity(intent);
//                    break;

//                case R.id.home_btn:
//                    AppUtil.goToHome(DetailActivity.this);
//                    break;
            }
        }
    };
    TextWatcher mVehicleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                vehicleNoTil.setError(null);
            }
        }
    };
    TextWatcher mMeterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                meterReadingTil.setError(null);
            }
        }
    };
    TextWatcher mQtyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                qtyTil.setError(null);
            }
        }
    };

    private void initListener() {
        ifOtherCb.setOnCheckedChangeListener(mOnCheckedChangeListener);
        submitMbtn.setOnClickListener(mOnClickListener);
        vehicleNoEt.addTextChangedListener(mVehicleTextWatcher);
        qtyNoEt.addTextChangedListener(mQtyTextWatcher);
        meterReadingEt.addTextChangedListener(mMeterTextWatcher);
//        reportBtn.setOnClickListener(mOnClickListener);
//        homeBtn.setOnClickListener(mOnClickListener);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
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
        ifOtherCbLl = findViewById(R.id.if_other_cb_ll);
        fuelTransactionListRv = findViewById(R.id.fuel_transaction_list_rv);
        reportBtn = findViewById(R.id.report_btn);
//        homeBtn = findViewById(R.id.home_btn);

    }

    private void updateView(ArrayList<CardDetails> cardDetailsList) {

        if (cardDetailsList != null) {
            if (cardDetailsList.size() > 0) {

                cardInfoTv.setText(cardDetailsList.get(0).getFullName() + " / " + cardDetailsList.get(0).getCardsNO());
            }
        }
    }

    private void submit() {

        if (ifOtherCb.isChecked()) {

            String meterReading = meterReadingEt.getText().toString();
            String quantity = qtyNoEt.getText().toString();
            String vehicleNo = vehicleNoEt.getText().toString();

            // get selected radio button from radioGroup
            int selectedId = fuelTypeRg.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            fuelTypeRb = findViewById(selectedId);

            String fuelType = fuelTypeRb.getText().toString();

            if (vehicleNo.isEmpty()) {
                vehicleNoTil.setError("Enter Vehicle No.");

            } else if (quantity.isEmpty()) {
                qtyTil.setError("Enter Quantity(ltrs.)");

            } else if (meterReading.isEmpty()) {
                meterReadingTil.setError("Enter Meter Reading");

            } else if (TextUtils.isEmpty(cardDetailsList.get(0).getCardsNO())) {

                Toaster.show(this, "Select card");
            } else if (fuelType.isEmpty()) {
                Toaster.show(this, "Select Fuel Type");
            } else {
                save(meterReading, quantity, cardNo, fuelType, vehicleNo, "true");
            }
        } else {
            String meterReading = meterReadingEt.getText().toString();
            String quantity = qtyNoEt.getText().toString();

            if (quantity.isEmpty()) {
                qtyTil.setError("Enter Quantity(ltrs.)");

            } else if (meterReading.isEmpty()) {
                meterReadingTil.setError("Enter Meter Reading");

            } else if (TextUtils.isEmpty(cardNo)) {
                Toaster.show(this, "Select Vehicle");
            } else if (TextUtils.isEmpty(fuelType)) {
                Toaster.show(this, "Select Vehicle");
            } else if (TextUtils.isEmpty(registrationNo)) {
                Toaster.show(this, "Select Vehicle");
            } else {
                save(meterReading, quantity, cardNo, fuelType, registrationNo, "false");
            }
        }


    }

    private void save(String meterReading, String quantity, String cardNo, String fuelType, String registrationNo, String ifOther) {


        mCustomDialog.showLoadingDialogWithMessage("Saving...");


        RequestBuilder requestBuilder = new RequestBuilder.RequestParameterBuilder()
                .setCardNo(cardNo)
                .setMeterReading(meterReading)
                .setQuantity(quantity)
                .setFuelType(fuelType)
                .setIfOther(ifOther)
                .setRegistrationNo(registrationNo)
                .setKey(SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "KEY"))
                .setSiteName(SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "SITE_NAME")).build();

        Log.e(TAG, "save: " + new Gson().toJson(requestBuilder));

        ApiClient.getApiClient().save(requestBuilder).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                mCustomDialog.hideLoadingDialogWithMessage();
                if (response.code() == 200) {

                    SuccessResponse successResponse = response.body();
                    Log.e(TAG, new Gson().toJson(successResponse));

                    if (successResponse != null) {
                        if (successResponse.getMessage().equalsIgnoreCase("success")) {
                            Toaster.show(DetailActivity.this, successResponse.getMessage());
                            ifOtherCb.setChecked(false);
                            vehicleNoEt.setText("");

                            /*hide after submit folrm*/
                            mUserDetailAdapter.clear();
                            ifOtherCbLl.setVisibility(View.GONE);
                            ifOtherCb.setVisibility(View.GONE);
                            qtyTil.setVisibility(View.GONE);
                            meterReadingTil.setVisibility(View.GONE);
                            submitMbtn.setVisibility(View.GONE);

                            AppUtil.hideKeyboard(DetailActivity.this);
                            getFuelDetail();
                        } else {
                            Toaster.show(DetailActivity.this, successResponse.getMessage());

                        }
                    }
                } else {
                    Toaster.show(DetailActivity.this, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                mCustomDialog.hideLoadingDialogWithMessage();
                ErrorUtil.showErrorView(t, DetailActivity.this);


            }
        });
    }

    private void getFuelDetail() {

        ApiClient.getApiClient().getCardFuelDetails(cardDetailsList.get(0).getCardsNO(),
                SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "KEY"),
                SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "SITE_NAME")).enqueue(new Callback<ArrayList<TransactionDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<TransactionDetails>> call, Response<ArrayList<TransactionDetails>> response) {
                mCustomDialog.hideLoadingDialogWithMessage();
                if (response.code() == 200) {
                    ArrayList<TransactionDetails> transactionDetailList = response.body();

                    if (transactionDetailList != null) {
                        if (transactionDetailList.size() > 0) {
                            String msg = transactionDetailList.get(0).getErrorMessage();

                            if (msg == null) {

                                mTransactionAdapter.clear();
                                mTransactionAdapter.addAll(transactionDetailList);
//
                            } else {


                                Toaster.show(DetailActivity.this, msg);

                            }
                        } else {

                            ifOtherCb.setChecked(true);
                            //no Fuel transaction
                        }
                    }


                } else {
                    Toaster.show(DetailActivity.this, String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<TransactionDetails>> call, Throwable t) {
                mCustomDialog.hideLoadingDialogWithMessage();
                ErrorUtil.showErrorView(t, DetailActivity.this);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Functionality if we press back button
        if (id == R.id.action_home) {
            AppUtil.goToHome(DetailActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
