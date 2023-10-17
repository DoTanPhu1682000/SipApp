package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.exception.MyException;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.utils.LogUtil;

public class StatusLinearLayout extends LinearLayout implements StatusLayout {
    private Context mContext;

    private LayoutInflater inflater;

    private CircularProgressIndicator pbLoading;
    private ImageView image;
    private TextView tvTitle;
    private TextView tvDescription;
    private Button bRetry;
    private NestedScrollView scrollView;
    private NestedScrollView scrollViewLoading;

    private View viewStatus;
    private View viewLoading;

    private int loadingSize;
    private int loadingColor;
    private int imageWidth;
    private int imageHeight;
    private float titleSize;
    private ColorStateList titleColor;
    private float descriptionSize;
    private ColorStateList descriptionColor;
    private float buttonTextSize;
    private ColorStateList buttonTextColor;
    private int buttonBackground;
    private int background;
    private Drawable backgroundDrawable;
    private boolean fillViewport = true;

    public StatusLinearLayout(Context context) throws MyException {
        super(context);
        mContext = context;
    }

    public StatusLinearLayout(Context context, AttributeSet attrs) throws MyException {
        super(context, attrs);
        init(context, attrs);
    }

    public StatusLinearLayout(Context context, AttributeSet attrs, int defStyle) throws MyException {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.StatusLayout, 0, 0);
            try {

                loadingSize = ta.getDimensionPixelSize(R.styleable.StatusLayout_sl_loadingSize, 0);
                loadingColor = ta.getColor(R.styleable.StatusLayout_sl_loadingColor, 0);

                imageWidth = ta.getDimensionPixelSize(R.styleable.StatusLayout_sl_imageWidth, 0);
                imageHeight = ta.getDimensionPixelSize(R.styleable.StatusLayout_sl_imageHeight, 0);

                titleSize = ta.getDimension(R.styleable.StatusLayout_sl_titleSize, -1);
                titleColor = ta.getColorStateList(R.styleable.StatusLayout_sl_titleColor);

                descriptionSize = ta.getDimension(R.styleable.StatusLayout_sl_descriptionSize, -1);
                descriptionColor = ta.getColorStateList(R.styleable.StatusLayout_sl_descriptionColor);

                buttonTextSize = ta.getDimension(R.styleable.StatusLayout_sl_buttonTextSize, -1);
                buttonTextColor = ta.getColorStateList(R.styleable.StatusLayout_sl_buttonTextColor);
                buttonBackground = ta.getColor(R.styleable.StatusLayout_sl_buttonBackground, 0);

                //background = ta.getColor(R.styleable.StatusLayout_sl_background, ContextCompat.getColor(mContext, R.color.white));
                background = ta.getColor(R.styleable.StatusLayout_sl_background, 0);
                backgroundDrawable = ta.getDrawable(R.styleable.StatusLayout_sl_backgroundDrawable);

                fillViewport = ta.getBoolean(R.styleable.StatusLayout_sl_fillViewport, true);

            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            } finally {
                ta.recycle();
            }

        }

