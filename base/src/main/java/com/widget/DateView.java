package com.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;

import com.utils.DatePickerSpinnerUtil;
import com.utils.LogUtil;
import com.utils.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DateView extends BaseCustomView implements View.OnClickListener {
    private LinearLayout customLayoutParentTitle;
    private LinearLayout customLayoutTitle;
    private TextView customTvTitle;
    private TextView customTvDescription;
    private ImageView customImgRequired;
    private ImageView customImgMultilineRequired;
    private ButtonImageView customBiHelp;
    private String mHelp = null;

    private LinearLayout customLayoutParent;
    private TextView customTvDate;
    private ImageView customImgLeft;
    private ImageView customImgRight;
    private Space customSpaceLeft;
    private Space customSpaceRight;
    private Space customPaddingLeft;
    private Space customPaddingRight;

    private Calendar mMinDate;
    private Calendar mMaxDate;
    private Calendar mDefaultDate;
    private OnChooseDateListener mOnChooseDateListener;
    private Activity mActivity;
    private Context mContext;
    // set enable Dateview
    private boolean flag = true;

    private String mDateFormat = "dd/MM/yyyy";

    public interface OnChooseDateListener {
        void onChooseDate(int day, int month, int year);
    }

    /*--------------------------------------[METHOD]----------------------------------------------*/
    public DateView(Context context) {
        super(context);
        init(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            inflater.inflate(R.layout.custom_date_view, this);
            customLayoutTitle = findViewById(R.id.customLayoutTitle);
            customTvTitle = findViewById(R.id.customTvTitle);
            customTvDescription = findViewById(R.id.customTvDescription);
            customImgRequired = findViewById(R.id.customImgRequired);
            customImgMultilineRequired = findViewById(R.id.customImgMultilineRequired);
            customBiHelp = findViewById(R.id.customBiHelp);
            customLayoutParentTitle = findViewById(R.id.customLayoutParentTitle);

            customLayoutParent = findViewById(R.id.customLayoutParent);
            customSpaceLeft = findViewById(R.id.customSpaceLeft);
            customSpaceRight = findViewById(R.id.customSpaceRight);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);
            customImgLeft = findViewById(R.id.customImgLeft);
            customImgRight = findViewById(R.id.customImgRight);

            customTvDate = findViewById(R.id.customTvDate);
            mMinDate = Calendar.getInstance();

            if (attrs != null) {
                if (!isInEditMode())
                    mActivity = scanForActivity(context);
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DateView, 0, 0);
                try {
                    String dateFormat = ta.getString(R.styleable.DateView_dv_dateFormat);
                    setDateFormat(dateFormat);
                    String title = ta.getString(R.styleable.DateView_dv_title);
                    ColorStateList titleColor = ta.getColorStateList(R.styleable.DateView_dv_titleColor);
                    int titleStyle = ta.getInteger(R.styleable.DateView_dv_titleStyle, -1);
                    int textStyle = ta.getInteger(R.styleable.DateView_dv_textStyle, -1);
                    float titleSize = ta.getDimension(R.styleable.DateView_dv_titleSize, -1);
                    setTextStyle(textStyle);
                    setTitle(title);
                    setTitleStyle(titleStyle);
                    setTitleColor(titleColor);
                    setTitleSize(titleSize);

                    ColorStateList textColor = ta.getColorStateList(R.styleable.DateView_dv_textColor);
                    float textSize = ta.getDimension(R.styleable.DateView_dv_textSize, -1);
                    setTextColor(textColor);
                    setTextSize(textSize);

                    String description = ta.getString(R.styleable.DateView_dv_description);
                    ColorStateList descriptionColor = ta.getColorStateList(R.styleable.DateView_dv_descriptionColor);
                    setDescription(description);
                    setDescriptionColor(descriptionColor);

                    String help = ta.getString(R.styleable.DateView_dv_help);
                    boolean isRequired = ta.getBoolean(R.styleable.DateView_dv_required, false);
                    Drawable background = ta.getDrawable(R.styleable.DateView_dv_background);
                    Drawable iconLeft = ta.getDrawable(R.styleable.DateView_dv_iconLeft);
                    Drawable iconRight = ta.getDrawable(R.styleable.DateView_dv_iconRight);

                    setHelp(help);
                    setRequired(isRequired);

                    setTextBackground(background);
                    setIconLeft(iconLeft);
                    setIconRight(iconRight);

                    float iconLeftSize = ta.getDimension(R.styleable.DateView_dv_iconLeftSize, -1);
                    float iconRightSize = ta.getDimension(R.styleable.DateView_dv_iconRightSize, -1);
                    setIconLeftSize(iconLeftSize);
                    setIconRightSize(iconRightSize);

                    float spaceLeft = ta.getDimension(R.styleable.DateView_dv_spaceLeft, -1);
                    float spaceRight = ta.getDimension(R.styleable.DateView_dv_spaceRight, -1);
                    setSpaceLeft(spaceLeft);
                    setSpaceRight(spaceRight);

                    float paddingLeft = ta.getDimension(R.styleable.DateView_dv_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.DateView_dv_paddingRight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);
                    int textGravity = ta.getInteger(R.styleable.DateView_dv_textGravity, -1);
                    setTextGravity(textGravity);
                    if (customTvDescription.getVisibility() == View.VISIBLE || customLayoutTitle.getVisibility() == View.VISIBLE)
                        customLayoutParentTitle.setVisibility(View.VISIBLE);
                    else
                        customLayoutParentTitle.setVisibility(View.GONE);

                    String hint = ta.getString(R.styleable.DateView_dv_hint);
                    setHint(hint);
                    customLayoutParent.setOnClickListener(this);

                    float inputHeight = ta.getDimension(R.styleable.DateView_dv_inputHeight, -1);
                    setInputHeight(inputHeight);

                } finally {
                    ta.recycle();
                }
            }
        }
    }

    /*--------------------------------------[IMPLEMENT]-------------------------------------------*/

    @Override
    public void onClick(View v) {
        if (flag) {
            if (v.getId() == R.id.customBiHelp) {
                if (customBiHelp != null && customBiHelp.getContext() != null && mHelp != null)
                    Tooltip.info(customBiHelp.getContext(), mHelp, customBiHelp);
            } else if (v.getId() == R.id.customLayoutParent) {
                show();
            }
        }
    }


    private String convertDisplayDate(int dayOfMonth, int monthOfYear, int year) {
        SimpleDateFormat format = new SimpleDateFormat(mDateFormat, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        return format.format(c.getTime());
    }

    public void show() {
        if (mActivity != null) {
            String textDate = customTvDate.getText().toString().trim();
            if (TextUtils.isEmpty(textDate)) {
                DatePickerSpinnerUtil.createAndShowDatePicker(mActivity, mMinDate, mMaxDate, mDefaultDate, new DatePickerSpinnerUtil.OnDatePickerListener() {
                    @Override
                    public void onDateSet(int dayOfMonth, int monthOfYear, int year) {
                        customTvDate.setText(convertDisplayDate(dayOfMonth, monthOfYear, year));

                        if (mOnChooseDateListener != null) {
                            mOnChooseDateListener.onChooseDate(dayOfMonth, monthOfYear, year);
                        }
                    }
                });
            } else {
                DatePickerSpinnerUtil.createAndShowDatePickerData(mActivity, mDateFormat, textDate, mMinDate, mMaxDate, new DatePickerSpinnerUtil.OnDatePickerListener() {
                    @Override
                    public void onDateSet(int dayOfMonth, int monthOfYear, int year) {
                        customTvDate.setText(convertDisplayDate(dayOfMonth, monthOfYear, year));
                        if (mOnChooseDateListener != null) {
                            mOnChooseDateListener.onChooseDate(dayOfMonth, monthOfYear, year);
                        }
                    }
                });
            }
        }
    }

    /*--------------------------------------[PRIVATE]---------------------------------------------*/
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


    /*--------------------------------------[GETTER]----------------------------------------------*/
    public String getTitle() {
        return customTvTitle != null ? customTvTitle.getText().toString().trim() : "";
    }

    public String getText() {
        return customTvDate.getText().toString();
    }


    // Get lottie View
    public TextView getViewTvDate() {
        return customTvDate;
    }

    /*--------------------------------------[TITLE]-----------------------------------------------*/
    private void setInputHeight(float inputHeight) {
        if (inputHeight > 0)
            customLayoutParent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) inputHeight));
    }

    public void setHint(String hint) {
        if (customTvDate != null && hint != null)
            customTvDate.setHint(hint);
    }

    public void setTitle(String title) {
        if (customTvTitle != null && title != null) {
            customTvTitle.setText(title);
            if (customLayoutTitle.getVisibility() != View.VISIBLE)
                customLayoutTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setTextStyle(int textStyle) {
        setFont(mContext, customTvDate, textStyle);
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

    public void setTextSize(float textSize) {
        if (customTvDate != null && textSize > 0)
            customTvDate.setTextSize(textSize / getResources().getDisplayMetrics().scaledDensity);
    }

    public void setTextColor(ColorStateList textColor) {
        if (customTvDate != null && textColor != null)
            customTvDate.setTextColor(textColor);
    }

    public void setHelp(String help) {
        if (customBiHelp != null && help != null) {
            customBiHelp.setVisibility(View.VISIBLE);
            this.mHelp = help;
            customBiHelp.setOnClickListener(this);
        }
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


    /*--------------------------------------[SETTER]----------------------------------------------*/

    public void setDateFormat(String dateFormat) {
        if (!TextUtils.isEmpty(dateFormat))
            this.mDateFormat = dateFormat;
    }

    public void setTextGravity(int textLeftGravity) {
        if (customTvDate != null && textLeftGravity >= 0) {
            if (textLeftGravity == 0)
                customTvDate.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL | Gravity.START);
            else if (textLeftGravity == 1)
                customTvDate.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL | Gravity.END);
            else if (textLeftGravity == 2)
                customTvDate.setGravity(Gravity.CENTER);
        }
    }

    public void setText(String text) {
        if (customTvDate != null && text != null)
            customTvDate.setText(text);
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

    public void setIconRight(Drawable res) {
        if (customImgRight != null && res != null) {
            ViewUtil.setImageDrawable(customImgRight, res);
            if (customImgRight.getVisibility() != View.VISIBLE)
                customImgRight.setVisibility(View.VISIBLE);
            if (customSpaceRight.getVisibility() != View.VISIBLE)
                customSpaceRight.setVisibility(View.VISIBLE);
        }
    }
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
    public void setTextBackground(Drawable background) {
        ViewUtil.setBackground(customLayoutParent, background);
    }

    public void setText(CharSequence text) {
        customTvDate.setText(text != null ? text : "");
    }

    public void setDefaultDate(Calendar calendar) {
        if (mDefaultDate == null)
            mDefaultDate = Calendar.getInstance();
        this.mDefaultDate = calendar;
    }

    public void setDefaultDate(int year, int month, int day) {
        if (mDefaultDate == null)
            mDefaultDate = Calendar.getInstance();
        mDefaultDate.set(year, month, day);
    }


    public void setMinDate(Calendar calendar) {
        mMinDate = calendar;
    }

    public void setMinDate(int year, int month, int day) {
        mMinDate.set(year, month, day);
    }

    public void setMaxDate(Calendar calendar) {
        mMaxDate = calendar;
    }

    public void setMaxDate(int year, int month, int day) {
        if (mMaxDate == null) {
            mMaxDate = Calendar.getInstance();
        }
        mMaxDate.set(year, month, day);
    }

    public void setOnChooseDateListener(OnChooseDateListener listener) {
        this.mOnChooseDateListener = listener;
    }

    public void setIconLeftSize(float value) {
        if (value > 0) {
            LayoutParams layoutParams = new LayoutParams((int) value, (int) value);
            customImgLeft.setLayoutParams(layoutParams);
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

    // set enable Dateview
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}