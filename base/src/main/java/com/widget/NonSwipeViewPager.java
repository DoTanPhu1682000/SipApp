package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.utils.LogUtil;
import com.custom.MyScroller;

import java.lang.reflect.Field;

/**
 * Disable paging by swiping with finger in ViewPager
 * https://stackoverflow.com/questions/9650265/how-do-disable-paging-by-swiping-with-finger-in-viewpager-but-still-be-able-to-s
 */
public class NonSwipeViewPager extends ViewPager {
    //private OnItemClickListener mOnItemClickListener;

    public NonSwipeViewPager(Context context) {
        super(context);
        //init();
        setMyScroller();
    }

    public NonSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // init();
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

//    @Override
//    public boolean canScrollHorizontally(int direction) {
//        return false;
//    }
//
//    private void init() {
//        final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());
//
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                tapGestureDetector.onTouchEvent(event);
//                return false;
//            }
//        });
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            if (mOnItemClickListener != null) {
//                mOnItemClickListener.onItemClick(getCurrentItem());
//            }
//            return true;
//        }
//    }
}
