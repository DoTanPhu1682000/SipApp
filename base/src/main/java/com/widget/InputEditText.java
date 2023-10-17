package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.utils.ViewUtil;

/**
 * Issue 1
 * How to prevent editText from auto filling text when fragment is restored from saved instance?
 * https://stackoverflow.com/questions/43208708/how-to-prevent-edittext-from-auto-filling-text-when-fragment-is-restored-from-sa
 */
public class InputEditText extends BaseCustomView implements View.OnClickListener {

    private LinearLayout customLayoutParentTitle;
    private LinearLayout customLayoutTitle;
    private TextView customTvTitle;
    private TextView customTvDescription;
    private ImageView customImgRequired;
    private ImageView customImgMultilineRequired;
    private ButtonImageView customBiHelp;
    private String mHelp = null;
    private Space customPaddingLeft;
    private Space customPaddingRight;

    private ImageView customImgClear;
    private LinearLayout customLayoutParentContent;
    private EditText customEtTitle;
    private Context mContext;
    private boolean isEnableClearIcon = false;

    public InputEditText(Context context) {
        super(context);
        init(context, null);
    }

    public InputEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_input_edittext, this);

            customLayoutTitle = findViewById(R.id.customLayoutTitle);
            customLayoutParentContent = findViewById(R.id.customLayoutParentContent);
            customTvTitle = findViewById(R.id.customTvTitle);
            customTvDescription = findViewById(R.id.customTvDescription);
            customImgRequired = findViewById(R.id.customImgRequired);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);
            customImgMultilineRequired = findViewById(R.id.customImgMultilineRequired);
            customBiHelp = findViewById(R.id.customBiHelp);
            customLayoutParentTitle = findViewById(R.id.customLayoutParentTitle);
            customImgClear = findViewById(R.id.customImgClear);

            customEtTitle = findViewById(R.id.customEtTitle);
            //Issue 1
            customEtTitle.setSaveEnabled(false);
            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputEditText, 0, 0);
                try {
                    String title = ta.getString(R.styleable.InputEditText_iet_title);
                    ColorStateList titleColor = ta.getColorStateList(R.styleable.InputEditText_iet_titleColor);
                    int titleStyle = ta.getInteger(R.styleable.InputEditText_iet_titleStyle, -1);
                    int textStyle = ta.getInteger(R.styleable.InputEditText_iet_textStyle, -1);
                    float titleSize = ta.getDimension(R.styleable.InputEditText_iet_titleSize, -1);
                    setTitle(title);
                    setTitleStyle(titleStyle);
                    setTextStyle(textStyle);
                    setTitleColor(titleColor);
                    setTitleSize(titleSize);

                    String text = ta.getString(R.styleable.InputEditText_iet_text);

                    int inputType = ta.getInteger(R.styleable.InputEditText_iet_inputType, -1);
                    float textSize = ta.getDimension(R.styleable.InputEditText_iet_textSize, -1);
                    ColorStateList textColor = ta.getColorStateList(R.styleable.InputEditText_iet_textColor);
                    setInputType(inputType);

                    setTextColor(textColor);
                    setTextSize(textSize);

                    String description = ta.getString(R.styleable.InputEditText_iet_description);
                    ColorStateList descriptionColor = ta.getColorStateList(R.styleable.InputEditText_iet_descriptionColor);
                    setDescription(description);
                    setDescriptionColor(descriptionColor);

                    String hint = ta.getString(R.styleable.InputEditText_iet_hint);
                    ColorStateList textColorHint = ta.getColorStateList(R.styleable.InputEditText_iet_textColorHint);
                    setHint(hint);
                    setHintTextColor(textColorHint);

                    boolean isRequired = ta.getBoolean(R.styleable.InputEditText_iet_required, false);
                    String help = ta.getString(R.styleable.InputEditText_iet_help);
                    Drawable background = ta.getDrawable(R.styleable.InputEditText_iet_background);
                    ColorStateList backgroundTint = ta.getColorStateList(R.styleable.InputEditText_iet_backgroundTint);
                    boolean isEnable = ta.getBoolean(R.styleable.InputEditText_iet_enable, true);
                    isEnableClearIcon = ta.getBoolean(R.styleable.InputEditText_iet_enableClearIcon, false);

                    setRequired(isRequired);
                    setHelp(help);
                    setRequired(isRequired);
                    setEditTextEnabled(isEnable);
                    setEnableClearIcon();

                    setEditTextBackground(background);
                    setEditTextBackgroundTint(backgroundTint);

                    setText(text);


                    float paddingLeft = ta.getDimension(R.styleable.InputEditText_iet_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.InputEditText_iet_paddingRight, -1);
                    float inputHeight = ta.getDimension(R.styleable.InputEditText_iet_inputHeight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);
                    setInputHeight(inputHeight);


                    //Android original
                    int androidInputType = ta.getInt(R.styleable.InputEditText_android_inputType, EditorInfo.TYPE_NULL);
                    if (androidInputType != EditorInfo.TYPE_NULL) {
                        customEtTitle.setInputType(androidInputType);
                    }
                    int androidGravity = ta.getInt(R.styleable.InputEditText_android_gravity, EditorInfo.TYPE_NULL);
                    if (androidGravity != EditorInfo.TYPE_NULL) {
                        customEtTitle.setGravity(androidGravity);
                    }
                    int androidMinLines = ta.getInt(R.styleable.InputEditText_android_minLines, EditorInfo.TYPE_NULL);
                    if (androidMinLines != EditorInfo.TYPE_NULL) {
                        customEtTitle.setMinLines(androidMinLines);
                    }
                    int androidMaxLength = ta.getInt(R.styleable.InputEditText_android_maxLength, EditorInfo.TYPE_NULL);
                    if (androidMaxLength != EditorInfo.TYPE_NULL) {
                        customEtTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(androidMaxLength)});
                    }

                    int minLines = ta.getInteger(R.styleable.InputEditText_iet_minLines, -1);
                    setMinLines(minLines);

                    int maxLength = ta.getInteger(R.styleable.InputEditText_iet_maxLength, -1);
                    setMaxLength(maxLength);

                    // set ERROR editext
                    String error = ta.getString(R.styleable.InputEditText_iet_error);
                    setError(error);
                    if (customTvDescription.getVisibility() == View.VISIBLE || customLayoutTitle.getVisibility() == View.VISIBLE)
                        customLayoutParentTitle.setVisibility(View.VISIBLE);
                    else
                        customLayoutParentTitle.setVisibility(View.GONE);

                    customImgClear.setOnClickListener(this);


                } finally {
                    ta.recycle();
                }
            }
        }
    }

    /*-----------------------------[IMPLEMENT]----------------------------------------------------*/

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.customBiHelp) {
            if (customBiHelp != null && customBiHelp.getContext() != null && mHelp != null)
                Tooltip.info(customBiHelp.getContext(), mHelp, customBiHelp);
        } else if (id == R.id.customImgClear) {
            customEtTitle.setText("");
        }
    }

    /*-----------------------------[PRIVATE]------------------------------------------------------*/
    private void toggleClearIcon() {
        if (isEnableClearIcon && customEtTitle != null) {
            if (customEtTitle.getText().toString().length() > 0)
                customImgClear.setVisibility(View.VISIBLE);
            else
                customImgClear.setVisibility(View.GONE);
        }
    }

    /*--------------------------------------[METHOD]----------------------------------------------*/

    private void setEnableClearIcon() {
        if (isEnableClearIcon && customEtTitle != null)
            customEtTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //ignore
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //ignore
                }

                @Override
                public void afterTextChanged(Editable s) {
                    toggleClearIcon();
                }
            });
    }

    public void setMinLines(int minLines) {
        if (customEtTitle != null && minLines > 0)
            customEtTitle.setMinLines(minLines);
    }

    public void setEditTextEnabled(boolean isEnable) {
        if (customEtTitle != null)
            customEtTitle.setEnabled(isEnable);
    }

    public void setEditTextBackground(Drawable background) {
        ViewUtil.setBackground(customLayoutParentContent, background);
    }

    public void setEditTextBackgroundTint(ColorStateList backgroundTint) {
        if (backgroundTint == null)
            customEtTitle.setBackground(null);
        else
            ViewCompat.setBackgroundTintList(customEtTitle, backgroundTint);
    }

    public void setHintTextColor(ColorStateList textColorHint) {
        if (customEtTitle != null && textColorHint != null)
            customEtTitle.setHintTextColor(textColorHint);
    }

    public void setTextSize(float textSize) {
        if (customEtTitle != null && textSize > 0)
            customEtTitle.setTextSize(textSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTextColor(ColorStateList color) {
        if (customEtTitle != null && color != null)
            customEtTitle.setTextColor(color);
    }

    public void setMaxLength(int maxLength) {
        if (customEtTitle != null && maxLength > 0) {
            customEtTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    public void setText(String title) {
        if (customEtTitle != null && title != null) {
            customEtTitle.setText(title);
            toggleClearIcon();
        }
    }

    public void setText(Object object) {
        if (customEtTitle != null && object != null) {
            customEtTitle.setText(String.valueOf(object));
            toggleClearIcon();
        }
    }

    public void setInputType(int inputType) {
        if (!isInEditMode())
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
                case 7:
                    customEtTitle.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                default:
                    customEtTitle.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
    }

    public void setHint(String hint) {
        if (customEtTitle != null && hint != null)
            customEtTitle.setHint(hint);
    }

    public EditText getEditText() {
        return customEtTitle;
    }

    public String getText() {
        return customEtTitle.getText().toString();
    }

    /*----------------------------[TITLE]---------------------------------------------------------*/
    public String getTitle() {
        return customTvTitle != null ? customTvTitle.getText().toString() : "";
    }

    public void setHelp(String help) {
        if (customBiHelp != null && help != null) {
            customBiHelp.setVisibility(View.VISIBLE);
            this.mHelp = help;
            customBiHelp.setOnClickListener(this);
        }
    }

    public void setTextStyle(int textStyle) {
        setFont(mContext, customEtTitle, textStyle);
    }

    public void setTitleStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvTitle, textStyle);
    }

    public void setTitleSize(float value) {
        if (customTvTitle != null && value > 0)
            customTvTitle.setTextSize(value / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTitleColor(ColorStateList color) {
        if (customTvTitle != null && color != null)
            customTvTitle.setTextColor(color);
    }


    public void setTitle(String title) {
        if (customTvTitle != null && title != null) {
            customTvTitle.setText(title);
            if (customLayoutTitle.getVisibility() != View.VISIBLE)
                customLayoutTitle.setVisibility(View.VISIBLE);

        }
    }

    public void setDescription(String description) {
        if (customTvDescription != null && description != null) {
            customTvDescription.setVisibility(View.VISIBLE);
            customTvDescription.setText(description);
            if (customLayoutTitle.getVisibility() != View.VISIBLE)
                customLayoutTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setDescriptionColor(ColorStateList color) {
        if (customTvDescription != null && color != null)
            customTvDescription.setTextColor(color);
    }

    public void setRequired(boolean isRequired) {
        if (customTvTitle != null && isRequired) {
            if (isInEditMode())
                customImgRequired.setVisibility(View.VISIBLE);
            else
                customTvTitle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (customTvTitle.getLineCount() > 1) {
                            if (customImgMultilineRequired != null)
                                customImgMultilineRequired.setVisibility(View.VISIBLE);
                        } else if (customImgRequired != null)
                            customImgRequired.setVisibility(View.VISIBLE);
                    }
                });

        }
    }

    public void setError(String error) {
        if (customEtTitle != null && error != null)
            customEtTitle.setError(error);
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

    private void setInputHeight(float inputHeight) {
        if (inputHeight > 0)
            customLayoutParentContent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) inputHeight));
    }
    /*-------------------------------------[SAVE STATE]-------------------------------------------*/

    //    @Override
    //    public Parcelable onSaveInstanceState() {
    //        Parcelable superState = super.onSaveInstanceState();
    //        SavedState ss = new SavedState(superState);
    //        ss.childrenStates = new SparseArray();
    //        for (int i = 0; i < getChildCount(); i++) {
    //            getChildAt(i).saveHierarchyState(ss.childrenStates);
    //        }
    //        return ss;
    //    }
    //
    //    @Override
    //    public void onRestoreInstanceState(Parcelable state) {
    //        SavedState ss = (SavedState) state;
    //        super.onRestoreInstanceState(ss.getSuperState());
    //        for (int i = 0; i < getChildCount(); i++) {
    //            getChildAt(i).restoreHierarchyState(ss.childrenStates);
    //        }
    //    }
    //
    //    @Override
    //    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
    //        dispatchFreezeSelfOnly(container);
    //    }
    //
    //    @Override
    //    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
    //        dispatchThawSelfOnly(container);
    //    }
    //
    //    public static class SavedState extends BaseSavedState {
    //        SparseArray childrenStates;
    //
    //        SavedState(Parcelable superState) {
    //            super(superState);
    //        }
    //
    //        private SavedState(Parcel in, ClassLoader classLoader) {
    //            super(in);
    //            childrenStates = in.readSparseArray(classLoader);
    //        }
    //
    //        @Override
    //        public void writeToParcel(Parcel out, int flags) {
    //            super.writeToParcel(out, flags);
    //            out.writeSparseArray(childrenStates);
    //        }
    //
    //        public static final ClassLoaderCreator<SavedState> CREATOR
    //                = new ClassLoaderCreator<SavedState>() {
    //            @Override
    //            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
    //                return new SavedState(source, loader);
    //            }
    //
    //            @Override
    //            public SavedState createFromParcel(Parcel source) {
    //                return createFromParcel(null);
    //            }
    //
    //            public SavedState[] newArray(int size) {
    //                return new SavedState[size];
    //            }
    //        };
    //    }
}
