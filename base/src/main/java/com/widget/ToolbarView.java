package com.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.utils.LogUtil;
import com.utils.ViewUtil;


public class ToolbarView extends BaseCustomView implements View.OnClickListener {

    private static final int TYPE_SINGLE_BUTTON = 0;
    private static final int TYPE_DOUBLE_BUTTON = 1;
    private static final int TYPE_SINGLE_BACK = 3;
    private static final int TYPE_DOUBLE_TEXT = 2;

    private OnClickSingleButtonListener onClickSingleButtonListener;
    private OnClickDoubleButtonListener onClickDoubleButtonListener;

    private Space customPaddingLeft;
    private Space customPaddingRight;
    private ButtonImageView customBiLeft;
    private ButtonImageView customBiRight;
    private TextView customTvTitle;
    private TextView customTvRight;
    private ProgressBar customProgressBar;
    private Toolbar customToolbar;

    private int type = TYPE_SINGLE_BUTTON;
    private Context context;

    /*---------------------------------[INTERFACE]------------------------------------------------*/

    public interface OnClickSingleButtonListener {
        void onClickLeftButton();

    }

    public interface OnClickDoubleButtonListener {
        void onClickLeftButton();

        void onClickRightButton();
    }
    /*---------------------------------[GETTER-SETTER]--------------------------------------------*/

    public void setOnClickSingleButtonListener(OnClickSingleButtonListener onClickSingleButtonListener) {
        this.onClickSingleButtonListener = onClickSingleButtonListener;
    }

    public void setOnClickDoubleButtonListener(OnClickDoubleButtonListener onClickDoubleButtonListener) {
        this.onClickDoubleButtonListener = onClickDoubleButtonListener;
    }

    /*---------------------------------[CONSTRUCTION]---------------------------------------------*/
    public ToolbarView(Context context) {
        super(context);
        init(context, null);
    }

