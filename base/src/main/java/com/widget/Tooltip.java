package com.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.utils.ViewUtil;

public class Tooltip {
    private static final int SUCCESS = 1;
    private static final int INF0 = 3;
    private static final int WARNING = 2;
    private static final int ERROR = 0;

    private Tooltip() {
        //private
    }

    public static void info(Context context, String text, View anchor) {
        createTooltip(context, text, anchor, INF0);
    }

    public static void success(Context context, String text, View anchor) {
        createTooltip(context, text, anchor, SUCCESS);
    }

    public static void error(Context context, String text, View anchor) {
        createTooltip(context, text, anchor, ERROR);
    }

    public static void warning(Context context, String text, View anchor) {
        createTooltip(context, text, anchor, WARNING);
    }

    private static void createTooltip(Context context, String text, View anchor, int type) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ToastColor.info(context, text);
            return;
        }

        Drawable background;

        switch (type) {
            case WARNING:
                background = ContextCompat.getDrawable(context, R.drawable.bg_help_warning);
                break;

            case ERROR:
                background = ContextCompat.getDrawable(context, R.drawable.bg_help_error);
                break;
            case SUCCESS:
                background = ContextCompat.getDrawable(context, R.drawable.bg_help_success);
                break;
            default:
                background = ContextCompat.getDrawable(context, R.drawable.bg_help_info);
                break;
        }

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            View popupView = layoutInflater.inflate(R.layout.popup_help, null);
            TextView customTvTitle;
//            ButtonImageView customBiClose;
//            customBiClose = popupView.findViewById(R.id.customBiClose);
            customTvTitle = popupView.findViewById(R.id.customTvTitle);


            ViewUtil.setBackground(customTvTitle, background);
            customTvTitle.setText(text);

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setOutsideTouchable(true);
            //popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_white_border_grey_round));
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // here I want to close the pw when clicking outside it but
                    // at all this is just an example of how it works and you can
                    // implement the onTouch() or the onKey() you want
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popupWindow.dismiss();
                        return true;
                    }
                    return false;
                }

            });
//            customBiClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
            if (anchor != null)
                popupWindow.showAsDropDown(anchor, 0, 0);
        }

    }

}
