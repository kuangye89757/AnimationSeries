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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jikexueyuan.animationseries.R;


/**
 * 红点帮助类
 *
 * @author shijie9
 */
public class RedPointViewHelper {
    private Bitmap mBitmap;
    private RedPointAble mRedPointAble;
    private Paint mPaint;
    /**
     * 红点背景色
     */
    private int mRedPointBgColor;
    /**
     * 红点文本的颜色
     */
    private int mRedPointTextColor;
    /**
     * 红点文本字体大小
     */
    private int mRedPointTextSize;
    /**
     * 红点背景与宿主控件上下边缘间距离
     */
    private int mRedPointVerticalMargin;
    /**
     * 红点背景与宿主控件左右边缘间距离
     */
    private int mRedPointHorizontalMargin;
    /***
     * 红点文本边缘与红点背景边缘间的距离
     */
    private int mRedPointPadding;
    /**
     * 红点文本
     */
    private String mRedPointText;
    /**
     * 红点文本所占区域大小
     */
    private Rect mRedPointNumberRect;
    /**
     * 是否显示RedPoint
     */
    private boolean mIsShowRedPoint;
    /**
     * 红点在宿主控件中的位置
     */
    private RedPointGravity mRedPointGravity;
    /**
     * 整个红点所占区域
     */
    private RectF mRedPointRectF;
    /**
     * 是否可拖动
     */
    private boolean mDragable;
    /**
     * 拖拽红点超出轨迹范围后，再次放回到轨迹范围时，是否恢复轨迹
     */
    private boolean mIsResumeTravel;
    /***
     * 红点描边宽度
     */
    private int mRedPointBorderWidth;
    /***
     * 红点描边颜色
     */
    private int mRedPointBorderColor;
    /**
     * 触发开始拖拽红点事件的扩展触摸距离
     */
    private int mDragExtra;
    /**
     * 整个红点加上其触发开始拖拽区域所占区域
     */
    private RectF mRedPointDragExtraRectF;
    /**
     * 拖动时的红点控件
     */
    private RedPointDragView mDropBadgeView;
    /**
     * 是否正在拖动
     */
    private boolean mIsDraging;
    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指红点消失的代理
     */
    private RedPointDragDismissDelegate mDelegage;
    private boolean mIsShowDrawable = false;

    public RedPointViewHelper(RedPointAble redPointAble, Context context, AttributeSet attrs, RedPointGravity defaultBadgeGravity) {
        mRedPointAble = redPointAble;
        initDefaultAttrs(context, defaultBadgeGravity);
        initCustomAttrs(context, attrs);
        afterInitDefaultAndCustomAttrs();
        mDropBadgeView = new RedPointDragView(context, this);
    }

