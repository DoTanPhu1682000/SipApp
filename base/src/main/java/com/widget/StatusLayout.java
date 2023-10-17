package com.widget;

import android.view.View;

import androidx.annotation.DrawableRes;

public interface StatusLayout {
    void showLoading();

    void showContent();

    void showEmpty();

    void showEmpty(View.OnClickListener buttonClickListener);

    void showRetry(View.OnClickListener buttonClickListener);

    void showRetry(String title, View.OnClickListener buttonClickListener);

    void showRetry(String title, String description, View.OnClickListener buttonClickListener);

    void showErrorNetwork();

    void showErrorNetwork(View.OnClickListener buttonClickListener);

    void showErrorNetwork(String buttonText, View.OnClickListener buttonClickListener);

    void showInfo(String title, String description, @DrawableRes Integer resId, String buttonText, View.OnClickListener buttonClickListener);

    boolean isShowingContent();

}
