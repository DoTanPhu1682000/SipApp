package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;

import com.utils.ViewUtil;

public class IconTextView extends BaseCustomView {
    private LinearLayout customLayoutParent;
    private Space customSpace;
    private Space customSpaceLeft;
    private Space customSpaceRight;
    private Space customPaddingLeft;
    private Space customPaddingRight;
    private ImageView customImgLeft;
    private ImageView customImgRight;
    private TextView customTvLeft;
    private TextView customTvRight;
    private Context mContext;


    /*--------------------------------------[METHOD]----------------------------------------------*/
    public IconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_icon_text_view, this);

            customSpace = findViewById(R.id.customSpace);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);
            customImgLeft = findViewById(R.id.customImgLeft);
            customImgRight = findViewById(R.id.customImgRight);
            customTvLeft = findViewById(R.id.customTvLeft);
            customTvRight = findViewById(R.id.customTvRight);
            customSpaceLeft = findViewById(R.id.customSpaceLeft);
            customSpaceRight = findViewById(R.id.customSpaceRight);
            customLayoutParent = findViewById(R.id.customLayoutParent);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconTextView, 0, 0);
                try {
                    String textLeft = ta.getString(R.styleable.IconTextView_itv_textLeft);
                    ColorStateList textLeftColor = ta.getColorStateList(R.styleable.IconTextView_itv_textLeftColor);
                    int textLeftLines = ta.getInteger(R.styleable.IconTextView_itv_textLeftLines, -1);
                    int textLeftStyle = ta.getInteger(R.styleable.IconTextView_itv_textLeftStyle, -1);
                    float textLeftSize = ta.getDimension(R.styleable.IconTextView_itv_textLeftSize, -1);
                    int textLeftGravity = ta.getInteger(R.styleable.IconTextView_itv_textLeftGravity, -1);
                    setTextLeft(textLeft);
                    setTextLeftColor(textLeftColor);
                    setTextLeftLines(textLeftLines);
                    setTextLeftStyle(textLeftStyle);
                    setTextLeftSize(textLeftSize);
                    setTextLeftGravity(textLeftGravity);

                    float textLeftWidth = ta.getDimension(R.styleable.IconTextView_itv_textLeftWidth, -1);
                    float textLeftMaxWidth = ta.getDimension(R.styleable.IconTextView_itv_textLeftMaxWidth, -1);
                    setTextLeftWidth(textLeftWidth);
                    setTextLeftMaxWidth(textLeftMaxWidth);

                    String textRight = ta.getString(R.styleable.IconTextView_itv_textRight);
                    ColorStateList textRightColor = ta.getColorStateList(R.styleable.IconTextView_itv_textRightColor);
                    int textRightLines = ta.getInteger(R.styleable.IconTextView_itv_textRightLines, -1);
                    int textRightStyle = ta.getInteger(R.styleable.IconTextView_itv_textRightStyle, -1);
                    float textRightSize = ta.getDimension(R.styleable.IconTextView_itv_textRightSize, -1);
                    int textRightGravity = ta.getInteger(R.styleable.IconTextView_itv_textRightGravity, -1);
                    setTextRight(textRight);
                    setTextRightColor(textRightColor);
                    setTextRightLines(textRightLines);
                    setTextRightStyle(textRightStyle);
                    setTextRightSize(textRightSize);
                    setTextRightGravity(textRightGravity);

                    Drawable iconLeft = ta.getDrawable(R.styleable.IconTextView_itv_iconLeft);
                    float iconLeftSize = ta.getDimension(R.styleable.IconTextView_itv_iconLeftSize, -1);
                    setIconLeft(iconLeft);
                    setIconLeftSize(iconLeftSize);

                    Drawable iconRight = ta.getDrawable(R.styleable.IconTextView_itv_iconRight);
                    float iconRightSize = ta.getDimension(R.styleable.IconTextView_itv_iconRightSize, -1);
                    setIconRight(iconRight);
                    setIconRightSize(iconRightSize);

                    float space = ta.getDimension(R.styleable.IconTextView_itv_space, -1);
                    setSpace(space);

                    float spaceLeft = ta.getDimension(R.styleable.IconTextView_itv_spaceLeft, -1);
                    float spaceRight = ta.getDimension(R.styleable.IconTextView_itv_spaceRight, -1);
                    setSpaceLeft(spaceLeft);
                    setSpaceRight(spaceRight);

                    float paddingLeft = ta.getDimension(R.styleable.IconTextView_itv_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.IconTextView_itv_paddingRight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);

                    ColorStateList iconLeftColor = ta.getColorStateList(R.styleable.IconTextView_itv_iconLeftColor);
                    ColorStateList iconRightColor = ta.getColorStateList(R.styleable.IconTextView_itv_iconRightColor);
                    setIconLeftColor(iconLeftColor);
                    setIconRightColor(iconRightColor);


                    int gravity = ta.getInteger(R.styleable.IconTextView_itv_gravity, -1);
                    setLayoutGravity(gravity);
                } finally {
                    ta.recycle();
                }
            }
        }
    }

    public void setLayoutGravity(int gravity) {
        if (gravity == 1)
            customLayoutParent.setGravity(Gravity.CENTER_VERTICAL);
        else if (gravity == 0)
            customLayoutParent.setGravity(Gravity.TOP);
    }


    /*--------------------------------------[GETTER]----------------------------------------------*/
    public String getTextRight() {
        try {
            return customTvRight.getText().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public String getTextLeft() {
        try {
            return customTvLeft.getText().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public TextView getTextViewRight() {
        return customTvRight;
    }

    /*--------------------------------------[SETTER]----------------------------------------------*/
    public void setIconLeftColor(ColorStateList iconLeftColor) {
        if (customImgLeft != null && iconLeftColor != null) {
            //ViewCompat.setBackgroundTintList(customImgLeft, iconLeftColor);
            ImageViewCompat.setImageTintList(customImgLeft, iconLeftColor);
        }
    }

    public void setIconRightColor(ColorStateList iconRightColor) {
        if (customImgRight != null && iconRightColor != null) {
            // ViewCompat.setBackgroundTintList(customImgRight, iconRightColor);
            ImageViewCompat.setImageTintList(customImgRight, iconRightColor);
        }
    }

    public void setTextLeftStyle(int textLeftStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvLeft, textLeftStyle);
    }

    public void setTextLeftGravity(int textLeftGravity) {
        if (customTvLeft != null && textLeftGravity >= 0) {
            if (textLeftGravity == 0)
                customTvLeft.setGravity(Gravity.START);
            else if (textLeftGravity == 1)
                customTvLeft.setGravity(Gravity.END);
            else if (textLeftGravity == 2)
                customTvLeft.setGravity(Gravity.CENTER);
        }
    }

    public void setTextLeftMaxWidth(float textLeftMaxWidth) {
        if (customTvLeft != null && textLeftMaxWidth > 0) {
            customTvLeft.setMaxWidth((int) textLeftMaxWidth);
            LayoutParams param = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    0
            );
            customTvLeft.setLayoutParams(param);
        }
    }

    public void setTextLeftWidth(float textLeftWidth) {
        if (customTvLeft != null && textLeftWidth > 0) {
            customTvLeft.setWidth((int) textLeftWidth);
            LayoutParams param = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    0
            );
            customTvLeft.setLayoutParams(param);
        }
    }

    public void setTextLeftLines(int line) {
        if (customTvLeft != null) {
            if (line == 1) {
                customTvLeft.setLines(1);
                customTvLeft.setSingleLine(true);
            } else if (line > 1) {
                customTvLeft.setSingleLine(false);
                customTvLeft.setLines(line);
            }
        }
    }

    public void setTextLeft(Object object) {
        if (customTvLeft != null && object != null) {
            customTvLeft.setText(String.valueOf(object));
            if (customTvLeft.getVisibility() != View.VISIBLE)
                customTvLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setTextLeft(String title) {
        if (customTvLeft != null && title != null) {
            customTvLeft.setText(title);
            if (customTvLeft.getVisibility() != View.VISIBLE)
                customTvLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setTextLeftColor(ColorStateList color) {
        if (customTvLeft != null && color != null)
            customTvLeft.setTextColor(color);
    }

    public void setTextLeftSize(float textLeftSize) {
        if (customTvLeft != null && textLeftSize > 0)
            customTvLeft.setTextSize(textLeftSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTextRightStyle(int textRightStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvRight, textRightStyle);
    }

    public void setTextRightGravity(int textRightGravity) {
        if (customTvRight != null && textRightGravity >= 0) {
            if (textRightGravity == 0)
                customTvRight.setGravity(Gravity.START);
            else if (textRightGravity == 1)
                customTvRight.setGravity(Gravity.END);
            else if (textRightGravity == 2)
                customTvRight.setGravity(Gravity.CENTER);
        }
    }

    public void setTextRightLines(int line) {
        if (customTvRight != null) {
            if (line == 1) {
                customTvRight.setLines(1);
                customTvRight.setSingleLine(true);
            } else if (line > 1) {
                customTvRight.setSingleLine(false);
                customTvRight.setLines(line);
            }
        }
    }

    public void setTextRight(String title) {
        if (customTvRight != null && title != null) {
            customTvRight.setText(title);
            if (customTvRight.getVisibility() != View.VISIBLE)
                customTvRight.setVisibility(View.VISIBLE);
        }
    }

    public void setTextRight(Object object) {
        if (customTvRight != null && object != null) {
            customTvRight.setText(String.valueOf(object));
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

    public void setSpace(float size) {
        if (customSpace != null && size >= 0) {
            customSpace.setVisibility(View.VISIBLE);
            LayoutParams params = new LayoutParams(
                    (int) size,
                    LayoutParams.WRAP_CONTENT
            );
            customSpace.setLayoutParams(params);
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

    public void setIconLeftSize(float value) {
        if (value > 0) {
            LayoutParams layoutParams = new LayoutParams((int) value, (int) value);
            customImgLeft.setLayoutParams(layoutParams);
        }
    }

    public void setIconRight(String path) {
        if (customImgRight != null) {
//            if (path != null && !path.equals(""))
//                Glide.with(customImgRight.getContext()).load(path).into(customImgRight);

            if (customImgRight.getVisibility() != View.VISIBLE)
                customImgRight.setVisibility(View.VISIBLE);
            if (customSpaceRight.getVisibility() != View.VISIBLE)
                customSpaceRight.setVisibility(View.VISIBLE);
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

}
