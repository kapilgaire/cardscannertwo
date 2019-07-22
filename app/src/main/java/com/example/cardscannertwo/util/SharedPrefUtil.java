package com.example.cardscannertwo.util;

import android.content.Context;
import android.content.SharedPreferences;

final public class SharedPrefUtil {
    private SharedPrefUtil() {
    }


    public static boolean clearPrefrence(Context context, String prefName) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            return editor.commit();
        }
        return false;
    }


    public static boolean setString(Context context, String prefName, String prefKey, String datas) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(prefKey, datas);


            return edit.commit();
        }
        return false;
    }



    public static String getString(Context context, String prefName, String prefKey) {
        String value = null;
        SharedPreferences preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (preferences != null) {
            value = preferences.getString(prefKey, null);
        }
        return value;

    }
}