    public ToolbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToolbarView, 0, 0);
            try {
                type = ta.getInteger(R.styleable.ToolbarView_toolbar_type, TYPE_SINGLE_BUTTON);
                switch (type) {
                    case TYPE_SINGLE_BACK:
                    case TYPE_SINGLE_BUTTON:
                        inflater.inflate(R.layout.custom_toolbar_single_button, this);
                        break;
                    case TYPE_DOUBLE_BUTTON:
                        inflater.inflate(R.layout.custom_toolbar_double_button, this);
                        customBiRight = findViewById(R.id.customBiRight);
                        customBiRight.setOnClickListener(this);
                        break;
                    case TYPE_DOUBLE_TEXT:
                        inflater.inflate(R.layout.custom_toolbar_double_text, this);
                        customTvRight = findViewById(R.id.customTvRight);
                        customTvRight.setOnClickListener(this);
                        break;
                    default:
                        break;
                }
                initData(ta);
            } finally {
                ta.recycle();
            }
        }
    }

    private void initData(TypedArray ta) {
        customToolbar = findViewById(R.id.customToolbar);
        customBiLeft = findViewById(R.id.customBiLeft);
        customTvTitle = findViewById(R.id.customTvTitle);
        customProgressBar = findViewById(R.id.customProgressBar);
        customPaddingLeft = findViewById(R.id.customPaddingLeft);
        customPaddingRight = findViewById(R.id.customPaddingRight);

        customBiLeft.setOnClickListener(this);
        if (type == TYPE_SINGLE_BACK) {
            setIconLeft(ContextCompat.getDrawable(context, R.drawable.ic_back));
        }

        Drawable iconLeft = ta.getDrawable(R.styleable.ToolbarView_toolbar_iconLeft);
        float iconLeftSize = ta.getDimension(R.styleable.ToolbarView_toolbar_iconLeftSize, -1);
        setIconLeft(iconLeft);
        setIconLeftSize(iconLeftSize);

        Drawable iconRight = ta.getDrawable(R.styleable.ToolbarView_toolbar_iconRight);
        float iconRightSize = ta.getDimension(R.styleable.ToolbarView_toolbar_iconRightSize, -1);
        setIconRight(iconRight);
        setIconRightSize(iconRightSize);

        String text = ta.getString(R.styleable.ToolbarView_toolbar_text);
        float textSize = ta.getDimension(R.styleable.ToolbarView_toolbar_textSize, -1);
        ColorStateList textColor = ta.getColorStateList(R.styleable.ToolbarView_toolbar_textColor);
        ColorStateList iconLeftColor = ta.getColorStateList(R.styleable.ToolbarView_toolbar_iconLeftColor);
        ColorStateList iconRightColor = ta.getColorStateList(R.styleable.ToolbarView_toolbar_iconRightColor);
        int textStyle = ta.getInteger(R.styleable.ToolbarView_toolbar_textStyle, -1);
        setText(text);
        setTextSize(textSize);
        setTextColor(textColor);
        setTextStyle(textStyle);

        ColorStateList progressbarColor = ta.getColorStateList(R.styleable.ToolbarView_toolbar_progressbarColor);
        setProgressbarColor(progressbarColor);
        Drawable background = ta.getDrawable(R.styleable.ToolbarView_toolbar_background);
        setBackground(background);

        setIconLeftColor(iconLeftColor);
        setIconRightColor(iconRightColor);

        String textRight = ta.getString(R.styleable.ToolbarView_toolbar_textRight);
        float textRightSize = ta.getDimension(R.styleable.ToolbarView_toolbar_textRightSize, -1);
        ColorStateList textRightColor = ta.getColorStateList(R.styleable.ToolbarView_toolbar_textRightColor);
        setTextRight(textRight);
        setTextRightColor(textRightColor);
        setTextRightSize(textRightSize);

        float paddingLeft = ta.getDimension(R.styleable.ToolbarView_toolbar_paddingLeft, -1);
        float paddingRight = ta.getDimension(R.styleable.ToolbarView_toolbar_paddingRight, -1);
        setPaddingLeft(paddingLeft);
        setPaddingRight(paddingRight);
    }


    /*--------------------------------------[PRIVATE METHOD]--------------------------------------*/
    private static Activity scanForActivity(Context cont) {
        try {
            if (cont == null)
                return null;
            else if (cont instanceof Activity)
                return (Activity) cont;
            else if (cont instanceof ContextWrapper)
                return scanForActivity(((ContextWrapper) cont).getBaseContext());
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    public void setPaddingLeft(float paddingLeft) {
        if (customPaddingLeft != null && paddingLeft >= 0) {
            LayoutParams params = new LayoutParams(
                    (int) paddingLeft,
                    LayoutParams.WRAP_CONTENT
            );
            customPaddingLeft.setLayoutParams(params);
        }
    }

    public void setPaddingRight(float paddingRight) {
        if (customPaddingRight != null && paddingRight >= 0) {
            LayoutParams params = new LayoutParams(
                    (int) paddingRight,
                    LayoutParams.WRAP_CONTENT
            );
            customPaddingRight.setLayoutParams(params);
        }
    }

    public TextView getTextView() {
        return customTvTitle;
    }

    public void setTextStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(context, customTvTitle, textStyle);
    }

    public void setBackground(Drawable backgroundDrawable) {
        ViewUtil.setBackground(customToolbar, backgroundDrawable);
    }

    public void setIconRight(Drawable iconRight) {
        if (customBiRight != null && iconRight != null) {
            customBiRight.setIcon(iconRight);
            if (customBiRight.getVisibility() != View.VISIBLE)
                customBiRight.setVisibility(View.VISIBLE);
        }
        if (customBiRight != null && iconRight == null) {
            customBiRight.setVisibility(View.INVISIBLE);
        }


    }

    public void setIconRightSize(float value) {
        if (value > 0) {
            customBiRight.setIconSize(value);
        }
    }

    public void setTextRight(String title) {
        if (customTvRight != null && title != null) {
            customTvRight.setText(title);
            if (customTvRight.getVisibility() != View.VISIBLE)
                customTvRight.setVisibility(View.VISIBLE);
        }
    }

    public void setTextRightColor(ColorStateList color) {
        if (customTvRight != null && color != null)
            customTvRight.setTextColor(color);
    }

    public void setTextRightSize(float textRightSize) {
        if (customTvRight != null && textRightSize > 0)
            customTvRight.setTextSize(textRightSize / getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.customBiLeft) {
            switch (type) {
                case TYPE_SINGLE_BACK:
                    //                        ((Activity) context).onBackPressed();
                    Activity activity = scanForActivity(context);
                    if (activity != null)
                        activity.onBackPressed();
                    break;
                case TYPE_SINGLE_BUTTON:
                    if (onClickSingleButtonListener != null)
                        onClickSingleButtonListener.onClickLeftButton();
                    break;
                case TYPE_DOUBLE_BUTTON:
                case TYPE_DOUBLE_TEXT:
                    if (onClickSingleButtonListener != null)
                        onClickSingleButtonListener.onClickLeftButton();
                    if (onClickDoubleButtonListener != null)
                        onClickDoubleButtonListener.onClickLeftButton();
                    break;
                default:
                    break;
            }
        } else if (id == R.id.customBiRight) {
            if (onClickDoubleButtonListener != null)
                onClickDoubleButtonListener.onClickRightButton();
        } else if (id == R.id.customTvRight) {
            if (onClickDoubleButtonListener != null)
                onClickDoubleButtonListener.onClickRightButton();
        }
    }

    public void setProgressbarColor(ColorStateList progressbarColor) {
        if (customProgressBar == null)
            return;

        customProgressBar.getIndeterminateDrawable().setColorFilter(progressbarColor != null ? progressbarColor.getDefaultColor() : ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void hideProgressbar() {
        switch (type) {
            case TYPE_SINGLE_BUTTON:
            case TYPE_SINGLE_BACK:
                if (customProgressBar != null && customProgressBar.getVisibility() != View.INVISIBLE)
                    customProgressBar.setVisibility(View.INVISIBLE);
                break;
            case TYPE_DOUBLE_BUTTON:
                if (customProgressBar != null && customProgressBar.getVisibility() != View.GONE)
                    customProgressBar.setVisibility(View.GONE);
                if (customBiRight != null && customBiRight.getVisibility() != View.VISIBLE)
                    customBiRight.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void showProgressbar() {
        switch (type) {
            case TYPE_SINGLE_BUTTON:
            case TYPE_SINGLE_BACK:
                if (customProgressBar != null && customProgressBar.getVisibility() != View.VISIBLE)
                    customProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_DOUBLE_BUTTON:
                if (customProgressBar != null && customProgressBar.getVisibility() != View.VISIBLE)
                    customProgressBar.setVisibility(View.VISIBLE);
                if (customBiRight != null && customBiRight.getVisibility() != View.GONE)
                    customBiRight.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public String getText() {
        return customTvTitle.getText().toString();
    }

    public void setTextColor(ColorStateList textColor) {
        if (customTvTitle != null && textColor != null)
            customTvTitle.setTextColor(textColor);
    }

    public void setTextSize(float textSize) {
        if (customTvTitle != null && textSize > 0)
            customTvTitle.setTextSize(textSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setText(String text) {
        if (customTvTitle != null && text != null) {
            customTvTitle.setText(text);
            if (customTvTitle.getVisibility() != View.VISIBLE)
                customTvTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setIconLeft(Drawable iconLeft) {
        if (customBiLeft != null && iconLeft != null) {
            customBiLeft.setIcon(iconLeft);
            if (customBiLeft.getVisibility() != View.VISIBLE)
                customBiLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setIconLeftSize(float value) {
        if (value > 0) {
            customBiLeft.setIconSize(value);
        }
    }

    public void setIconLeftColor(ColorStateList iconColor) {
        if (customBiLeft != null && iconColor != null)
            customBiLeft.setIconColor(iconColor);

    }

    public void setIconRightColor(ColorStateList iconColor) {
        if (customBiRight != null && iconColor != null)
            customBiRight.setIconColor(iconColor);
    }

    public boolean isShowingProgressBar() {
        return customProgressBar != null && customProgressBar.getVisibility() == View.VISIBLE;
    }

    /*--------------------------------------[PUBLIC METHOD]---------------------------------------*/
    public ButtonImageView getIconRight() {
        return customBiRight;
    }

    public ButtonImageView getIconLeft() {
        return customBiLeft;
    }

}
