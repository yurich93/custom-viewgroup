package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;


public class MyCustomLayout extends ViewGroup {

    public MyCustomLayout(Context context) {
        super(context);
    }

    public MyCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("OnMeasure", "Entering onMeasure");

        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);

        int placeTakenByNonMatchParent = 0;

        View matchParentView = null;

        int childCount = getChildCount();

        try {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);

                int childWidth = child.getLayoutParams().width;

                //Two match_parent elements are not
                //supposed to be here
                if (childWidth == LayoutParams.MATCH_PARENT) {
                    if (matchParentView == null) {
                        matchParentView = child;
                    } else {
                        throw new IOException();
                    }
                } else {
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);
                    placeTakenByNonMatchParent += child.getMeasuredWidth();
                }
            }

            if (matchParentView != null) {
                int rest = MeasureSpec.makeMeasureSpec(
                        layoutWidth - placeTakenByNonMatchParent,
                        MeasureSpec.EXACTLY
                );

                matchParentView.measure(rest, heightMeasureSpec);
            }

            setMeasuredDimension(layoutWidth, layoutHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int top = getPaddingTop();
        int left = getPaddingLeft();

        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            left = right;
        }
    }
}
