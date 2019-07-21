package com.example.cardscannertwo.util;

import android.content.Context;
import android.content.Intent;

import com.example.cardscannertwo.ui.main.MainActivity;

public class AppUtil {

    public static void goToHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
