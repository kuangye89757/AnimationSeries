package com.jikexueyuan.animationseries.redpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 红点View
 * @author shijie9
 */
public class RedPointView extends View implements RedPointAble {
    private RedPointViewHelper mRedPointViewHelper;

    public RedPointView(Context context) {
        this(context, null);
    }

    public RedPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRedPointViewHelper = new RedPointViewHelper(this, context, attrs, RedPointViewHelper.RedPointGravity.RightCenter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mRedPointViewHelper.onTouchEvent(event);
    }

    @Override
    public boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRedPointViewHelper.drawBadge(canvas);
    }

    @Override
    public void showCircleRedPoint() {
        mRedPointViewHelper.showCirclePointBadge();
    }

    @Override
    public void showTextRedPoint(String badgeText) {
        mRedPointViewHelper.showTextBadge(badgeText);
    }

    @Override
    public void hiddenRedPoint() {
        mRedPointViewHelper.hiddenBadge();
    }

    @Override
    public void showDrawableRedPoint(Bitmap bitmap) {
        mRedPointViewHelper.showDrawable(bitmap);
    }

    @Override
    public void setDragDismissDelegage(RedPointDragDismissDelegate delegate) {
        mRedPointViewHelper.setDragDismissDelegage(delegate);
    }

    @Override
    public boolean isShowRedPoint() {
        return mRedPointViewHelper.isShowRedPoint();
    }

    @Override
    public RedPointViewHelper getRedPointViewHelper() {
        return mRedPointViewHelper;
    }

    @Override
    public void redPointPostInvalidate() {
        super.postInvalidate();
    }

    @Override
    public void registerRedGroup(int... groupIds) {
        //注册红点组
        for (int groupId : groupIds) {
            RedpointManager.register(this, groupId);
        }
    }

    @Override
    public void unregisterRedGroup() {
        //解绑红点组
        RedpointManager.unregister(this);
    }


    @Override
    public void showPoint() {
        showCircleRedPoint();
    }

    @Override
    public void hindPoint() {
        hiddenRedPoint();
    }
}