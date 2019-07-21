package com.example.cardscannertwo.util;

import android.content.Context;
import android.widget.Toast;

import com.example.cardscannertwo.R;

import java.util.concurrent.TimeoutException;

public final class ErrorUtil {

    public static void showErrorView(Throwable throwable, Context context) {


        if (!NetworkHelperKt.isNetworkConnected(context)) {

            Toaster.show(context, context.getResources().getString(R.string.msg_internet_connection));
        } else {
            if (throwable instanceof TimeoutException) {
                Toaster.show(context, context.getResources().getString(R.string.msg_internet_connection));


            } else {
                Toaster.show(context, context.getResources().getString(R.string.error_network_msg));



            }
        }
    }

}
