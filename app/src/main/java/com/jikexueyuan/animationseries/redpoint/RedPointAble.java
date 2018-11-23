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
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * 红点接口
 */
public interface RedPointAble extends RedpointManager.RedpointParent{
    /**
     * 显示圆点红点
     */
    void showCircleRedPoint();

    /**
     * 显示文字红点
     *
     * @param pointText
     */
    void showTextRedPoint(String pointText);

    /**
     * 隐藏红点
     */
    void hiddenRedPoint();

    /**
     * 显示图像红点
     *
     * @param bitmap
     */
    void showDrawableRedPoint(Bitmap bitmap);

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param event
     * @return
     */
    boolean callSuperOnTouchEvent(MotionEvent event);

    /**
     * 拖动大于RedPointViewHelper.mMoveHiddenThreshold后抬起手指红点消失的代理
     *
     * @param delegate
     */
    void setDragDismissDelegage(RedPointDragDismissDelegate delegate);

    /**
     * 是否显示红点
     *
     * @return
     */
    boolean isShowRedPoint();

    RedPointViewHelper getRedPointViewHelper();

    int getWidth();

    int getHeight();

    void redPointPostInvalidate();

    ViewParent getParent();

    int getId();

    boolean getGlobalVisibleRect(Rect r);

    Context getContext();

    View getRootView();
}