package com.example.cardscannertwo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.response.CardDetails;
import com.example.cardscannertwo.ui.detail.DetailActivity;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.Toaster;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText editText;
    private Timer timer;
    private final long DELAY = 350; // milliseconds
    private TextView noTv;

    private CustomDialog mCustomDialog;
    private ImageView gifHomeIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mCustomDialog  = new CustomDialog(this);

        initViews();

        /*setting home gif image*/
        Glide.with(this).asGif()
                .load(R.drawable.home)
                .placeholder(R.drawable.home)

                .into(gifHomeIv);


        intListener();


    }

    private void intListener() {
        editText.addTextChangedListener(mTextWatcher);
//        gifHomeIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, DetailActivity.class));
//            }
//        });
    }

    private void initViews() {
        editText = findViewById(R.id.edit_text);
        noTv = findViewById(R.id.no_tv);
        gifHomeIv =  findViewById(R.id.gif_home_iv);

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > 0) {
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
//                                    Log.e(TAG, "afterTextChanged: " + editText.getText().toString().trim());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        noTv.setText(editText.getText().toString().trim());
                                        getCardDetail(editText.getText().toString().trim(), "honda");
                                        editText.setText("");
                                    }
                                });


                            }
                        },
                        DELAY


                );
            }
        }
    };


    public void getCardDetail(String cardno, String key) {

        mCustomDialog.showLoadingDialogWithMessage("getting details...");
        ApiClient.getApiClient().getCardDetails(cardno, key).enqueue(new Callback<ArrayList<CardDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<CardDetails>> call, Response<ArrayList<CardDetails>> response) {

                mCustomDialog.hideLoadingDialogWithMessage();
                if (response.code() == 200) {
                    ArrayList<CardDetails> cardDetailsList = response.body();

                    if (cardDetailsList != null) {
                        if (cardDetailsList.size() > 0) {
                            String msg = cardDetailsList.get(0).getErrorMessage();

                            if (msg == null) {

//                                Log.e(TAG, "onResponse: " + cardDetailsList.toString());

                                Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
                                intentDetail.putParcelableArrayListExtra("CARD_DETAIL_LIST", cardDetailsList);
                                startActivity(intentDetail);
                            } else {


                                Toaster.show(MainActivity.this, msg);

                            }
                        } else {
                            Toaster.show(MainActivity.this, "Card Does Not Exist");

                        }
                    }


                }else {
                    Toaster.show(MainActivity.this, String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CardDetails>> call, Throwable t) {
                mCustomDialog.hideLoadingDialogWithMessage();

                ErrorUtil.showErrorView(t, MainActivity.this);
            }
        });

    }


}
