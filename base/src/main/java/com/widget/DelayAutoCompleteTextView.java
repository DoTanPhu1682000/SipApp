package com.widget;

import android.content.Context;
import android.os.Message;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.task.TriggerHandler;

public class DelayAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    public interface OnDelayAutoCompleteTextViewListener {
        void onFilterStart();

        void onFilterComplete();
    }

    private OnDelayAutoCompleteTextViewListener onDelayAutoCompleteTextViewListener;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 500;
    private static final int CODE_TRIGGER = 100;

    private int delay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;

    private TriggerHandler mHandler = new TriggerHandler(new TriggerHandler.OnTriggerHandlerListener() {
        @Override
        public void onTrigger(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    }, CODE_TRIGGER);

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setOnDelayAutoCompleteTextViewListener(OnDelayAutoCompleteTextViewListener onDelayAutoCompleteTextViewListener) {
        this.onDelayAutoCompleteTextViewListener = onDelayAutoCompleteTextViewListener;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        delay = autoCompleteDelay;
    }

    @Override
    protected void replaceText(CharSequence text) {
        //ignore
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null)
            mLoadingIndicator.setVisibility(View.VISIBLE);

        if (onDelayAutoCompleteTextViewListener != null)
            onDelayAutoCompleteTextViewListener.onFilterStart();

        mHandler.removeMessages(CODE_TRIGGER);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(CODE_TRIGGER, text), delay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null)
            mLoadingIndicator.setVisibility(View.GONE);
        if (onDelayAutoCompleteTextViewListener != null)
            onDelayAutoCompleteTextViewListener.onFilterComplete();
        super.onFilterComplete(count);
    }
}