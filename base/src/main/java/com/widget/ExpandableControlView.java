package com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.utils.ViewUtil;


public class ExpandableControlView extends BaseCustomView implements View.OnClickListener {

    private LinearLayout customLayoutParent;
    private TextView customTvTitle;
    private TextView customTvDescription;
    private ImageView customImgLeft;
    private ImageView customImgControl;
    private Space customSpaceLeft;
    private Space customPaddingLeft;
    private Space customPaddingRight;
    private View view;
    private int viewId;
    private Context mContext;
    private boolean mIsExpandable = true;
    private Drawable iconUp;
    private Drawable iconDown;
    /*--------------------------------------[METHOD]----------------------------------------------*/

    public ExpandableControlView(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandableControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (viewId != 0)
            setLayoutContent(viewId);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_expandable_control_view, this);
            customLayoutParent = findViewById(R.id.customLayoutParent);
            customTvTitle = findViewById(R.id.customTvTitle);
            customTvDescription = findViewById(R.id.customTvDescription);
            customImgControl = findViewById(R.id.customImgControl);
            customImgLeft = findViewById(R.id.customImgLeft);
            customPaddingLeft = findViewById(R.id.customPaddingLeft);
            customPaddingRight = findViewById(R.id.customPaddingRight);
            customSpaceLeft = findViewById(R.id.customSpaceLeft);
            customLayoutParent.setOnClickListener(this);
            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableControlView, 0, 0);
                try {
                    viewId = ta.getResourceId(R.styleable.ExpandableControlView_ecv_control, 0);

                    String title = ta.getString(R.styleable.ExpandableControlView_ecv_title);
                    int titleStyle = ta.getInteger(R.styleable.ExpandableControlView_ecv_titleStyle, -1);
                    float titleSize = ta.getDimension(R.styleable.ExpandableControlView_ecv_titleSize, -1);
                    ColorStateList titleColor = ta.getColorStateList(R.styleable.ExpandableControlView_ecv_titleColor);
                    setTitle(title);
                    setTitleColor(titleColor);
                    setTitleSize(titleSize);
                    setTitleStyle(titleStyle);

                    String description = ta.getString(R.styleable.ExpandableControlView_ecv_description);
                    ColorStateList descriptionColor = ta.getColorStateList(R.styleable.ExpandableControlView_ecv_descriptionColor);
                    setDescription(description);
                    setDescriptionColor(descriptionColor);

                    Drawable background = ta.getDrawable(R.styleable.ExpandableControlView_ecv_background);
                    iconUp = ta.getDrawable(R.styleable.ExpandableControlView_ecv_iconUp);
                    iconDown = ta.getDrawable(R.styleable.ExpandableControlView_ecv_iconDown);
                    mIsExpandable = ta.getBoolean(R.styleable.ExpandableControlView_ecv_isExpanded, true);
                    setBackgroundView(background);
                    if (iconUp == null)
                        iconUp = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_grey);
                    if (iconDown == null)
                        iconDown = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_grey);
                    customImgControl.post(new Runnable() {
                        @Override
                        public void run() {
                            setExpanded(mIsExpandable);
                        }
                    });
                    float paddingLeft = ta.getDimension(R.styleable.ExpandableControlView_ecv_paddingLeft, -1);
                    float paddingRight = ta.getDimension(R.styleable.ExpandableControlView_ecv_paddingRight, -1);
                    setPaddingLeft(paddingLeft);
                    setPaddingRight(paddingRight);

                    Drawable iconLeft = ta.getDrawable(R.styleable.ExpandableControlView_ecv_iconLeft);
                    float iconLeftSize = ta.getDimension(R.styleable.ExpandableControlView_ecv_iconLeftSize, -1);
                    setIconLeft(iconLeft);
                    setIconLeftSize(iconLeftSize);
                    float spaceLeft = ta.getDimension(R.styleable.ExpandableControlView_ecv_spaceLeft, -1);
                    setSpaceLeft(spaceLeft);


                } finally {
                    ta.recycle();
                }
            }
        }
    }
    /*--------------------------------------[IMPLEMENT]-------------------------------------------*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.customLayoutParent)
            toggleExpanded();
    }

    /*--------------------------------------[GETTER]----------------------------------------------*/

    public String getTitle() {
        return customTvTitle != null ? customTvTitle.getText().toString().trim() : "";
    }

    /*--------------------------------------[SETTER]----------------------------------------------*/


    private void setLayoutContent(int viewId) {
        try {
            view = ((View) getParent()).findViewById(viewId);
        } catch (Exception e) {
            view = null;
        }
    }


    /*-----------------------------[PRIVATE]------------------------------------------------------*/
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

    public void setTitle(String title) {
        if (customTvTitle != null && title != null)
            customTvTitle.setText(title);
    }

    public void setTitleStyle(int textStyle) {
        //if (!isInEditMode())
        setFont(mContext, customTvTitle, textStyle);
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
        }
    }

    public void setDescriptionColor(ColorStateList color) {
        if (customTvDescription != null && color != null)
            customTvDescription.setTextColor(color);
    }

    public void setBackgroundView(Drawable background) {
        ViewUtil.setBackground(customLayoutParent, background);
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpandable = isExpanded;
        ViewUtil.setImageDrawable(customImgControl, (mIsExpandable ? iconUp : iconDown));
        if (view != null)
            view.setVisibility(mIsExpandable ? View.VISIBLE : View.GONE);
    }

    private void toggleExpanded() {
        mIsExpandable = !mIsExpandable;
        ViewUtil.setImageDrawable(customImgControl, (mIsExpandable ? iconUp : iconDown));
        if (view != null)
            view.setVisibility(mIsExpandable ? View.VISIBLE : View.GONE);
//            if (mIsExpandable)
//                ViewUtil.expand(view);
//            else
//                ViewUtil.collapse(view);
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
