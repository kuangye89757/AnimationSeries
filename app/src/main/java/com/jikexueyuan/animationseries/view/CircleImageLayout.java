package com.jikexueyuan.animationseries.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jikexueyuan.animationseries.R;
import com.jikexueyuan.animationseries.UIUtils;

import java.util.ArrayList;
import java.util.List;


public class CircleImageLayout extends LinearLayout {
    private static final String TAG = "CircleImageLayout";
    private double mAngle = 0;

    //初始角度 
    private int mX, mY;
    //子控件位置
    private int mWidth, mHeight;

    //控件长宽 
    private int mRadius;


    //子控件距离控件圆心位置 
    private int mCount;
    private List<ImageView> mCircleImageViewList;
    private ImageView mTargetImageView;

    public CircleImageLayout(Context context) {
        this(context, null);
    }

    public CircleImageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleImageViewList = new ArrayList<>();
//        init(20, UIUtils.dp2px(20));
        init(12, UIUtils.dp2px(17));
    }

    /**
     * 设置子控件到控件圆心的位置
     */
    public void setRadius(int radius) {
        mRadius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initDraw();
    }

    public void initDraw() {
        mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            View child = getChildAt(i);
            child.getWidth();
            child.getHeight();
            if (i == 0) {
                mX = mWidth / 2;
                mY = mHeight / 2;
            } else {
                 switch (i) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        mAngle = 315.0 - (i - 1) * 18;
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        mAngle = 135.0 - (i - 7) * 18;
                        break;
                    default:
                        break;
                }
//                mAngle = 360.0 - 360.0 - (mCount - i) * 18 + 9;
                
                Log.d(TAG, String.format("第%s个 mAngle = %s", i, mAngle));
                mX = (int) (mWidth / 2 + Math.cos(Math.toRadians(mAngle)) * mRadius);
                mY = (int) (mHeight / 2 - Math.sin(Math.toRadians(mAngle)) * mRadius);
            }
            child.layout(mX - child.getWidth() / 2, mY - child.getHeight() / 2, mX + child.getWidth() / 2, mY + child.getHeight() / 2);
        }
    }


    /**
     * 初始化环绕数量半径
     */
    public void init(int count, int radius) {
        String[] circleImgs = new String[]{
//                "#00ff00","#00ff00","#8AD1DE", "#DEC58A", "#8ADEAD", "#8AAEDE", "#DE8AA0", "#988ADE","#00ff00","#00ff00",
//                "#00ff00","#00ff00","#8AD1DE", "#DEC58A", "#8ADEAD", "#8AAEDE", "#DE8AA0", "#988ADE","#00ff00","#00ff00",
                
                "#00ff00","#00ff00","#8AD1DE", "#DEC58A", "#8ADEAD", "#8AAEDE", "#DE8AA0", "#988ADE",
                "#00ff00","#00ff00","#8AD1DE", "#DEC58A", "#8ADEAD", "#8AAEDE", "#DE8AA0", "#988ADE",
        };

        mRadius = radius;
        for (int i = 0; i < count + 1; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) { //i为0时为圆型头像 
                imageView.setImageResource(R.mipmap.thumb_praise);
                mTargetImageView = imageView;
                addView(imageView);
            } else {
                imageView.setImageResource(R.drawable.shape_circle);
                GradientDrawable mm = (GradientDrawable) imageView.getDrawable();
                mm.setColor(Color.parseColor(circleImgs[i - 1]));
                addView(imageView, UIUtils.dp2px(3), UIUtils.dp2px(3));
                imageView.setVisibility(GONE);
                mCircleImageViewList.add(imageView);
            }
        }
    }

    public void startRightDirct() {

        //图标扩散动画
        ValueAnimator iconBoomValueAnimator = ValueAnimator.ofInt(UIUtils.dp2px(17), UIUtils.dp2px(25));
        iconBoomValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for (ImageView imageView : mCircleImageViewList) {
                    imageView.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                for (ImageView imageView : mCircleImageViewList) {
                    imageView.setVisibility(VISIBLE);
                }
            }
        });
        iconBoomValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e(TAG, animation.getAnimatedValue() + "");
                int radius = (Integer) animation.getAnimatedValue();
                setRadius(radius);
                requestLayout();
            }
        });

        //图标缩小动画
        ValueAnimator iconScaleValueAnimator = ValueAnimator.ofFloat(2.25f,1.0f);
        iconScaleValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                for (ImageView imageView : mCircleImageViewList) {
                    imageView.setScaleX(scale);
                    imageView.setScaleY(scale);
                }
            }
        });

        //图标显隐动画
        ValueAnimator iconAlphaValueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        iconAlphaValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (Float) animation.getAnimatedValue();
                for (ImageView imageView : mCircleImageViewList) {
                    imageView.setAlpha(alpha);
                }
            }
        });


        //拇指旋转动画 (hide)
        PropertyValuesHolder rotationHideVH = PropertyValuesHolder.ofFloat("rotation", 0, -30);
        ObjectAnimator thumbHideRotationAnimator = ObjectAnimator.ofPropertyValuesHolder(mTargetImageView, rotationHideVH);
        thumbHideRotationAnimator.setDuration(50);
        thumbHideRotationAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTargetImageView.setImageResource(R.mipmap.thumb_praise_chk_hide);
            }
        });


        //拇指旋转动画 (show)
        PropertyValuesHolder rotationVH = PropertyValuesHolder.ofFloat("rotation", 0, 30, 15, 0);
        ObjectAnimator thumbShowRotationAnimator = ObjectAnimator.ofPropertyValuesHolder(mTargetImageView, rotationVH);
        thumbShowRotationAnimator.setDuration(200);
        thumbShowRotationAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTargetImageView.setImageResource(R.mipmap.thumb_praise_chk_show);
            }
        });

        //拇指缩放动画
        PropertyValuesHolder scaleXVH = PropertyValuesHolder.ofFloat("scaleX", 0.75f, 1.0f);
        PropertyValuesHolder scaleYVH = PropertyValuesHolder.ofFloat("scaleY", 0.75f, 1.0f);
        ObjectAnimator thumbScaleValueAnimator = ObjectAnimator.ofPropertyValuesHolder(mTargetImageView, scaleXVH, scaleYVH);
        thumbScaleValueAnimator.setDuration(250);
        thumbScaleValueAnimator.setInterpolator(new LinearInterpolator());

        //图标动画集合
        final AnimatorSet iconAnimatorSet = new AnimatorSet();
        iconAnimatorSet.playTogether(iconBoomValueAnimator, iconScaleValueAnimator, iconAlphaValueAnimator);
        iconAnimatorSet.setInterpolator(new DecelerateInterpolator());
        iconAnimatorSet.setDuration(400);

        //拇指动画集合
        AnimatorSet thumbAnimatorSet = new AnimatorSet();
        thumbAnimatorSet.playSequentially(thumbHideRotationAnimator, thumbShowRotationAnimator);
        thumbAnimatorSet.playTogether(thumbScaleValueAnimator, thumbHideRotationAnimator);
        thumbAnimatorSet.setDuration(200).setStartDelay(10);
        thumbAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //图标动画开始
                iconAnimatorSet.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mTargetImageView.setImageResource(R.mipmap.thumb_praise_chk_show);
            }
        });
        thumbAnimatorSet.setInterpolator(new OvershootInterpolator());
        thumbAnimatorSet.start();
    }

}
