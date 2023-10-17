package com.widget;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utils.ViewUtil;

public class BaseCustomView extends LinearLayout {
    public BaseCustomView(Context context) {
        super(context);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setFont(Context context, TextView textView, int textStyle) {
        ViewUtil.setTextStyle(context, textView, textStyle);
    }
}
