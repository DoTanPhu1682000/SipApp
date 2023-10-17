package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TitleView extends BaseCustomView implements View.OnClickListener {

    private LinearLayout customLayoutTitle;
    private TextView customTvTitle;
    private TextView customTvDescription;
    private ImageView customImgRequired;
    private ImageView customImgMultilineRequired;
    private ButtonImageView customBiHelp;
    private String mHelp = null;

    private Context context;

    public TitleView(Context context) {
        super(context);
        init(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_title_view, this);

            customLayoutTitle = findViewById(R.id.customLayoutTitle);
            customTvTitle = findViewById(R.id.customTvTitle);
            customTvDescription = findViewById(R.id.customTvDescription);
            customImgRequired = findViewById(R.id.customImgRequired);
            customImgMultilineRequired = findViewById(R.id.customImgMultilineRequired);
            customBiHelp = findViewById(R.id.customBiHelp);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleView, 0, 0);
                try {
                    String title = ta.getString(R.styleable.TitleView_tv_title);
                    String description = ta.getString(R.styleable.TitleView_tv_description);
                    String help = ta.getString(R.styleable.TitleView_tv_help);
                    boolean isRequired = ta.getBoolean(R.styleable.TitleView_tv_required, false);
                    ColorStateList titleColor = ta.getColorStateList(R.styleable.TitleView_tv_titleColor);
                    float titleSize = ta.getDimension(R.styleable.TitleView_tv_titleSize, -1);
                    int titleStyle = ta.getInteger(R.styleable.TitleView_tv_titleStyle, -1);
                    ColorStateList descriptionColor = ta.getColorStateList(R.styleable.TitleView_tv_descriptionColor);
                    int descriptionStyle = ta.getInteger(R.styleable.TitleView_tv_descriptionStyle, -1);
                    float descriptionSize = ta.getDimension(R.styleable.TitleView_tv_descriptionSize, -1);

                    setTitle(title);
                    setTitleColor(titleColor);
                    setTitleSize(titleSize);
                    setTitleStyle(titleStyle);
                    setDescription(description);
                    setDescriptionColor(descriptionColor);
                    setDescriptionStyle(descriptionStyle);
                    setDescriptionSize(descriptionSize);
                    setHelp(help);
                    setRequired(isRequired);

                } finally {
                    ta.recycle();
                }
            }
        }
    }

    /*-----------------------------[IMPLEMENT]----------------------------------------------------*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.customBiHelp && customBiHelp != null && customBiHelp.getContext() != null && mHelp != null) {
            // ToastColor.info(customBiHelp.getContext(), help, ToastColor.LENGTH_LONG);
            Tooltip.info(customBiHelp.getContext(), mHelp, customBiHelp);

        }
    }

    /*-----------------------------[METHOD]-------------------------------------------------------*/
    public void setDescriptionStyle(int textStyle) {
        setFont(context, customTvDescription, textStyle);
    }

    public void setDescriptionSize(float value) {
        if (customTvDescription != null && value > 0)
            customTvDescription.setTextSize(value / getResources().getDisplayMetrics().scaledDensity);
    }

    public String getTitle() {
        return customTvTitle != null ? customTvTitle.getText().toString().trim() : "";
    }

    public String getDescription() {
        return customTvDescription != null ? customTvDescription.getText().toString().trim() : "";
    }

    public void setTitle(String title) {
        if (customTvTitle != null && title != null) {
            customTvTitle.setText(title);
            if (customLayoutTitle.getVisibility() != View.VISIBLE)
                customLayoutTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(context, customTvTitle, textStyle);
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
        if (customTvDescription != null) {
            if (description != null) {
                customTvDescription.setVisibility(View.VISIBLE);
                customTvDescription.setText(description);
            } else
                customTvDescription.setVisibility(View.GONE);
        }

    }

    public void setDescriptionColor(ColorStateList color) {
        if (customTvDescription != null && color != null)
            customTvDescription.setTextColor(color);
    }

    public void setHelp(String help) {
        if (customBiHelp == null)
            return;

        if (help == null) {
            customBiHelp.setVisibility(View.GONE);
            return;
        }

        this.mHelp = help;
        customBiHelp.setVisibility(View.VISIBLE);
        customBiHelp.setOnClickListener(this);
        if (customLayoutTitle.getVisibility() != View.VISIBLE)
            customLayoutTitle.setVisibility(View.VISIBLE);
    }

    public void setRequired(boolean isRequired) {
        if (customTvTitle != null) {
//            if (isInEditMode())
//                customImgRequired.setVisibility(isRequired?View.VISIBLE:View.GONE);
//            else
//                customTvTitle.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (customTvTitle.getLineCount() > 1) {
//                            if (customImgMultilineRequired != null)
//                                customImgMultilineRequired.setVisibility(View.VISIBLE);
//                        } else if (customImgRequired != null)
//                            customImgRequired.setVisibility(View.VISIBLE);
//                    }
//                });
            try {
                customImgRequired.setVisibility(isRequired ? View.VISIBLE : View.GONE);
            } catch (Exception e) {
                //ignored
            }
        }
    }

    public TextView getTextViewDescription() {
        return customTvDescription;
    }

    public TextView gettTextViewTittle() {
        return customTvTitle;
    }
}
