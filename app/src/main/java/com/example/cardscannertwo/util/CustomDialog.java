package com.example.cardscannertwo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cardscannertwo.R;


public class CustomDialog {

    private ProgressDialog progressDialogMessage;
    private Context context;
    private Activity mParentActivity;

    public CustomDialog(Context context) {
        progressDialogMessage = new ProgressDialog(context);
        this.context = context;
        mParentActivity = (Activity)context;


    }


    public ProgressDialog showLoadingDialogWithMessage(String msg) {
        progressDialogMessage.show();
        if (progressDialogMessage.getWindow() != null) {
            progressDialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        progressDialogMessage.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = progressDialogMessage.findViewById(R.id.loader_iv);
        TextView textView = progressDialogMessage.findViewById(R.id.msg_tv);

        textView.setText(msg);


        Glide.with(context).asGif()
                .load(R.drawable.loader)
                .placeholder(R.drawable.loader)

                .into(gifImageView);


        progressDialogMessage.setIndeterminate(true);
        progressDialogMessage.setCancelable(false);
        progressDialogMessage.setCanceledOnTouchOutside(false);
        return progressDialogMessage;
    }

    public void hideLoadingDialogWithMessage() {
        if (progressDialogMessage != null && progressDialogMessage.isShowing() && !mParentActivity.isFinishing()) {
            progressDialogMessage.dismiss();
        }
    }
}
