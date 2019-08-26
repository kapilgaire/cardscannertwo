package com.example.cardscannertwo.ui.scan;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.remote.ApiClient;
import com.example.cardscannertwo.data.response.CardDetails;
import com.example.cardscannertwo.mifare.MifareBlock;
import com.example.cardscannertwo.mifare.MifareClassCard;
import com.example.cardscannertwo.mifare.MifareSector;
import com.example.cardscannertwo.mifare.StringUtils;
import com.example.cardscannertwo.ui.detail.DetailActivity;
import com.example.cardscannertwo.ui.report.ReportActivity;
import com.example.cardscannertwo.util.CustomDialog;
import com.example.cardscannertwo.util.ErrorUtil;
import com.example.cardscannertwo.util.SharedPrefUtil;
import com.example.cardscannertwo.util.Toaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardScannerActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;

    private static final String TAG = "CardScannerActivity";

    private EditText editText;
    private Timer timer;
    private final long DELAY = 350; // milliseconds
    private TextView noTv;

    private CustomDialog mCustomDialog;
    private ImageView gifHomeIv;

    private Button reportBtn;

    private String[][] mTechLists;
    private boolean isFirst = true;


    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, getResources().getString(R.string.no_nfc),
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, getResources().getString(R.string.open_nfc),
                    Toast.LENGTH_SHORT).show();
            finish();


            Log.e(TAG, "enabled");
            return;
        }

        Log.e(TAG, " NFC " + String.valueOf(nfcAdapter.isEnabled()));
        setContentView(R.layout.activity_scan);


        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef};

        //设置
        mTechLists = new String[][]{new String[]{IsoDep.class
                .getName()}, new String[]{MifareClassic.class.getName()}};//MifareClassic

        mCustomDialog = new CustomDialog(this);

        initViews();

        /*setting home gif image*/
//        Glide.with(this).asGif()
//                .load(R.drawable.home)
//                .placeholder(R.drawable.home)
//
//                .into(gifHomeIv);


        intListener();


    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
        if (isFirst) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
                String result = processIntent(getIntent());
//                editText.setText(result);

                Log.e(TAG, "Read Result  " + result);
            }
            isFirst = false;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            String result = processIntent(intent);
//            editText.setText(result);


        }
    }

    /**
     * @param intent
     * @return
     */
    @SuppressLint("NewApi")
    private String processIntent(Intent intent) {
       /* Parcelable[] rawmsgs = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawmsgs[0];
        NdefRecord[] records = msg.getRecords();
        String resultStr = new String(records[0].getPayload());*/


        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mifareClassic = MifareClassic.get(tag);
        if (mifareClassic == null) {

            Log.e(TAG, "It is not a Mifare card");
        }

        MifareClassCard mifareClassCard = null;

        try {
            mifareClassic.connect();

            boolean auth = false;
            // 5.2) and get the number of sectors this card has..and loop
            // thru these sectors
            int secCount = mifareClassic.getSectorCount();
            mifareClassCard = new MifareClassCard(secCount);
            int bCount = 0;
            int bIndex = 0;

            for (int j = 0; j < secCount; j++) {
                MifareSector mifareSector = new MifareSector();
                mifareSector.sectorIndex = j;
                // 6.1) authenticate the sector
                auth = mifareClassic.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_DEFAULT);
                mifareSector.authorized = auth;
                if (auth) {
                    // 6.2) In each sector - get the block count
                    bCount = mifareClassic.getBlockCountInSector(j);
                    bCount = Math.min(bCount, MifareSector.BLOCKCOUNT);
                    bIndex = mifareClassic.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {

                        // 6.3) Read the block
                        byte[] data = mifareClassic.readBlock(bIndex);
                        MifareBlock mifareBlock = new MifareBlock(data);
                        mifareBlock.blockIndex = bIndex;
                        // 7) Convert the data into a string from Hex
                        // format.

                        bIndex++;
                        mifareSector.blocks[i] = mifareBlock;


                    }
                    mifareClassCard.setSector(mifareSector.sectorIndex,
                            mifareSector);
                } else { // Authentication failed - Handle it

                }
            }

            ArrayList<String> blockData = new ArrayList<String>();

            int blockIndex = 0;
            for (int i = 0; i < secCount; i++) {

                MifareSector mifareSector = mifareClassCard.getSector(i);
                for (int j = 0; j < MifareSector.BLOCKCOUNT; j++) {
                    MifareBlock mifareBlock = mifareSector.blocks[j];
                    byte[] data = mifareBlock.getData();
                    blockData.add("Block " + blockIndex++ + " : " + StringUtils.bytesToHexString(data));
                }
            }

            //---------------------显示输出---------------------
            String[] contents = new String[blockData.size()];
            blockData.toArray(contents);
//            listView.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, contents));

            Log.e(TAG, String.valueOf(contents));


            mifareClassic.close();

        } catch (IOException e) {
                Log.e(TAG,"Exception "+ e.getLocalizedMessage());
        }

        return "";
    }


    private void intListener() {
        editText.addTextChangedListener(mTextWatcher);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CardScannerActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });
//        gifHomeIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CardScannerActivity.this, DetailActivity.class));
//            }
//        });
    }

    private void initViews() {
        editText = findViewById(R.id.edit_text);
        noTv = findViewById(R.id.no_tv);
        gifHomeIv = findViewById(R.id.gif_home_iv);
        reportBtn = findViewById(R.id.report_btn);

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
                                        getCardDetail(editText.getText().toString().trim(),
                                                SharedPrefUtil.getString(CardScannerActivity.this, "HONDA_PREF", "KEY"),
                                                SharedPrefUtil.getString(CardScannerActivity.this, "HONDA_PREF", "SITE_NAME"));
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


    public void getCardDetail(String cardno, String key, String siteName) {

        mCustomDialog.showLoadingDialogWithMessage("getting details...");
        ApiClient.getApiClient().getCardDetails(cardno, key, siteName).enqueue(new Callback<ArrayList<CardDetails>>() {
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

                                Intent intentDetail = new Intent(CardScannerActivity.this, DetailActivity.class);
                                intentDetail.putParcelableArrayListExtra("CARD_DETAIL_LIST", cardDetailsList);
                                startActivity(intentDetail);
                            } else {


                                Toaster.show(CardScannerActivity.this, msg);

                            }
                        } else {
                            Toaster.show(CardScannerActivity.this, "Card Does Not Exist");

                        }
                    }


                } else {
                    Toaster.show(CardScannerActivity.this, String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CardDetails>> call, Throwable t) {
                mCustomDialog.hideLoadingDialogWithMessage();

                ErrorUtil.showErrorView(t, CardScannerActivity.this);
            }
        });

    }


}
