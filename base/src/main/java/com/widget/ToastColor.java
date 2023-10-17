package com.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.utils.LogUtil;
import com.utils.ViewUtil;

public class ToastColor {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    private static final int SUCCESS = 1;
    private static final int INF0 = 3;
    private static final int WARNING = 2;
    private static final int ERROR = 0;

    private ToastColor() {
        //private
    }

    public static void show(Context context, String msg, int duration, int type) {
        createToast(context, msg, duration, type, false, false);
    }

    public static void showIcon(Context context, String msg, int duration, int type) {
        createToast(context, msg, duration, type, true, false);
    }

    public static void showHtml(Context context, String msg, int duration, int type) {
        createToast(context, msg, duration, type, false, true);
    }

    public static void showIconHtml(Context context, String msg, int duration, int type) {
        createToast(context, msg, duration, type, true, true);
    }
    /*-----------------------------[WARNING]------------------------------------------------------*/

    public static void info(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, INF0, false, false);
    }

    public static void info(Context context, String msg, int duration) {
        createToast(context, msg, duration, INF0, false, false);
    }

    public static void infoIcon(Context context, String msg, int duration) {
        createToast(context, msg, duration, INF0, true, false);
    }

    public static void infoHtml(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, INF0, false, false);
    }

    public static void infoHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, INF0, false, false);
    }

    public static void infoIconHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, INF0, true, false);
    }
    /*-----------------------------[WARNING]------------------------------------------------------*/

    public static void success(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, SUCCESS, false, false);
    }

    public static void success(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, false, false);
    }

    public static void successIcon(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, true, false);
    }

    public static void successHtml(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, SUCCESS, false, true);
    }

    public static void successHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, false, true);
    }

    public static void successIconHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, true, true);
    }
    /*-----------------------------[WARNING]------------------------------------------------------*/


    public static void warning(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, WARNING, false, false);
    }

    public static void warning(Context context, String msg, int duration) {
        createToast(context, msg, duration, WARNING, false, false);
    }

    public static void warningIcon(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, true, false);
    }

    public static void warningHtml(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, WARNING, false, true);
    }

    public static void warningHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, WARNING, false, true);
    }

    public static void warningIconHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS, true, true);
    }


    /*-----------------------------[ERROR]--------------------------------------------------------*/
    public static void error(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, ERROR, false, false);
    }

    public static void error(Context context, String msg, int duration) {
        createToast(context, msg, duration, ERROR, false, false);
    }

    public static void errorIcon(Context context, String msg, int duration) {
        createToast(context, msg, duration, ERROR, true, false);
    }

    public static void errorHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, ERROR, false, true);
    }

    public static void errorIconHtml(Context context, String msg, int duration) {
        createToast(context, msg, duration, ERROR, true, true);
    }

    public static void errorHtml(Context context, String msg) {
        createToast(context, msg, ToastColor.LENGTH_LONG, ERROR, false, true);
    }

    private static void createToast(Context context, String msg, int duration, int type, boolean hasIcon, boolean html) {

        Drawable background;
        int icon;

        switch (type) {
            case WARNING:
                icon = R.drawable.ic_alert_white;
                background = ContextCompat.getDrawable(context, R.drawable.bg_toast_warning);
                break;

            case ERROR:
                icon = R.drawable.ic_x_white;
                background = ContextCompat.getDrawable(context, R.drawable.bg_toast_error);
                break;
            case SUCCESS:
                icon = R.drawable.ic_tick_white;
                background = ContextCompat.getDrawable(context, R.drawable.bg_toast_success);
                break;
            default:
                icon = R.drawable.ic_info_white;
                background = ContextCompat.getDrawable(context, R.drawable.bg_toast_info);
                break;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View layout = inflater.inflate(R.layout.custom_toast_color, null);

            LinearLayout layoutContainer = layout.findViewById(R.id.layoutContainer);
            ViewUtil.setBackground(layoutContainer, background);

            TextView text = layout.findViewById(R.id.customTvText);
            text.setText(html ? HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY) : msg);
            try {
                if (hasIcon) {
                    ImageView customImgIcon = layout.findViewById(R.id.customImgIcon);
                    customImgIcon.setVisibility(View.VISIBLE);
                    customImgIcon.setImageResource(icon);
                }
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

            Toast toast = new Toast(context);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        }
    }
}
