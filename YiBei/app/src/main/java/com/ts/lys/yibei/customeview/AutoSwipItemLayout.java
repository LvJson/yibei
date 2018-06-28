package com.ts.lys.yibei.customeview;

import android.content.Context;
import android.util.AttributeSet;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public class AutoSwipItemLayout extends SwipeItemLayout {
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoSwipItemLayout(Context context) {
        super(context);
    }

    public AutoSwipItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public AutoFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
