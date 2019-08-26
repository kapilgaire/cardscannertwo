package com.example.cardscannertwo.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.response.ReportDetails;
import com.example.cardscannertwo.util.AppUtil;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.SharedPrefUtil;
import com.example.cardscannertwo.util.SimpleDividerItemDecoration;
import com.example.cardscannertwo.util.Toaster;
import com.google.gson.Gson;



import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private CustomDialog mCustomDialog;

    private ReportDetailAdapter mReportDetailAdapter;
    private ReportDetailAdapter mDeiselReportDetailAdapter;
    private TextView siteNameTv;
    private LinearLayout headerLl;
    private RecyclerView reportListRv, report_list_diesel_rv;
    private TextView fuelTypeTv;
    //    private TextView totalTv;
    private static final String TAG = "ReportActivity";
//    private TextView totalPetrolTv;

    //    private Button homeBtn;
    private TextView fuelTypeTwoTv;

    private LinearLayout noDataLl;
    private LinearLayout dataLayoutLl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initViews();
        toolbar.setTitle(AppUtil.setTypeFaceToolbar("Today's Report"));

        setSupportActionBar(toolbar);

//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppUtil.goToHome(ReportActivity.this);
//            }
//        });

        siteNameTv.setText(SharedPrefUtil.getString(ReportActivity.this, "HONDA_PREF", "SITE_NAME"));
        mCustomDialog = new CustomDialog(this);
        mReportDetailAdapter = new ReportDetailAdapter(this);

        reportListRv.setLayoutManager(new LinearLayoutManager(this));
        reportListRv.setAdapter(mReportDetailAdapter);
        reportListRv.addItemDecoration(new SimpleDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 35));

        mDeiselReportDetailAdapter = new ReportDetailAdapter(this);
        report_list_diesel_rv.setLayoutManager(new LinearLayoutManager(this));
        report_list_diesel_rv.setAdapter(mDeiselReportDetailAdapter);
        report_list_diesel_rv.addItemDecoration(new SimpleDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 35));
//

        getReport();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        siteNameTv = findViewById(R.id.site_name_tv);
//        headerLl = findViewById(R.id.header_ll);
        reportListRv = findViewById(R.id.report_list_rv);
        report_list_diesel_rv = findViewById(R.id.report_list_diesel_rv);
        fuelTypeTwoTv = findViewById(R.id.fuel_type_two_tv);

        fuelTypeTv = findViewById(R.id.fuel_type_tv);
//        totalTv = findViewById(R.id.total_tv);
//        homeBtn = findViewById(R.id.home_btn);
        noDataLl = findViewById(R.id.no_data_ll);

//        totalPetrolTv = findViewById(R.id.total_petrol_tv);
        dataLayoutLl = findViewById(R.id.data_layout_ll);

    }

    private void getReport() {
        mCustomDialog.showLoadingDialogWithMessage("Loading...");

        ApiClient.getApiClient().getDayWiseReport(SharedPrefUtil.getString(ReportActivity.this, "HONDA_PREF", "SITE_NAME"),
                SharedPrefUtil.getString(ReportActivity.this, "HONDA_PREF", "KEY"))
                .enqueue(new Callback<ArrayList<ReportDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReportDetails>> call, Response<ArrayList<ReportDetails>> response) {
                        mCustomDialog.hideLoadingDialogWithMessage();
                        if (response.code() == 200) {
                            ArrayList<ReportDetails> reportDetailsArrayList = response.body();

                            Log.e(TAG, "onResponse: "+ new Gson().toJson(reportDetailsArrayList) );


                            if (reportDetailsArrayList != null) {
                                if (reportDetailsArrayList.size() > 0) {
                                    String msg = reportDetailsArrayList.get(0).getErrorMessage();

                                    if (msg == null) {

                                        showDataLayot();
                                        hideNoDataLayot();

                                        String deisel = "0", petrol = "0";


                                        for (ReportDetails rd : reportDetailsArrayList) {
                                            if (rd.getFuelType().equalsIgnoreCase("Petrol")) {

                                                mReportDetailAdapter.add(rd);

                                                petrol = rd.getTotalfuel();

                                            } else {
                                                mDeiselReportDetailAdapter.add(rd);
                                                deisel = rd.getTotalfuel();
                                            }
                                        }
                                        fuelTypeTv.setText("Petrol" + " : " + petrol + " Ltrs.");
                                        fuelTypeTwoTv.setText("Diesel " + deisel + " Ltrs.");
                                       /* String total="0";

                                        try {
                                            total = String.valueOf(Double.parseDouble(deisel)+ Double.parseDouble(petrol));

                                        }catch (Exception e){


                                            Log.e(TAG, "onResponse: "+e.getMessage() );
                                        }*/
//                                        totalTv.setText(deisel);
//                                        totalPetrolTv.setText(petrol);


                                    } else {

                                        showNoDataLayot();
                                        hideDataLayot();

                                        Toaster.show(ReportActivity.this, msg);

                                    }
                                } else {
                                    Toaster.show(ReportActivity.this, "No Report Available");

                                    showNoDataLayot();
                                    hideDataLayot();


                                }
                            }


                        } else {
                            Toaster.show(ReportActivity.this, String.valueOf(response.code()));

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReportDetails>> call, Throwable t) {
                        mCustomDialog.hideLoadingDialogWithMessage();

                        ErrorUtil.showErrorView(t, ReportActivity.this);
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
            AppUtil.goToHome(ReportActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void hideDataLayot() {
        if (dataLayoutLl.getVisibility() == View.VISIBLE) {
            dataLayoutLl.setVisibility(View.GONE);
        }

    }


    private void showDataLayot() {
        if (dataLayoutLl.getVisibility() == View.GONE) {
            dataLayoutLl.setVisibility(View.VISIBLE);
        }

    }


    private void hideNoDataLayot() {
        if (noDataLl.getVisibility() == View.VISIBLE) {
            noDataLl.setVisibility(View.GONE);
        }

    }

    private void showNoDataLayot() {
        if (noDataLl.getVisibility() == View.GONE) {
            noDataLl.setVisibility(View.VISIBLE);
        }

    }

}
