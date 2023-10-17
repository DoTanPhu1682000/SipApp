package com.task;

import android.os.Handler;
import android.os.Message;

public class TriggerHandler extends Handler {
    private OnTriggerHandlerListener onTriggerHandlerListener;
    private int code;

    public interface OnTriggerHandlerListener {
        void onTrigger(Message msg);
    }

    public TriggerHandler(OnTriggerHandlerListener onTriggerHandlerListener, int code) {
        this.code = code;
        this.onTriggerHandlerListener = onTriggerHandlerListener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (onTriggerHandlerListener != null && msg.what == code) {
            onTriggerHandlerListener.onTrigger(msg);
        }
    }

}
