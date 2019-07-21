package com.example.cardscannertwo.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;

public class FontUtil {

    public static SpannableString setTypeFaceToolbar(String text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new TypefaceSpan("sans-serif-condensed"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

}
