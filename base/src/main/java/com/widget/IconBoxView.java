package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;

import com.utils.ViewUtil;


public class IconBoxView extends BaseCustomView {

    private ImageView customImgIcon;
    private TextView customTvTitle;
    private TextView customTvDescription;
    private Context mContext;

    public IconBoxView(Context context) {
        super(context);
        init(context, null);
    }

    public IconBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_icon_box_view, this);

            customImgIcon = findViewById(R.id.customImgIcon);
            customTvTitle = findViewById(R.id.customTvTitle);
            customTvDescription = findViewById(R.id.customTvDescription);
            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconBoxView, 0, 0);
                try {
                    String title = ta.getString(R.styleable.IconBoxView_ibv_title);
                    String description = ta.getString(R.styleable.IconBoxView_ibv_description);

                    ColorStateList titleColor = ta.getColorStateList(R.styleable.IconBoxView_ibv_titleColor);
                    ColorStateList descriptionColor = ta.getColorStateList(R.styleable.IconBoxView_ibv_descriptionColor);

                    Drawable icon = ta.getDrawable(R.styleable.IconBoxView_ibv_icon);
                    float iconSize = ta.getDimension(R.styleable.IconBoxView_ibv_iconSize, -1);
                    float titleSize = ta.getDimension(R.styleable.IconBoxView_ibv_titleSize, -1);
                    float descriptionSize = ta.getDimension(R.styleable.IconBoxView_ibv_descriptionSize, -1);
                    int titleStyle = ta.getInteger(R.styleable.IconBoxView_ibv_titleStyle, -1);
                    int descriptionStyle = ta.getInteger(R.styleable.IconBoxView_ibv_descriptionStyle, -1);
                    ColorStateList iconColor = ta.getColorStateList(R.styleable.IconBoxView_ibv_iconColor);
                    setIconColor(iconColor);
                    setIconSize(iconSize);

                    setTitle(title);
                    setTitleSize(titleSize);
                    setTitleStyle(titleStyle);
                    setDescriptionStyle(descriptionStyle);

                    setDescription(description);

                    setTitleColor(titleColor);
                    setDescriptionColor(descriptionColor);
                    setDescriptionSize(descriptionSize);

                    setIcon(icon);

                } finally {
                    ta.recycle();
                }
            }
        }
    }


    /*--------------------------------------[PUBLIC METHOD]--------------------------------------*/
    public void setDescriptionStyle(int descriptionStyle) {
        setFont(mContext, customTvTitle, descriptionStyle);
    }

    public void setDescriptionSize(float value) {
        if (customTvTitle != null && value > 0)
            customTvTitle.setTextSize(value / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setIconColor(ColorStateList iconColor) {
        if (customImgIcon != null && iconColor != null) {
            // ViewCompat.setBackgroundTintList(customImgRight, iconRightColor);
            ImageViewCompat.setImageTintList(customImgIcon, iconColor);
        }
    }

    public void setTitleStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvTitle, textStyle);
    }

    public void setTitleSize(float value) {
        if (customTvTitle != null && value > 0)
            customTvTitle.setTextSize(value / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setDescription(String description) {
        if (customTvDescription != null && description != null) {
            customTvDescription.setText(description);
            if (customTvDescription.getVisibility() != View.VISIBLE)
                customTvDescription.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title) {
        if (customTvTitle != null && title != null) {
            customTvTitle.setText(title);
            if (customTvTitle.getVisibility() != View.VISIBLE)
                customTvTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleColor(ColorStateList color) {
        if (customTvTitle != null && color != null)
            customTvTitle.setTextColor(color);
    }

    public void setDescriptionColor(ColorStateList color) {
        if (customTvDescription != null && color != null)
            customTvDescription.setTextColor(color);

    }

    public void setIcon(Drawable res) {
        if (customImgIcon != null && res != null) {
            //customImgIcon.setImageDrawable(res);
            ViewUtil.setImageDrawable(customImgIcon, res);
            if (customImgIcon.getVisibility() != View.VISIBLE)
                customImgIcon.setVisibility(View.VISIBLE);
        }
    }

    public void setIconSize(float iconSize) {
        if (iconSize > 0) {
            LayoutParams layoutParams = new LayoutParams((int) iconSize, (int) iconSize);
            customImgIcon.setLayoutParams(layoutParams);
        }
    }

    public ImageView getImgIcon() {
        return customImgIcon;
    }
}
