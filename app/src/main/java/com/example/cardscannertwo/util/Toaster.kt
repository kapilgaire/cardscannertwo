package com.example.cardscannertwo.util

import android.content.Context
import android.graphics.PorterDuff
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cardscannertwo.R

object Toaster {
    @JvmStatic  fun show(context: Context, text: CharSequence) {
        val toast =Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.view.background.setColorFilter(
                ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN
        )
        val textView = toast.view.findViewById(android.R.id.message) as TextView
        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        toast.show()
    }
}