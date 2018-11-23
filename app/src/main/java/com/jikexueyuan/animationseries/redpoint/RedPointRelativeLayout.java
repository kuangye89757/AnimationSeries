/**
 * Copyright 2015 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jikexueyuan.animationseries.redpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * 红点RelativeLayout
 * @author shijie9
 */
public class RedPointRelativeLayout extends RelativeLayout implements RedPointAble {
    private RedPointViewHelper mRedPointViewHelper;

    public RedPointRelativeLayout(Context context) {
        this(context, null);
    }

    public RedPointRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPointRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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