    private void initDefaultAttrs(Context context, RedPointGravity defaultBadgeGravity) {
        mRedPointNumberRect = new Rect();
        mRedPointRectF = new RectF();
        mRedPointBgColor = Color.RED;
        mRedPointTextColor = Color.WHITE;
        mRedPointTextSize = RedPointViewUtil.sp2px(context, 10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        // 设置mBadgeText居中，保证mBadgeText长度为1时，文本也能居中
        mPaint.setTextAlign(Paint.Align.CENTER);

        mRedPointPadding = RedPointViewUtil.dp2px(context, 4);
        mRedPointVerticalMargin = RedPointViewUtil.dp2px(context, 4);
        mRedPointHorizontalMargin = RedPointViewUtil.dp2px(context, 4);

        mRedPointGravity = defaultBadgeGravity;
        mIsShowRedPoint = false;

        mRedPointText = null;

        mBitmap = null;

        mIsDraging = false;

        mDragable = false;

        mRedPointBorderColor = Color.WHITE;

        mDragExtra = RedPointViewUtil.dp2px(context, 4);
        mRedPointDragExtraRectF = new RectF();
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RedPointView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.RedPointView_red_point_bgColor) {
            mRedPointBgColor = typedArray.getColor(attr, mRedPointBgColor);
        } else if (attr == R.styleable.RedPointView_red_point_textColor) {
            mRedPointTextColor = typedArray.getColor(attr, mRedPointTextColor);
        } else if (attr == R.styleable.RedPointView_red_point_textSize) {
            mRedPointTextSize = typedArray.getDimensionPixelSize(attr, mRedPointTextSize);
        } else if (attr == R.styleable.RedPointView_red_point_verticalMargin) {
            mRedPointVerticalMargin = typedArray.getDimensionPixelSize(attr, mRedPointVerticalMargin);
        } else if (attr == R.styleable.RedPointView_red_point_horizontalMargin) {
            mRedPointHorizontalMargin = typedArray.getDimensionPixelSize(attr, mRedPointHorizontalMargin);
        } else if (attr == R.styleable.RedPointView_red_point_padding) {
            mRedPointPadding = typedArray.getDimensionPixelSize(attr, mRedPointPadding);
        } else if (attr == R.styleable.RedPointView_red_point_gravity) {
            int ordinal = typedArray.getInt(attr, mRedPointGravity.ordinal());
            mRedPointGravity = RedPointGravity.values()[ordinal];
        } else if (attr == R.styleable.RedPointView_red_point_dragable) {
            mDragable = typedArray.getBoolean(attr, mDragable);
        } else if (attr == R.styleable.RedPointView_red_point_isResumeTravel) {
            mIsResumeTravel = typedArray.getBoolean(attr, mIsResumeTravel);
        } else if (attr == R.styleable.RedPointView_red_point_borderWidth) {
            mRedPointBorderWidth = typedArray.getDimensionPixelSize(attr, mRedPointBorderWidth);
        } else if (attr == R.styleable.RedPointView_red_point_borderColor) {
            mRedPointBorderColor = typedArray.getColor(attr, mRedPointBorderColor);
        } else if (attr == R.styleable.RedPointView_red_point_dragExtra) {
            mDragExtra = typedArray.getDimensionPixelSize(attr, mDragExtra);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        mPaint.setTextSize(mRedPointTextSize);
    }

    public void setBadgeBgColorInt(int badgeBgColor) {
        mRedPointBgColor = badgeBgColor;
        mRedPointAble.redPointPostInvalidate();
    }

    public void setBadgeTextColorInt(int badgeTextColor) {
        mRedPointTextColor = badgeTextColor;
        mRedPointAble.redPointPostInvalidate();
    }

    public void setBadgeTextSizeSp(int badgetextSize) {
        if (badgetextSize >= 0) {
            mRedPointTextSize = RedPointViewUtil.sp2px(mRedPointAble.getContext(), badgetextSize);
            mPaint.setTextSize(mRedPointTextSize);
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setBadgeVerticalMarginDp(int badgeVerticalMargin) {
        if (badgeVerticalMargin >= 0) {
            mRedPointVerticalMargin = RedPointViewUtil.dp2px(mRedPointAble.getContext(), badgeVerticalMargin);
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setBadgeHorizontalMarginDp(int badgeHorizontalMargin) {
        if (badgeHorizontalMargin >= 0) {
            mRedPointHorizontalMargin = RedPointViewUtil.dp2px(mRedPointAble.getContext(), badgeHorizontalMargin);
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setBadgePaddingDp(int badgePadding) {
        if (badgePadding >= 0) {
            mRedPointPadding = RedPointViewUtil.dp2px(mRedPointAble.getContext(), badgePadding);
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setRedPointGravity(RedPointGravity redPointGravity) {
        if (redPointGravity != null) {
            mRedPointGravity = redPointGravity;
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setDragable(boolean dragable) {
        mDragable = dragable;
        mRedPointAble.redPointPostInvalidate();
    }

    public void setIsResumeTravel(boolean isResumeTravel) {
        mIsResumeTravel = isResumeTravel;
        mRedPointAble.redPointPostInvalidate();
    }

    public void setBadgeBorderWidthDp(int badgeBorderWidthDp) {
        if (badgeBorderWidthDp >= 0) {
            mRedPointBorderWidth = RedPointViewUtil.dp2px(mRedPointAble.getContext(), badgeBorderWidthDp);
            mRedPointAble.redPointPostInvalidate();
        }
    }

    public void setBadgeBorderColorInt(int badgeBorderColor) {
        mRedPointBorderColor = badgeBorderColor;
        mRedPointAble.redPointPostInvalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRedPointDragExtraRectF.left = mRedPointRectF.left - mDragExtra;
                mRedPointDragExtraRectF.top = mRedPointRectF.top - mDragExtra;
                mRedPointDragExtraRectF.right = mRedPointRectF.right + mDragExtra;
                mRedPointDragExtraRectF.bottom = mRedPointRectF.bottom + mDragExtra;

                if ((mRedPointBorderWidth == 0 || mIsShowDrawable) && mDragable && mIsShowRedPoint && mRedPointDragExtraRectF.contains(event.getX(), event.getY())) {
                    mIsDraging = true;
                    mRedPointAble.getParent().requestDisallowInterceptTouchEvent(true);

                    Rect badgeableRect = new Rect();
                    mRedPointAble.getGlobalVisibleRect(badgeableRect);
                    mDropBadgeView.setStickCenter(badgeableRect.left + mRedPointRectF.left + mRedPointRectF.width() / 2, badgeableRect.top + mRedPointRectF.top + mRedPointRectF.height() / 2);

                    mDropBadgeView.onTouchEvent(event);
                    mRedPointAble.redPointPostInvalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDraging) {
                    mDropBadgeView.onTouchEvent(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDraging) {
                    mDropBadgeView.onTouchEvent(event);
                    mIsDraging = false;
                    return true;
                }
                break;
            default:
                break;
        }
        return mRedPointAble.callSuperOnTouchEvent(event);
    }

    public void endDragWithDismiss() {
        hiddenBadge();
        if (mDelegage != null) {
            mDelegage.onDismiss(mRedPointAble);
        }
    }

    public void endDragWithoutDismiss() {
        mRedPointAble.redPointPostInvalidate();
    }

    public void drawBadge(Canvas canvas) {
        if (mIsShowRedPoint && !mIsDraging) {
            if (mIsShowDrawable) {
                drawDrawableBadge(canvas);
            } else {
                drawTextBadge(canvas);
            }
        }
    }

    /**
     * 绘制图像红点
     *
     * @param canvas
     */
    private void drawDrawableBadge(Canvas canvas) {
        mRedPointRectF.left = mRedPointAble.getWidth() - mRedPointHorizontalMargin - mBitmap.getWidth();
        mRedPointRectF.top = mRedPointVerticalMargin;
        switch (mRedPointGravity) {
            case RightTop:
                mRedPointRectF.top = mRedPointVerticalMargin;
                break;
            case RightCenter:
                mRedPointRectF.top = (mRedPointAble.getHeight() - mBitmap.getHeight()) / 2;
                break;
            case RightBottom:
                mRedPointRectF.top = mRedPointAble.getHeight() - mBitmap.getHeight() - mRedPointVerticalMargin;
                break;
            default:
                break;
        }
        canvas.drawBitmap(mBitmap, mRedPointRectF.left, mRedPointRectF.top, mPaint);
        mRedPointRectF.right = mRedPointRectF.left + mBitmap.getWidth();
        mRedPointRectF.bottom = mRedPointRectF.top + mBitmap.getHeight();
    }

    /**
     * 绘制文字红点
     *
     * @param canvas
     */
    private void drawTextBadge(Canvas canvas) {
        String badgeText = "";
        if (!TextUtils.isEmpty(mRedPointText)) {
            badgeText = mRedPointText;
        }
        // 获取文本宽所占宽高
        mPaint.getTextBounds(badgeText, 0, badgeText.length(), mRedPointNumberRect);
        // 计算红点背景的宽高
        int badgeHeight = mRedPointNumberRect.height() + mRedPointPadding * 2;
        int badgeWidth;
        // 当mBadgeText的长度为1或0时，计算出来的高度会比宽度大，此时设置宽度等于高度
        if (badgeText.length() == 1 || badgeText.length() == 0) {
            badgeWidth = badgeHeight;
        } else {
            badgeWidth = mRedPointNumberRect.width() + mRedPointPadding * 2;
        }

        // 计算红点背景上下的值
        mRedPointRectF.top = mRedPointVerticalMargin;
        mRedPointRectF.bottom = mRedPointAble.getHeight() - mRedPointVerticalMargin;
        switch (mRedPointGravity) {
            case RightTop:
                mRedPointRectF.bottom = mRedPointRectF.top + badgeHeight;
                break;
            case RightCenter:
                mRedPointRectF.top = (mRedPointAble.getHeight() - badgeHeight) / 2;
                mRedPointRectF.bottom = mRedPointRectF.top + badgeHeight;
                break;
            case RightBottom:
                mRedPointRectF.top = mRedPointRectF.bottom - badgeHeight;
                break;
            default:
                break;
        }

        // 计算红点背景左右的值
        mRedPointRectF.right = mRedPointAble.getWidth() - mRedPointHorizontalMargin;
        mRedPointRectF.left = mRedPointRectF.right - badgeWidth;

        if (mRedPointBorderWidth > 0) {
            // 设置红点边框景色
            mPaint.setColor(mRedPointBorderColor);
            // 绘制红点边框背景
            canvas.drawRoundRect(mRedPointRectF, badgeHeight / 2, badgeHeight / 2, mPaint);

            // 设置红点背景色
            mPaint.setColor(mRedPointBgColor);
            // 绘制红点背景
            canvas.drawRoundRect(new RectF(mRedPointRectF.left + mRedPointBorderWidth, mRedPointRectF.top + mRedPointBorderWidth, mRedPointRectF.right - mRedPointBorderWidth, mRedPointRectF.bottom - mRedPointBorderWidth), (badgeHeight - 2 * mRedPointBorderWidth) / 2, (badgeHeight - 2 * mRedPointBorderWidth) / 2, mPaint);
        } else {
            // 设置红点背景色
            mPaint.setColor(mRedPointBgColor);
            // 绘制红点背景
            canvas.drawRoundRect(mRedPointRectF, badgeHeight / 2, badgeHeight / 2, mPaint);
        }


        if (!TextUtils.isEmpty(mRedPointText)) {
            // 设置红点文本颜色
            mPaint.setColor(mRedPointTextColor);
            // initDefaultAttrs方法中设置了mBadgeText居中，此处的x为红点背景的中心点y
            float x = mRedPointRectF.left + badgeWidth / 2;
            // 注意：绘制文本时的y是指文本底部，而不是文本的中间
            float y = mRedPointRectF.bottom - mRedPointPadding;
            // 绘制红点文本
            canvas.drawText(badgeText, x, y, mPaint);
        }
    }

    public void showCirclePointBadge() {
        showTextBadge(null);
    }

    public void showTextBadge(String badgeText) {
        mIsShowDrawable = false;
        mRedPointText = badgeText;
        mIsShowRedPoint = true;
        mRedPointAble.redPointPostInvalidate();
    }

    /**
     * 只设置文字不显示
     * @param badgeText
     * @param hidden
     */
    public void showTextBadge(String badgeText, boolean hidden) {
        mIsShowDrawable = false;
        mRedPointText = badgeText;
        mIsShowRedPoint = !hidden;
        mRedPointAble.redPointPostInvalidate();
    }

    public void hiddenBadge() {
        mIsShowRedPoint = false;
        mRedPointAble.redPointPostInvalidate();
    }

    public boolean isShowRedPoint() {
        return mIsShowRedPoint;
    }

    public void showDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mIsShowDrawable = true;
        mIsShowRedPoint = true;
        mRedPointAble.redPointPostInvalidate();
    }

    public boolean isShowDrawable() {
        return mIsShowDrawable;
    }

    public RectF getRedPointRectF() {
        return mRedPointRectF;
    }

    public int getRedPointPadding() {
        return mRedPointPadding;
    }

    public String getRedPointText() {
        return mRedPointText;
    }

    public int getRedPointBgColor() {
        return mRedPointBgColor;
    }

    public int getRedPointTextColor() {
        return mRedPointTextColor;
    }

    public int getRedPointTextSize() {
        return mRedPointTextSize;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setDragDismissDelegage(RedPointDragDismissDelegate delegage) {
        mDelegage = delegage;
    }

    public View getRootView() {
        return mRedPointAble.getRootView();
    }

    public boolean isResumeTravel() {
        return mIsResumeTravel;
    }

    public enum RedPointGravity {
        RightTop,
        RightCenter,
        RightBottom
    }
}