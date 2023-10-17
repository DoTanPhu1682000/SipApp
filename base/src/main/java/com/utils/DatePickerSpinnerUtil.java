package com.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.dialog.DatePickerSpinnerDialog;
import com.widget.R;

import java.util.Calendar;

public class DatePickerSpinnerUtil {

    public interface OnDatePickerListener {
        void onDateSet(int dayOfMonth, int monthOfYear, int year);
    }

    public static void createAndShowDatePicker(Activity activity, @Nullable Calendar minDate, @Nullable Calendar maxDate, Calendar mDefaultDate, final OnDatePickerListener listener) {
        Calendar now = mDefaultDate == null ? Calendar.getInstance() : mDefaultDate;
        DatePickerDialog dpd = DatePickerSpinnerDialog.newInstance(activity,
                new DatePickerSpinnerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (listener != null)
                            listener.onDateSet(dayOfMonth, month, year);
                    }

                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        if (minDate != null) {
            dpd.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }
        if (maxDate != null) {
            dpd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        Window window = dpd.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dpd.setCancelable(true);
        dpd.setCanceledOnTouchOutside(true);
        dpd.show();

        Button btnPositive = dpd.getButton(DatePickerDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        Button btnNegative = dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(activity, R.color.disable));

        try {
            LinearLayout parentP = (LinearLayout) btnPositive.getParent();
            parentP.setGravity(Gravity.CENTER_HORIZONTAL);
            View spacerP = parentP.getChildAt(1);
            spacerP.setVisibility(View.GONE);
            LinearLayout parentN = (LinearLayout) btnNegative.getParent();
            parentN.setGravity(Gravity.CENTER_HORIZONTAL);
            View spacerN = parentN.getChildAt(1);
            spacerN.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }

        double width = getScreenWidth(activity);
        if (width > 0) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_white_round));
            width = width / 4;
            window.setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //Hide navigation bar in FullScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        dpd.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    private static void createAndShowDatePicker(Activity activity, @Nullable int[] defaultDate_ddMMyyyy, @Nullable Calendar minDate, @Nullable Calendar maxDate, @Nullable final OnDatePickerListener listener) {
        if (defaultDate_ddMMyyyy == null)
            return;

        Calendar now = Calendar.getInstance();
        now.set(defaultDate_ddMMyyyy[2], defaultDate_ddMMyyyy[1], defaultDate_ddMMyyyy[0]);
        DatePickerDialog dpd = DatePickerSpinnerDialog.newInstance(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (listener != null) {
                            listener.onDateSet(dayOfMonth, month, year);
                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        if (minDate != null) {
            dpd.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }
        if (maxDate != null) {
            dpd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        Window window = dpd.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dpd.setCancelable(true);
        dpd.setCanceledOnTouchOutside(true);
        dpd.show();
        Button btnPositive = dpd.getButton(DatePickerDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        Button btnNegative = dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(activity, R.color.disable));

        try {
            LinearLayout parentP = (LinearLayout) btnPositive.getParent();
            parentP.setGravity(Gravity.CENTER_HORIZONTAL);
            View spacerP = parentP.getChildAt(1);
            spacerP.setVisibility(View.GONE);
            LinearLayout parentN = (LinearLayout) btnNegative.getParent();
            parentN.setGravity(Gravity.CENTER_HORIZONTAL);
            View spacerN = parentN.getChildAt(1);
            spacerN.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }

        double width = getScreenWidth(activity);
        if (width > 0) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_white_round));
            width = width / 4;
            window.setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //Hide navigation bar in FullScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        dpd.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static void createAndShowDatePickerData(Activity activity, String formatDate, String date, @Nullable Calendar minDate, @Nullable Calendar maxDate, @Nullable final OnDatePickerListener listener) {
        Calendar calendar = CalendarUtil.setTimeCalendar(formatDate, date);
        int[] intDate = new int[]{calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};

        createAndShowDatePicker(activity, intDate, minDate, maxDate, listener);
    }


    public static int getScreenWidth(@NonNull Activity activity) {
        return getScreenWidth(activity, 0);
    }

    public static int getScreenWidth(@NonNull Activity activity, int defaultValue) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
                Insets insets = windowMetrics.getWindowInsets()
                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
                return windowMetrics.getBounds().width() - insets.left - insets.right;
            } else {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.widthPixels;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
