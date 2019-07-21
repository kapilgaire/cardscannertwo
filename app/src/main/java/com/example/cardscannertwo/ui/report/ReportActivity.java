package com.example.cardscannertwo.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.cardscannertwo.util.SimpleDividerItemDecoration;
import com.example.cardscannertwo.util.Toaster;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    Toolbar toolbar;

    private CustomDialog mCustomDialog;

    private ReportDetailAdapter mReportDetailAdapter;
    private TextView siteNameTv;
    private LinearLayout headerLl;
    private RecyclerView reportListRv;
    private TextView fuelTypeTv;
    private TextView totalTv;
    private Button homeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initViews();

        setSupportActionBar(toolbar);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.goToHome(ReportActivity.this);
            }
        });
        mCustomDialog = new CustomDialog(this);
        mReportDetailAdapter = new ReportDetailAdapter(this);
        reportListRv.setLayoutManager(new LinearLayoutManager(this));
        reportListRv.setAdapter(mReportDetailAdapter);
        reportListRv.addItemDecoration(new SimpleDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));


        getReport();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        siteNameTv = findViewById(R.id.site_name_tv);
        headerLl = findViewById(R.id.header_ll);
        reportListRv = findViewById(R.id.report_list_rv);

        fuelTypeTv = findViewById(R.id.fuel_type_tv);
        totalTv = findViewById(R.id.total_tv);
        homeBtn = findViewById(R.id.home_btn);

    }

    private void getReport() {

        ApiClient.getApiClient().getDayWiseReport("Dayal Honda Sutyana", "honda").enqueue(new Callback<ArrayList<ReportDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportDetails>> call, Response<ArrayList<ReportDetails>> response) {
                mCustomDialog.hideLoadingDialogWithMessage();
                if (response.code() == 200) {
                    ArrayList<ReportDetails> reportDetailsArrayList = response.body();

                    if (reportDetailsArrayList != null) {
                        if (reportDetailsArrayList.size() > 0) {
                            String msg = reportDetailsArrayList.get(0).getErrorMessage();

                            if (msg == null) {
                                Log.e("list", reportDetailsArrayList.toString());
                                mReportDetailAdapter.addAll(reportDetailsArrayList);

                                totalTv.setText(reportDetailsArrayList.get(0).getTotalfuel());
                                fuelTypeTv.setText(reportDetailsArrayList.get(0).getFuelType() + " : "
                                        + reportDetailsArrayList.get(0).getTotalfuel());
                            } else {


                                Toaster.show(ReportActivity.this, msg);

                            }
                        } else {
                            Toaster.show(ReportActivity.this, "No Report Available");

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

}
