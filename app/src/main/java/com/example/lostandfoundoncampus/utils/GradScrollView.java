package com.example.lostandfoundoncampus.utils;

import android.content.Context;
import android.print.PrinterId;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * created by XiaoAnDev on 2021/4/11
 * 自定义滑动框
 */
public class GradScrollView extends ScrollView {
    public interface ScrollViewListener {
        void onScrollChanged(GradScrollView scrollView, int x, int y,
                             int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public GradScrollView(Context context) {
        super(context);
    }

    public GradScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null)
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
    }
}
