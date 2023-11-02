package com.utils;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static com.utils.BaseConstant.TEXT_STYLE_BOLD;
import static com.utils.BaseConstant.TEXT_STYLE_ITALIC;
import static com.utils.BaseConstant.TEXT_STYLE_MEDIUM;
import static com.utils.BaseConstant.TEXT_STYLE_NORMAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.custom.CustomScroller;
import com.widget.R;

import java.lang.reflect.Field;

import io.github.inflationx.calligraphy3.CalligraphyUtils;


public class ViewUtil {

    public static void setTextStyle(Context context, TextView textView, int textStyle) {
        if (textView != null && textStyle >= 0) {
            switch (textStyle) {
                case TEXT_STYLE_BOLD:
                    textView.setTypeface(null, Typeface.BOLD);
                    applyFont(context, textView, context.getString(R.string.path_font_bold));
                    break;
                case TEXT_STYLE_ITALIC:
                    textView.setTypeface(null, Typeface.ITALIC);
                    applyFont(context, textView, context.getString(R.string.path_font_italic));
                    break;
                case TEXT_STYLE_NORMAL:
                    textView.setTypeface(null, Typeface.NORMAL);
                    applyFont(context, textView, context.getString(R.string.path_font_normal));
                    break;
                case TEXT_STYLE_MEDIUM:
                    //textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    textView.setTypeface(null, Typeface.BOLD);
                    applyFont(context, textView, context.getString(R.string.path_font_medium));
                    break;
            }
        }
    }

    private static void applyFont(Context context, TextView textView, String filePath) {
        CalligraphyUtils.applyFontToTextView(context, textView, filePath);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void setBackground(View v, Drawable background) {
        if (v != null && background != null) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
                v.setBackgroundDrawable(background);
            else
                v.setBackground(background);

        }
    }

    public static void setImageDrawable(ImageView v, Drawable background) {
        if (v != null && background != null) {
            v.setImageDrawable(background);
        }
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with lottie height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(300);
        v.startAnimation(a);
    }

    public static void setViewpagerSpeed(Context context, ViewPager viewPager) {
        Field mScroller;
        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            CustomScroller scroller = new CustomScroller(context, new DecelerateInterpolator(), 500);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bundle getBundleAnim(Context context) {
        return ActivityOptionsCompat.makeCustomAnimation(context,
                R.anim.fade_in, R.anim.fade_out).toBundle();
    }

    public static void initSwipeRefreshLayout(Activity activity, SwipeRefreshLayout swipeRefreshLayout) {
        TypedValue typed_value = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.actionBarSize, typed_value, true);
        swipeRefreshLayout.setProgressViewOffset(false, 0, activity.getApplicationContext().getResources().getDimensionPixelSize(typed_value.resourceId));
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity.getApplicationContext(), R.color.swipeRefresh_1),
                ContextCompat.getColor(activity.getApplicationContext(), R.color.swipeRefresh_2),
                ContextCompat.getColor(activity.getApplicationContext(), R.color.swipeRefresh_3),
                ContextCompat.getColor(activity.getApplicationContext(), R.color.swipeRefresh_4)
        );
    }

    public static void startActivityTransition(Activity activity, Intent intent, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, view, activity.getString(R.string.transition));
            activity.startActivity(intent, options.toBundle());
        }
    }

    public static void startActivityTransition(Activity activity, Intent intent, View view, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, view, activity.getString(R.string.transition));
            activity.startActivityForResult(intent, requestCode, options.toBundle());
        }
    }

    public static boolean isNavigationBarAvailable() {

        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        return (!(hasBackKey && hasHomeKey));
    }

    public static float convertSpToPixels(Context context, float input) {
        return input / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float getDimen(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id) / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }

    public static void swapView(View viewShow, View viewGone) {
        if (viewShow != null && viewShow.getVisibility() != View.VISIBLE)
            viewShow.setVisibility(View.VISIBLE);
        if (viewGone != null && viewGone.getVisibility() != View.GONE)
            viewGone.setVisibility(View.GONE);
    }

    public static void goneView(View... args) {
        for (View arg : args)
            if (arg != null && arg.getVisibility() != View.GONE)
                arg.setVisibility(View.GONE);
    }

    public static void visibleView(View... args) {
        for (View arg : args)
            if (arg != null && arg.getVisibility() != View.GONE)
                arg.setVisibility(View.GONE);
    }

    /**
     * Làm nội dung full màn hình và Status bar trong suốt, ngoại trừ thanh điều hướng
     */
    public static void setContentFitsWindowTransparentStatusBar(Activity activity) {
        setContentFitsWindow(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setStatusBarColor(activity, R.color.transparent);
        else
            setStatusBarColor(activity, R.color.transparent25);
    }

    public static void setContentFitsWindow(Activity activity) {
        Window window = activity.getWindow();
        if (window != null)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void makeTranslucentStatusBar(Activity activity, boolean isTransparent) {
        if (isTransparent) {
            //Window w = activity.getWindow(); // in Activity's onCreate() for instance
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            //Window w = activity.getWindow(); // in Activity's onCreate() for instance
            //w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setLightStatusBar(Activity activity, boolean isLightStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = activity.getWindow().getDecorView();
            if (isLightStatusBar)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            else
                view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void setStatusBarColor(Activity activity, @ColorRes int id) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, id));
    }

    public static void setShadowColor(Context context, View view, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && view != null) {
            view.setOutlineSpotShadowColor(ContextCompat.getColor(context, color));
            view.setOutlineSpotShadowColor(ContextCompat.getColor(context, color));
        }
    }

    public static CircularProgressDrawable getProgressSmall(Context context) {
        return getProgress(context, R.dimen.sizeVeryExtraShort, R.dimen._10, R.color.colorAccent);
    }

    public static CircularProgressDrawable getProgressMedium(Context context) {
        return getProgress(context, R.dimen._3, R.dimen.sizeStandard, R.color.colorAccent);
    }

    public static CircularProgressDrawable getProgressLarge(Context context) {
        return getProgress(context, R.dimen.sizeExtraShort, R.dimen.sizeLarge, R.color.colorAccent);
    }

    public static CircularProgressDrawable getProgress(Context context, @DimenRes int strokeWidth, @DimenRes int centerRadius, @ColorRes int color) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(context, color));
        circularProgressDrawable.setStrokeWidth(context.getResources().getDimension(strokeWidth));
        circularProgressDrawable.setCenterRadius(context.getResources().getDimension(centerRadius));
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
