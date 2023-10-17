package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;

import com.utils.ViewUtil;


public class ButtonImageView extends LinearLayout {
    private ImageView customImgIcon;

    public ButtonImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonImageView, 0, 0);
            try {
                inflater.inflate(R.layout.custom_button_imageview, this);
                customImgIcon = findViewById(R.id.customImgIcon);

                Drawable icon = ta.getDrawable(R.styleable.ButtonImageView_bi_icon);
                float iconSize = ta.getDimension(R.styleable.ButtonImageView_bi_iconSize, -1);
                ColorStateList iconColor = ta.getColorStateList(R.styleable.ButtonImageView_bi_iconColor);

                setIcon(icon);
                setIconSize(iconSize);
                setIconColor(iconColor);
            } finally {
                ta.recycle();
            }
        }


    }

    public void setIconSize(float iconSize) {
        if (customImgIcon != null && iconSize > 0) {
            LayoutParams layoutParams = new LayoutParams((int) iconSize, (int) iconSize);
            customImgIcon.setLayoutParams(layoutParams);
        }
    }

    public void setIcon(Drawable icon) {
        if (customImgIcon != null && icon != null)
            ViewUtil.setImageDrawable(customImgIcon, icon);
    }

    public void setIcon(int icon) {
        if (customImgIcon != null && icon != 0)
            customImgIcon.setImageResource(icon);
    }

    public void setIconColor(ColorStateList iconColor) {
        if (customImgIcon != null && iconColor != null) {
            //ViewCompat.setBackgroundTintList(customImgIcon, iconColor);
            ImageViewCompat.setImageTintList(customImgIcon, iconColor);
        }
    }

    public Drawable getIcon() {
        return customImgIcon.getDrawable();
    }

    public ColorStateList getIconColor() {
        return ImageViewCompat.getImageTintList(customImgIcon);
    }

    public ImageView getImageView() {
        return customImgIcon;
    }
}
