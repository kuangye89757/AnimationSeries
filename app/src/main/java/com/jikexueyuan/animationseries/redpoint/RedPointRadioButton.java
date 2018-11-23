/**
 * Copyright 2015 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
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
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 红点RadioButton
 * @author shijie9
 */
public class RedPointRadioButton extends AppCompatRadioButton implements RedPointAble {
    
    private RedPointViewHelper mRedPointViewHelper;

    public RedPointRadioButton(Context context) {
        this(context, null);
    }

    public RedPointRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.radioButtonStyle);
    }

    public RedPointRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRedPointViewHelper = new RedPointViewHelper(this, context, attrs, RedPointViewHelper.RedPointGravity.RightTop);
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