package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;

import com.utils.ViewUtil;

public class ButtonIconView extends BaseCustomView {
    private ImageView customImgLeft;
    private ImageView customImgRight;
    private TextView customTvTitle;
    private Space customPaddingLeft;
    private Space customPaddingRight;
    private Space customSpaceLeft;
    private Space customSpaceRight;
    private Context mContext;
    private boolean mTextAllCaps = false;

    /*--------------------------------------[METHOD]----------------------------------------------*/

    public ButtonIconView(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_button_icon_view, this);

            customImgLeft = findViewById(R.id.customImgLeft);
            customImgRight = findViewById(R.id.customImgRight);
            customTvTitle = findViewById(R.id.customTvTitle);

            customSpaceLeft = findViewById(R.id.customSpaceLeft);
            customSpaceRight = findViewById(R.id.customSpaceRight);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonIconView, 0, 0);
                try {
                    String text = ta.getString(R.styleable.ButtonIconView_biv_text);
                    ColorStateList textColor = ta.getColorStateList(R.styleable.ButtonIconView_biv_textColor);
                    float textSize = ta.getDimension(R.styleable.ButtonIconView_biv_textSize, -1);
                    int textGravity = ta.getInteger(R.styleable.ButtonIconView_biv_textGravity, -1);
                    int textStyle = ta.getInteger(R.styleable.ButtonIconView_biv_textStyle, -1);
                    mTextAllCaps = ta.getBoolean(R.styleable.ButtonIconView_biv_textAllCaps, false);

                    setText(text);
                    setTextColor(textColor);
                    setTextSize(textSize);
                    setTextGravity(textGravity);
                    setTextStyle(textStyle);

                    Drawable iconLeft = ta.getDrawable(R.styleable.ButtonIconView_biv_iconLeft);
                    float iconLeftSize = ta.getDimension(R.styleable.ButtonIconView_biv_iconLeftSize, -1);
                    setIconLeft(iconLeft);
                    setIconLeftSize(iconLeftSize);

                    Drawable iconRight = ta.getDrawable(R.styleable.ButtonIconView_biv_iconRight);
                    float iconRightSize = ta.getDimension(R.styleable.ButtonIconView_biv_iconRightSize, -1);
                    setIconRight(iconRight);
                    setIconRightSize(iconRightSize);

                    float spaceLeft = ta.getDimension(R.styleable.ButtonIconView_biv_spaceLeft, -1);
                    float spaceRight = ta.getDimension(R.styleable.ButtonIconView_biv_spaceRight, -1);
                    setSpaceLeft(spaceLeft);
                    setSpaceRight(spaceRight);

                    float paddingLeft = ta.getDimension(R.styleable.ButtonIconView_biv_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.ButtonIconView_biv_paddingRight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);

                    ColorStateList iconRightColor = ta.getColorStateList(R.styleable.ButtonIconView_biv_iconRightColor);
                    ColorStateList iconLeftColor = ta.getColorStateList(R.styleable.ButtonIconView_biv_iconLeftColor);
                    setIconLeftColor(iconLeftColor);
                    setIconRightColor(iconRightColor);
                } finally {
                    ta.recycle();
                }
            }
        }
    }


    /*--------------------------------------[GETTER]----------------------------------------------*/
    public String getText() {
        return (customTvTitle != null) ? customTvTitle.getText().toString() : "";
    }

    public TextView getTextView() {
        return customTvTitle;
    }

    /*--------------------------------------[SETTER]----------------------------------------------*/
    public void setIconLeftColor(ColorStateList iconColor) {
        if (customImgLeft != null && iconColor != null) {
            //ViewCompat.setBackgroundTintList(customImgLeft, iconColor);
            ImageViewCompat.setImageTintList(customImgLeft, iconColor);
        }
    }

    public void setIconRightColor(ColorStateList iconColor) {
        if (customImgRight != null && iconColor != null) {
            //ViewCompat.setBackgroundTintList(customImgRight, iconColor);
            ImageViewCompat.setImageTintList(customImgRight, iconColor);
        }
    }

    public void setTextSize(float textLeftSize) {
        if (customTvTitle != null && textLeftSize > 0)
            customTvTitle.setTextSize(textLeftSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTextStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvTitle, textStyle);
    }

    public void setTextGravity(int textLeftGravity) {
        if (customTvTitle != null && textLeftGravity >= 0) {
            if (textLeftGravity == 0)
                customTvTitle.setGravity(Gravity.START);
            else if (textLeftGravity == 1)
                customTvTitle.setGravity(Gravity.END);
            else if (textLeftGravity == 2)
                customTvTitle.setGravity(Gravity.CENTER);
        }
    }

    public void setText(String title) {
        if (customTvTitle != null) {
            if (!TextUtils.isEmpty(title)) {
                customTvTitle.setText(mTextAllCaps ? title.toUpperCase() : title);

                if (customTvTitle.getVisibility() != View.VISIBLE)
                    customTvTitle.setVisibility(View.VISIBLE);
            } else
                customTvTitle.setVisibility(View.GONE);
        }
    }

    public void setTextColor(ColorStateList color) {
        if (customTvTitle != null && color != null)
            customTvTitle.setTextColor(color);
    }

    public void setIconLeftSize(float value) {
        if (value > 0) {
            LayoutParams layoutParams = new LayoutParams((int) value, (int) value);
            customImgLeft.setLayoutParams(layoutParams);
        }
    }

    public void setIconLeft(Drawable res) {
        if (customImgLeft != null && res != null) {
            ViewUtil.setImageDrawable(customImgLeft, res);
            if (customImgLeft.getVisibility() != View.VISIBLE)
                customImgLeft.setVisibility(View.VISIBLE);
            if (customSpaceLeft.getVisibility() != View.VISIBLE)
                customSpaceLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setIconLeft(String path) {
        if (customImgLeft != null) {
//            if (path != null && !path.equals(""))
//                Glide.with(customImgLeft.getContext()).load(path).into(customImgLeft);

            if (customImgLeft.getVisibility() != View.VISIBLE)
                customImgLeft.setVisibility(View.VISIBLE);
            if (customSpaceLeft.getVisibility() != View.VISIBLE)
                customSpaceLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setIconRight(Drawable res) {
        if (customImgRight != null && res != null) {
            ViewUtil.setImageDrawable(customImgRight, res);
            if (customImgRight.getVisibility() != View.VISIBLE)
                customImgRight.setVisibility(View.VISIBLE);
            if (customSpaceRight.getVisibility() != View.VISIBLE)
                customSpaceRight.setVisibility(View.VISIBLE);
        }
    }

    public void setIconRightSize(float value) {
        if (value > 0) {
            LayoutParams layoutParams = new LayoutParams((int) value, (int) value);
            customImgRight.setLayoutParams(layoutParams);
        }
    }

    public void setSpaceLeft(float spaceLeft) {
        if (customSpaceLeft != null && spaceLeft >= 0) {
            LayoutParams params = new LayoutParams(
                    (int) spaceLeft,
                    LayoutParams.WRAP_CONTENT
            );
            customSpaceLeft.setLayoutParams(params);
        }
    }

    public void setSpaceRight(float spaceRight) {
        if (customSpaceRight != null && spaceRight >= 0) {
            LayoutParams params = new LayoutParams(
                    (int) spaceRight,
                    LayoutParams.WRAP_CONTENT
            );
            customSpaceRight.setLayoutParams(params);
        }
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

    public ImageView getImageLeft() {
        return customImgLeft;
    }

    public ImageView getImageRight() {
        return customImgRight;
    }
}
