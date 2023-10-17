package com.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.widget.R;


public class ProgressDialogUtil {
    public static AlertDialog showProgressDialog(Activity activity) {
        return showProgressDialog(activity, null);
    }

    public static AlertDialog showProgressDialog(Activity activity, boolean setCancelable, boolean setCanceledOnTouchOutside) {
        return showProgressDialog(activity, null, setCancelable, setCanceledOnTouchOutside);
    }

    public static void updateProgressDialog(AlertDialog dialog, String text) {
        if (dialog == null) return;
        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        if (tvDialogTitle != null) {
            if (!TextUtils.isEmpty(text)) {
                tvDialogTitle.setText(text);
                tvDialogTitle.setVisibility(View.VISIBLE);
            } else
                tvDialogTitle.setVisibility(View.GONE);
        }
    }

    public static AlertDialog showProgressDialog(Activity activity, String text) {
        return showProgressDialog(activity, text, false, false);
    }

    public static AlertDialog showProgressDialog(Activity activity, String text, boolean setCancelable, boolean setCanceledOnTouchOutside) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(R.layout.dialog_progress);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Hide navigation bar in FullScreen
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(setCancelable);
        dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);

        if (!TextUtils.isEmpty(text)) {
            TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
            if (tvDialogTitle != null) {
                tvDialogTitle.setText(text);
                tvDialogTitle.setVisibility(View.VISIBLE);
            }
        }
        return dialog;
    }

    public static void hideProgressDialog(AlertDialog dialog) {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.cancel();
        } catch (Exception e) {
            //ignore
        }

    }
}
