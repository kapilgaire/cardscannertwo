package com.example.cardscannertwo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.response.SiteName;
import com.example.cardscannertwo.ui.scan.CardScannerActivity;
import com.example.cardscannertwo.util.AppUtil;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.SharedPrefUtil;
import com.example.cardscannertwo.util.Toaster;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView siteNameTv;
    private FrameLayout siteNameFl;
    private Spinner siteNameSpinner;
    private Button submit_btn;

    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefUtil.getString(MainActivity.this, "HONDA_PREF", "KEY") != null) {
            startActivity(new Intent(MainActivity.this, CardScannerActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
        initViews();
        toolbar.setTitle(AppUtil.setTypeFaceToolbar("Select Site"));
        setSupportActionBar(toolbar);
        mCustomDialog = new CustomDialog(this);



        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SiteName siteName = (SiteName) siteNameSpinner.getSelectedItem();

                if (siteName.getSiteName().equalsIgnoreCase("Select Site name")) {
                    Toaster.show(MainActivity.this, "Select Site Name");
                } else {

                    SharedPrefUtil.setString(MainActivity.this, "HONDA_PREF",
                            "SITE_NAME",
                            siteName.getSiteName());
                    SharedPrefUtil.setString(MainActivity.this,
                            "HONDA_PREF", "KEY", siteName.getGUIDKey());

                    Toaster.show(MainActivity.this, "Success");

                    startActivity(new Intent(MainActivity.this, CardScannerActivity.class));
                    finish();
                }

            }
        });

        getSiteDetails();

    }


    private void getSiteDetails() {

        mCustomDialog.showLoadingDialogWithMessage("Loading...");

        ApiClient.getApiClient().getsiteDetails().enqueue(new Callback<ArrayList<SiteName>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteName>> call, Response<ArrayList<SiteName>> response) {
                mCustomDialog.hideLoadingDialogWithMessage();
                if (response.code() == 200) {
                    ArrayList<SiteName> siteNameArrayList = response.body();

                    if (siteNameArrayList != null) {
                        if (siteNameArrayList.size() > 0) {
                            String msg = siteNameArrayList.get(0).getMessage();

                            if (msg == null) {
                                siteNameArrayList.add(0, new SiteName("Select Site name"));
                                AppUtil.setSpinner(siteNameSpinner, MainActivity.this, siteNameArrayList);

                            } else {


                                Toaster.show(MainActivity.this, msg);

                            }
                        } else {
                            Toaster.show(MainActivity.this, "No Site Details Available");

                        }
                    }


                } else {
                    Toaster.show(MainActivity.this, String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<SiteName>> call, Throwable t) {
                mCustomDialog.hideLoadingDialogWithMessage();
                ErrorUtil.showErrorView(t, MainActivity.this);

            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        siteNameTv = findViewById(R.id.site_name_tv);
        siteNameFl = findViewById(R.id.site_name_fl);
        siteNameSpinner = findViewById(R.id.site_name_spinner);
        submit_btn = findViewById(R.id.submit_btn);
    }

}
