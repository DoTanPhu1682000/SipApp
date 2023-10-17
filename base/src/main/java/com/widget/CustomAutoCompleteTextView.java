package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class CustomAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    private int mListCount = 4;

    public CustomAutoCompleteTextView(Context context) {
        super(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomAutoCompleteTextView, 0, 0);
        mListCount = a.getInteger(R.styleable.CustomAutoCompleteTextView_limit_item, 4);
        a.recycle();
    }


    @Override
    public void onFilterComplete(int count) {
        setDropDownHeight((count > mListCount ? mListCount : count) * getHeight());
        super.onFilterComplete(count);
    }
}