package com.example.cardscannertwo.util;

import android.content.Context;
import android.widget.Toast;

import com.example.cardscannertwo.R;

import java.util.concurrent.TimeoutException;

public final class ErrorUtil {

    public static void showErrorView(Throwable throwable, Context context) {


        if (!NetworkHelperKt.isNetworkConnected(context)) {
            Toast.makeText(context, context.getResources().getString(R.string.msg_internet_connection), Toast.LENGTH_SHORT).show();
        } else {
            if (throwable instanceof TimeoutException) {
                Toast.makeText(context, context.getResources().getString(R.string.msg_internet_connection), Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, context.getResources().getString(R.string.error_network_msg), Toast.LENGTH_SHORT).show();

            }
        }
    }

}
