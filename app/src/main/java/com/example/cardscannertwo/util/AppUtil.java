package com.example.cardscannertwo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cardscannertwo.R;
import com.example.cardscannertwo.data.response.SiteName;
import com.example.cardscannertwo.ui.scan.CardScannerActivity;

import java.util.ArrayList;

public class AppUtil {

    public static void goToHome(Context context) {
        Intent intent = new Intent(context, CardScannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void setSpinner(Spinner spinner, final Context context, final ArrayList<SiteName> siteNameArrayList) {
        ArrayAdapter<SiteName> spinnerArrayAdapter = new ArrayAdapter<SiteName>(
                context, R.layout.spinner_item_two, siteNameArrayList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                parent.setPadding(0, 0, 0, 0);

                convertView = super.getView(position, convertView, parent);
                TextView tv = (TextView) convertView;
                tv.setText(siteNameArrayList.get(position).getSiteName());
                tv.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));


                tv.setPadding(20, 20, 20, 20);
                tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                tv.setTextColor(context.getResources().getColor(R.color.black));


                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                parent.setPadding(0, 0, 0, 0);

                convertView = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) convertView;
                tv.setText(siteNameArrayList.get(position).getSiteName());
                tv.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));


                if (position == 0) {

                    tv.setPadding(15, 15, 15, 15);
                    tv.setBackgroundColor(context.getResources().getColor(R.color.black));
                    tv.setTextColor(context.getResources().getColor(R.color.white));
                } else {


                    tv.setPadding(20, 20, 20, 20);
                    tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                    tv.setTextColor(context.getResources().getColor(R.color.black));

                }
                return convertView;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_two);

        spinner.setAdapter(spinnerArrayAdapter);
    }

    public static SpannableString setTypeFaceToolbar(String text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new TypefaceSpan("sans-serif-condensed"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if(imm!=null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
