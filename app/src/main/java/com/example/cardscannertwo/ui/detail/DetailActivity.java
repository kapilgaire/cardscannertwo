package com.example.cardscannertwo.ui.detail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.request.RequestBuilder;
import com.example.cardscannertwo.data.response.CardDetails;
import com.example.cardscannertwo.data.response.SuccessResponse;
import com.example.cardscannertwo.util.AppUtil;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.SharedPrefUtil;
import com.example.cardscannertwo.util.Toaster;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.rscja.deviceapi.Printer;
import com.rscja.deviceapi.exception.ConfigurationException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final int TIMER = 20000;
    private Toolbar toolbar;
    private ArrayList<CardDetails> cardDetailsList;

    private TextView cardInfoTv;
    private RelativeLayout formLayoutRl;
    private RecyclerView vehicleListRv;
    private CheckBox ifOtherCb;
    private LinearLayout ifOtherCbLl;
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
    private LinearLayout detailLayoutLl;
    private TextView regNoTvDetail;
    private TextView fuelTypeTvDetail;
    private TextView quantityTvDetail;
    private TextView meterReadingTvDetail;

    private FrameLayout parentFl;

    private LinearLayout llCb;


    private UserDetailAdapter mUserDetailAdapter;

    private static final String TAG = "DetailActivity";

    private String cardNo = null;
    private String fuelType = null;
    private String registrationNo = null;

    private CustomDialog mCustomDialog;

    private Button printBtn;
    private Button exitBtn;
    private CountDownTimer countDownTimer;


    /*printer*/
    boolean isLeisure = true;
    boolean isLackofpaper = false;
    boolean isNORMAL = true;
    public boolean runing = false;
    public Printer mPrinter;
    public int leftMargin = 0;
    public int rightMargin = 0;
    public int rowSpacing = 33;

    public boolean isItalic = false;
    public boolean isFrame = false;
    public boolean isBold = false;
    public boolean isdoubleWidth = false;
    public boolean isDoubleHigh = false;
    public boolean isUnderline = false;
    public boolean isWhite = false;

    public int speed = 1;
    public int gray = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cardDetailsList = getIntent().getParcelableArrayListExtra("CARD_DETAIL_LIST");

        try {
            mPrinter = Printer.getInstance();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        mPrinter.setPrinterStatusCallBack(new CallBack());

        initViews();
//        toolbar.setTitle(AppUtil.setTypeFaceToolbar(cardDetailsList.get(0).getCardsNO()));
        setSupportActionBar(toolbar);

        initListener();
        mUserDetailAdapter = new UserDetailAdapter(this);
        mCustomDialog = new CustomDialog(this);
//        mTransactionAdapter = new TransactionAdapter(this);
//        fuelTransactionListRv.setLayoutManager(new LinearLayoutManager(this));
//        fuelTransactionListRv.setAdapter(mTransactionAdapter);
//        fuelTransactionListRv.addItemDecoration(new SimpleDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));


        setUpRecyclerView(cardDetailsList);


        updateView(cardDetailsList);

//        getFuelDetail();


        showFormLayot();
        hideDetailLayot();

        startTimer(TIMER);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mPrinter != null) {
            mPrinter.free();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new InitTask().execute();

        mPrinter.setPrinterStatusCallBackEnable(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPrinter.clearCache();
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
            resetTimer();

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
                        resetTimer();
                        llCb.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        ifOtherCb.setTextColor(getResources().getColor(R.color.white));

                    } else {

                        llCb.setBackgroundColor(getResources().getColor(R.color.white));
                        ifOtherCb.setTextColor((getResources().getColor(R.color.dark_grey)));
                        ifOtherCbLl.setVisibility(View.GONE);
                        resetTimer();

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

                case R.id.exit_btn:
                    AppUtil.goToHome(DetailActivity.this);
                    stopCountdown();
                    break;
                case R.id.print_btn:

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
            stopCountdown();

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
            stopCountdown();
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

            stopCountdown();

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

        printBtn.setOnClickListener(mOnClickListener);
        exitBtn.setOnClickListener(mOnClickListener);


        parentFl.setOnTouchListener(mOnTouchListener);
//        reportBtn.setOnClickListener(mOnClickListener);
//        homeBtn.setOnClickListener(mOnClickListener);
    }

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Log.e(TAG, "touch x,y == " + event.getX() + "," + event.getY());
            resetTimer();
            return true;
        }

    };

    private void resetTimer() {
        stopCountdown();
        startTimer(TIMER);
    }

    private void initViews() {
        parentFl = findViewById(R.id.parent_fl);
        llCb = findViewById(R.id.ll_cb);

        toolbar = findViewById(R.id.toolbar);
        cardInfoTv = findViewById(R.id.card_info_tv);
        formLayoutRl = findViewById(R.id.form_layout_rl);
        vehicleListRv = findViewById(R.id.vehicle_list_rv);
        ifOtherCb = findViewById(R.id.if_other_cb);
        ifOtherCbLl = findViewById(R.id.if_other_cb_ll);
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
        detailLayoutLl = findViewById(R.id.detail_layout_ll);
        regNoTvDetail = findViewById(R.id.reg_no_tv_detail);
        fuelTypeTvDetail = findViewById(R.id.fuel_type_tv_detail);
        quantityTvDetail = findViewById(R.id.quantity_tv_detail);
        meterReadingTvDetail = findViewById(R.id.meter_reading_tv_detail);
        printBtn = findViewById(R.id.print_btn);
        exitBtn = findViewById(R.id.exit_btn);

    }

    private void updateView(ArrayList<CardDetails> cardDetailsList) {

        if (cardDetailsList != null) {
            if (cardDetailsList.size() > 0) {

                cardInfoTv.setText(cardDetailsList.get(0).getFullName() + " \n " + cardDetailsList.get(0).getCardsNO());
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
                save(meterReading, quantity, cardDetailsList.get(0).getCardsNO(), fuelType, vehicleNo, "true");
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

    private void save(final String meterReading, final String quantity, final String cardNo, final String fuelType, final String registrationNo, String ifOther) {

        resetTimer();

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
                resetTimer();

                if (response.code() == 200) {

                    SuccessResponse successResponse = response.body();
                    Log.e(TAG, new Gson().toJson(successResponse));

                    if (successResponse != null) {
                        if (successResponse.getMessage().equalsIgnoreCase("success")) {
                            Toaster.show(DetailActivity.this, successResponse.getMessage());
                            ifOtherCb.setChecked(false);
                            vehicleNoEt.setText("");

                            /*hide after submit folrm*/
//                            mUserDetailAdapter.clear();
//                            ifOtherCbLl.setVisibility(View.GONE);
//                            ifOtherCb.setVisibility(View.GONE);
//                            qtyTil.setVisibility(View.GONE);
//                            meterReadingTil.setVisibility(View.GONE);
//                            submitMbtn.setVisibility(View.GONE);


                            hideFormLayot();
                            showDetailLayot();

                            updateDetaillayout(registrationNo, fuelType, quantity, meterReading);
                            AppUtil.hideKeyboard(DetailActivity.this);


//                            getFuelDetail();
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
                resetTimer();


            }
        });
    }

    private void updateDetaillayout(String registrationNo, String fuelType, String quantity, String meterReading) {

        regNoTvDetail.setText(registrationNo);
        fuelTypeTvDetail.setText(fuelType);
        quantityTvDetail.setText(quantity);
        meterReadingTvDetail.setText(meterReading);
    }

//    private void getFuelDetail() {
//
//        ApiClient.getApiClient().getCardFuelDetails(cardDetailsList.get(0).getCardsNO(),
//                SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "KEY"),
//                SharedPrefUtil.getString(DetailActivity.this, "HONDA_PREF", "SITE_NAME")).enqueue(new Callback<ArrayList<TransactionDetails>>() {
//            @Override
//            public void onResponse(Call<ArrayList<TransactionDetails>> call, Response<ArrayList<TransactionDetails>> response) {
//                mCustomDialog.hideLoadingDialogWithMessage();
//                if (response.code() == 200) {
//                    ArrayList<TransactionDetails> transactionDetailList = response.body();
//
//                    if (transactionDetailList != null) {
//                        if (transactionDetailList.size() > 0) {
//                            String msg = transactionDetailList.get(0).getErrorMessage();
//
//                            if (msg == null) {
//
//                                mTransactionAdapter.clear();
//                                mTransactionAdapter.addAll(transactionDetailList);
////
//                            } else {
//
//
//                                Toaster.show(DetailActivity.this, msg);
//
//                            }
//                        } else {
//
//                            ifOtherCb.setChecked(true);
//                            //no Fuel transaction
//                        }
//                    }
//
//
//                } else {
//                    Toaster.show(DetailActivity.this, String.valueOf(response.code()));
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<TransactionDetails>> call, Throwable t) {
//                mCustomDialog.hideLoadingDialogWithMessage();
//                ErrorUtil.showErrorView(t, DetailActivity.this);
//
//            }
//        });
//    }

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
            stopCountdown();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideFormLayot() {
        if (formLayoutRl.getVisibility() == View.VISIBLE) {
            formLayoutRl.setVisibility(View.GONE);
        }

    }


    private void showFormLayot() {
        if (formLayoutRl.getVisibility() == View.GONE) {
            formLayoutRl.setVisibility(View.VISIBLE);
        }

    }


    private void hideDetailLayot() {
        if (detailLayoutLl.getVisibility() == View.VISIBLE) {
            detailLayoutLl.setVisibility(View.GONE);
        }

    }

    private void showDetailLayot() {
        if (detailLayoutLl.getVisibility() == View.GONE) {
            detailLayoutLl.setVisibility(View.VISIBLE);
        }

    }


    //Stop Countdown method
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method
    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                Log.e(TAG, "onTick: " + hms);
            }

            public void onFinish() {
                Log.e(TAG, "onTick: " + "TIME'S UP!!");

                countDownTimer = null;//set CountDownTimer to null

                AppUtil.goToHome(DetailActivity.this);
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopCountdown();

    }

    class AutoPrinter extends Thread {
        boolean isContinuous = false;
        String data = "";
        int size = 50;

        private AutoPrinter(String data, boolean isContinuous) {
            this.isContinuous = isContinuous;
            this.data = data;
        }

        public void run() {
            mPrinter.clearCache();
            do {
                for (int k = 0; (k < data.length()) && runing; ) {
                    if (isLeisure) {
                        int flagDataLen = data.length() - k;
                        int Statr = k;
                        int end = flagDataLen > size ? k + size : k + flagDataLen;
                        k = end;
                        String temp = data.substring(Statr, end);
                        mPrinter.print(temp);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (runing && isContinuous);
            printFeed();
            runing = false;
        }
    }

    class CallBack implements Printer.PrinterStatusCallBack {
        @Override
        public void message(Printer.PrinterStatus infoCode) {
            switch (infoCode) {
                case NORMAL:
                    if (isLackofpaper)
                        setMeg(getString(R.string.printNormal));
                    isLackofpaper = false;
                    isNORMAL = true;
                    break;
                case OVERPRESSURE:
                    isNORMAL = false;
                    setMeg(getString(R.string.printOverpressure));
                    break;
                case LACKOFPAPER:
                    isNORMAL = false;
                    isLackofpaper = true;
                    setMeg(getString(R.string.printLackofpaper));
                    break;
                case OVERHEATING://过热
                    isNORMAL = false;
                    setMeg(getString(R.string.printOverheating));
                    break;
                case PRESSUREAXISOPEN:
                    isNORMAL = false;
                    setMeg(getString(R.string.printPressureaxisopen));
                    break;
                case PAPERSTUCK:
                    isNORMAL = false;
                    setMeg(getString(R.string.printPaperstuck));
                    break;
                case SLICINGERROR:
                    isNORMAL = false;
                    setMeg(getString(R.string.printSlicingerror));
                    break;
                case PAPERFINISH:
                    isNORMAL = false;
                    setMeg(getString(R.string.printPaperfinish));
                    break;
                case CANCELPAPER:
                    isNORMAL = false;
                    setMeg(getString(R.string.printCancelpaper));
                    break;
                case LEISURE:
                    isLeisure = true;
                    setMeg(getString(R.string.printLeisure));
                    break;
                case UNLEISURED:
                    isNORMAL = false;
                    isLeisure = false;
                    setMeg(getString(R.string.printUnleisure));
                    break;
            }
        }
    }

    private void setMeg(String msg) {
        Toaster.show(getApplicationContext(), msg);
    }

    public void printFeed() {
        mPrinter.setPrintRowSpacing(33);
        mPrinter.setFeedRow(2);
        mPrinter.setPrintRowSpacing(rowSpacing);
    }

    public void initParameter() {
        mPrinter.setPrintRowSpacing(rowSpacing);
        mPrinter.setPrintLeftMargin(leftMargin);
        mPrinter.setPrintRightMargin(rightMargin);
        mPrinter.setPrintGrayLevel(gray + 1);
        mPrinter.setPrintSpeed(speed);
        mPrinter.setPrintCharacterStyle(isItalic, isFrame, isBold, isdoubleWidth, isDoubleHigh, isWhite, isUnderline);
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mPrinter.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(DetailActivity.this, "Printer fail",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailActivity.this, "Printer init success", Toast.LENGTH_SHORT).show();
                initParameter();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
