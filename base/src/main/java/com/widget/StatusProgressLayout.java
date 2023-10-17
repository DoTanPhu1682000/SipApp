package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.exception.MyException;

public class StatusProgressLayout extends StatusRelativeLayout {
    private View viewLoadingBottom;

    public StatusProgressLayout(Context context) throws MyException {
        super(context);
    }

    public StatusProgressLayout(Context context, AttributeSet attrs) throws MyException {
        super(context, attrs);
    }

    public StatusProgressLayout(Context context, AttributeSet attrs, int defStyle) throws MyException {
        super(context, attrs, defStyle);
    }


    public void showLoadingBottom() {
        inflateLoadingBottomLayout();

        super.hideLoadingLayout();
        super.hideStatusLayout();

        if (viewLoadingBottom.getVisibility() != VISIBLE)
            viewLoadingBottom.setVisibility(View.VISIBLE);
    }

    private void inflateLoadingBottomLayout() {
        if (viewLoadingBottom == null) {
            viewLoadingBottom = inflater.inflate(R.layout.layout_loading_bottom, null);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(ALIGN_PARENT_BOTTOM);

            addView(viewLoadingBottom, getChildCount(), layoutParams);
            viewLoadingBottom.setVisibility(GONE);
        }
    }

    private void hideLoadingBottomLayout() {
        if (viewLoadingBottom != null && viewLoadingBottom.getVisibility() != GONE)
            viewLoadingBottom.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        if (!isShowingContent()) {
            super.showContent();
            hideLoadingBottomLayout();
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        hideLoadingBottomLayout();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        hideLoadingBottomLayout();
    }

    @Override
    public void showEmpty(OnClickListener buttonClickListener) {
        super.showEmpty(buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public void showRetry(OnClickListener buttonClickListener) {
        super.showRetry(buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public void showRetry(String title, OnClickListener buttonClickListener) {
        super.showRetry(title, buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public void showRetry(String title, String description, OnClickListener buttonClickListener) {
        super.showRetry(title, description, buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public void showErrorNetwork() {
        super.showErrorNetwork();
        hideLoadingBottomLayout();
    }

    @Override
    public void showErrorNetwork(OnClickListener buttonClickListener) {
        super.showErrorNetwork(buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public void showErrorNetwork(String buttonText, OnClickListener buttonClickListener) {
        super.showErrorNetwork(buttonText, buttonClickListener);
        hideLoadingBottomLayout();
    }

    @Override
    public boolean isShowingContent() {
        return super.isShowingContent() && (viewLoadingBottom == null || viewLoadingBottom.getVisibility() == GONE);
    }
}
