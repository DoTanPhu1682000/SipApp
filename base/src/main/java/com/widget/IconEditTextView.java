package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.core.widget.ImageViewCompat;

import com.utils.ViewUtil;


public class IconEditTextView extends LinearLayout {
    private EditText customEtTitle;
    private ImageView customImgLeft;
    private ImageView customImgRight;
    private Space customSpaceLeft;
    private Space customSpaceRight;
    private Space customPaddingLeft;
    private Space customPaddingRight;
    /*--------------------------------------[METHOD]----------------------------------------------*/

    public IconEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_icon_edittext_view, this);
            customImgLeft = findViewById(R.id.customImgLeft);
            customImgRight = findViewById(R.id.customImgRight);
            customEtTitle = findViewById(R.id.customEtTitle);
            customSpaceLeft = findViewById(R.id.customSpaceLeft);
            customSpaceRight = findViewById(R.id.customSpaceRight);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);
            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconEditTextView, 0, 0);
                try {
                    String text = ta.getString(R.styleable.IconEditTextView_iev_text);
                    int inputType = ta.getInteger(R.styleable.IconEditTextView_iev_inputType, -1);
                    float textSize = ta.getDimension(R.styleable.IconEditTextView_iev_textSize, -1);
                    ColorStateList textColor = ta.getColorStateList(R.styleable.IconEditTextView_iev_textColor);
                    setText(text);
                    setTextColor(textColor);
                    setInputType(inputType);
                    setTextSize(textSize);

                    String hint = ta.getString(R.styleable.IconEditTextView_iev_hint);
                    ColorStateList textColorHint = ta.getColorStateList(R.styleable.IconEditTextView_iev_textColorHint);
                    setHint(hint);
                    setHintTextColor(textColorHint);

                    Drawable iconLeft = ta.getDrawable(R.styleable.IconEditTextView_iev_iconLeft);
                    float iconLeftSize = ta.getDimension(R.styleable.IconEditTextView_iev_iconLeftSize, -1);
                    setIconLeft(iconLeft);
                    setIconLeftSize(iconLeftSize);

                    Drawable iconRight = ta.getDrawable(R.styleable.IconEditTextView_iev_iconRight);
                    float iconRightSize = ta.getDimension(R.styleable.IconEditTextView_iev_iconRightSize, -1);
                    setIconRight(iconRight);
                    setIconRightSize(iconRightSize);

                    float spaceLeft = ta.getDimension(R.styleable.IconEditTextView_iev_spaceLeft, -1);
                    float spaceRight = ta.getDimension(R.styleable.IconEditTextView_iev_spaceRight, -1);
                    setSpaceLeft(spaceLeft);
                    setSpaceRight(spaceRight);

                    float paddingLeft = ta.getDimension(R.styleable.IconEditTextView_iev_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.IconEditTextView_iev_paddingRight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);

                    ColorStateList iconLeftColor = ta.getColorStateList(R.styleable.IconEditTextView_iev_iconLeftColor);
                    ColorStateList iconRightColor = ta.getColorStateList(R.styleable.IconEditTextView_iev_iconRightColor);
                    setIconLeftColor(iconLeftColor);
                    setIconRightColor(iconRightColor);
                } finally {
                    ta.recycle();
                }
            }
        }
    }


    /*-------------------------------------[SAVE STATE]-------------------------------------------*/
    @SuppressWarnings("unchecked")
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    public static class SavedState extends BaseSavedState {
        SparseArray childrenStates;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader classLoader) {
            super(in);
            childrenStates = in.readSparseArray(classLoader);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeSparseArray(childrenStates);
        }

        public static final ClassLoaderCreator<SavedState> CREATOR
                = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @SuppressWarnings("InfiniteRecursion")
            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    /*--------------------------------------[PRIVATE]---------------------------------------------*/

    public EditText getEditText() {
        return customEtTitle;
    }

    public Editable getText() {
        return customEtTitle.getText();
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

    public void setText(String title) {
        if (customEtTitle != null && title != null)
            customEtTitle.setText(title);
    }

    public void setTextSize(float textSize) {
        if (customEtTitle != null && textSize > 0)
            customEtTitle.setTextSize(textSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTextColor(ColorStateList color) {
        if (customEtTitle != null && color != null)
            customEtTitle.setTextColor(color);
    }

    public void setInputType(int inputType) {
        switch (inputType) {
            case 1:
                customEtTitle.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 2:
                customEtTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case 3:
                customEtTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 4:
                customEtTitle.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 5:
                customEtTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
            case 6:
                customEtTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            default:
                customEtTitle.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    public void setHintTextColor(ColorStateList textColorHint) {
        if (customEtTitle != null && textColorHint != null)
            customEtTitle.setHintTextColor(textColorHint);
    }

    public void setHint(String hint) {
        if (customEtTitle != null && hint != null)
            customEtTitle.setHint(hint);
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

    public void setIconLeftSize(float value) {
        if (value > 0) {
            LayoutParams layoutParams = new LayoutParams((int) value, (int) value);
            customImgLeft.setLayoutParams(layoutParams);
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

    public ImageView getImageRight() {
        return customImgRight;
    }

    public ImageView getImageLeft() {
        return customImgLeft;
    }
}