//        if (background == 0)
//            throw new Exception(mContext.getString(R.string.status_error_background_required));
    }

    @Override
    public void showLoading() {
        showLoadingLayout();
    }

    @Override
    public void showContent() {
        if (!isShowingContent()) {
            hideStatusLayout();
            hideLoadingLayout();
        }
    }

    @Override
    public void showEmpty() {
        showEmpty(null);
    }

    @Override
    public void showEmpty(OnClickListener buttonClickListener) {
        showInfo(mContext.getString(R.string.status_empty), mContext.getString(R.string.status_empty_description), R.drawable.ic_status_no_data, null, buttonClickListener);
    }

    @Override
    public void showRetry(OnClickListener buttonClickListener) {
        showRetry(null, null, buttonClickListener);
    }

    @Override
    public void showRetry(String description, OnClickListener buttonClickListener) {
        if (description == null)
            showRetry(buttonClickListener);
        else
            showInfo(mContext.getString(R.string.status_notice), description, R.drawable.ic_status_warning, null, buttonClickListener);
    }

    @Override
    public void showRetry(String title, String description, OnClickListener buttonClickListener) {
        showInfo(title, description, null, null, buttonClickListener);
    }

    @Override
    public void showErrorNetwork() {
        showErrorNetwork(null, null);
    }

    @Override
    public void showErrorNetwork(OnClickListener buttonClickListener) {
        showErrorNetwork(null, buttonClickListener);
    }

    @Override
    public void showErrorNetwork(String buttonText, OnClickListener buttonClickListener) {
        showInfo(mContext.getString(R.string.status_error_network), mContext.getString(R.string.status_error_network_description), R.drawable.ic_status_no_network, buttonText, buttonClickListener);
    }

    @Override
    public boolean isShowingContent() {
        boolean checkStatus = viewStatus != null && viewStatus.getVisibility() != GONE;
        boolean checkLoading = viewLoading != null && viewLoading.getVisibility() != GONE;

        return !(checkStatus || checkLoading);
    }

    private void showLoadingLayout() {
        inflateLoadingLayout();

        hideStatusLayout();

        if (viewLoading.getVisibility() != VISIBLE)
            viewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfo(String title, String description, @DrawableRes Integer resId, String buttonText, OnClickListener buttonClickListener) {
        inflateStatusLayout();

        hideLoadingLayout();

        if (viewStatus != null && viewStatus.getVisibility() != VISIBLE)
            viewStatus.setVisibility(VISIBLE);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else
            tvTitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(description)) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        } else
            tvDescription.setVisibility(View.GONE);

        if (resId != null) {
            image.setVisibility(View.VISIBLE);
            image.setImageDrawable(ContextCompat.getDrawable(mContext, resId));
        } else {
            image.setImageDrawable(null);
            image.setVisibility(View.GONE);
        }
        if (buttonClickListener != null) {
            bRetry.setVisibility(View.VISIBLE);
            bRetry.setOnClickListener(buttonClickListener);
        } else
            bRetry.setVisibility(View.INVISIBLE);

        if (!TextUtils.isEmpty(buttonText))
            bRetry.setText(buttonText);
    }

    private void inflateLoadingLayout() {
        if (viewLoading == null) {
            viewLoading = inflater.inflate(R.layout.layout_loading, null);
            pbLoading = viewLoading.findViewById(R.id.pbLoading);

            scrollViewLoading = viewLoading.findViewById(R.id.scrollViewLoading);
            setLayoutBackground(background);
            setLayoutBackgroundDrawable(backgroundDrawable);

            setFillViewport(fillViewport);

            setLoadingSize(loadingSize);
            setLoadingColor(loadingColor);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            addView(viewLoading, 0, layoutParams);
            viewLoading.setVisibility(GONE);
        }
    }

    private void inflateStatusLayout() {
        if (viewStatus == null) {
            viewStatus = inflater.inflate(R.layout.layout_status, null);
            pbLoading = viewStatus.findViewById(R.id.pbLoading);
            image = viewStatus.findViewById(R.id.image);
            tvTitle = viewStatus.findViewById(R.id.tvTitle);
            tvDescription = viewStatus.findViewById(R.id.tvDescription);
            bRetry = viewStatus.findViewById(R.id.bRetry);

            scrollView = viewStatus.findViewById(R.id.scrollView);
            setLayoutBackground(background);
            setLayoutBackgroundDrawable(backgroundDrawable);

            setImageWidth(imageWidth);
            setImageHeight(imageHeight);

            setTitleSize(titleSize);
            setTitleColor(titleColor);

            setDescriptionSize(descriptionSize);
            setDescriptionColor(descriptionColor);

            setButtonTextSize(buttonTextSize);
            setButtonTextColor(buttonTextColor);
            setButtonBackground(buttonBackground);
            setFillViewport(fillViewport);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            addView(viewStatus, 0, layoutParams);
            viewStatus.setVisibility(GONE);
        }
    }

    private void hideStatusLayout() {
        if (viewStatus != null && viewStatus.getVisibility() != GONE)
            viewStatus.setVisibility(GONE);
    }

    private void hideLoadingLayout() {
        if (viewLoading != null && viewLoading.getVisibility() != GONE)
            viewLoading.setVisibility(View.GONE);
    }

    private void setFillViewport(boolean fillViewport) {
        this.fillViewport = fillViewport;
        if (scrollView != null)
            scrollView.setFillViewport(fillViewport);
        if (scrollViewLoading != null)
            scrollViewLoading.setFillViewport(fillViewport);
    }

    /*----------------------------------[PUBLIC]--------------------------------------------------*/

    public void setLayoutBackground(int color) {
        this.background = color;
        if (background != 0 && scrollView != null)
            scrollView.setBackgroundColor(background);
        if (background != 0 && scrollViewLoading != null)
            scrollViewLoading.setBackgroundColor(background);
    }

    public void setLayoutBackgroundDrawable(Drawable drawable) {
        this.backgroundDrawable = drawable;
        if (backgroundDrawable != null && scrollView != null)
            scrollView.setBackground(backgroundDrawable);
        if (backgroundDrawable != null && scrollViewLoading != null)
            scrollViewLoading.setBackground(backgroundDrawable);
    }

    public void setButtonBackground(int buttonBackground) {
        this.buttonBackground = buttonBackground;
        if (bRetry != null && buttonBackground != 0)
            bRetry.setBackgroundColor(buttonBackground);
    }

    public void setTitleColor(ColorStateList color) {
        this.titleColor = color;
        if (tvTitle != null && color != null)
            tvTitle.setTextColor(color);
    }

    public void setDescriptionColor(ColorStateList color) {
        this.descriptionColor = color;
        if (tvDescription != null && color != null)
            tvDescription.setTextColor(color);
    }

    public void setButtonTextColor(ColorStateList color) {
        if (bRetry != null && color != null)
            bRetry.setTextColor(color);
    }

    public void setDescriptionSize(float descriptionSize) {
        this.descriptionSize = descriptionSize;
        if (tvDescription != null && descriptionSize > 0)
            tvDescription.setTextSize(descriptionSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        if (tvTitle != null && titleSize > 0)
            tvTitle.setTextSize(titleSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setButtonTextSize(float textSize) {
        this.buttonTextSize = textSize;
        if (bRetry != null && textSize > 0)
            bRetry.setTextSize(textSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        if (image != null && imageHeight > 0) {
            image.getLayoutParams().height = imageHeight;
            image.requestLayout();
        }
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        if (image != null && imageWidth > 0) {
            image.getLayoutParams().width = imageWidth;
            image.requestLayout();
        }
    }

    public void setLoadingColor(int loadingColor) {
        this.loadingColor = loadingColor;
        if (pbLoading != null && loadingColor != 0) {
            pbLoading.setIndicatorColor(loadingColor);
        }
    }

    public void setLoadingSize(int loadingSize) {
        this.loadingSize = loadingSize;
        if (pbLoading != null && loadingSize > 0) {
            pbLoading.getLayoutParams().width = loadingSize;
            pbLoading.getLayoutParams().height = loadingSize;
        }
    }
}
