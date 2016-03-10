package com.jikexueyuan.animationseries;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jikexueyuan.animationseries.view.Rotate3dAnimation;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll;
    private Button button1;
    private Button button2;
    private Button button4;
    private ListView listView;
    private int[] colors = {
      Color.RED,
      Color.YELLOW,
      Color.GREEN,
      Color.BLUE,
      Color.LTGRAY,
      Color.CYAN,
      Color.MAGENTA,
      Color.BLACK,

    };
    private Button button5;
    private Button button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.all_anim);
        button1.startAnimation(animation);

        button2 = (Button) findViewById(R.id.button2);
        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0.0f,180f,200f,200f,false);
        rotate3dAnimation.setDuration(5000);
        rotate3dAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotate3dAnimation.setRepeatMode(ValueAnimator.REVERSE);
        button2.startAnimation(rotate3dAnimation);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,CountDownActivity.class);
                startActivity(intent);
            }
        });


        /**代码实现*/
        listView = (ListView) findViewById(R.id.list);
        Animation listAnimation = AnimationUtils.loadAnimation(this,R.anim.anim_item);
        LayoutAnimationController controller = new LayoutAnimationController(listAnimation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);


        /**转场动画*/
        ((Button)findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                if(version > 5){
                    MainActivity.this.overridePendingTransition(R.anim.move_in_up, R.anim.move_out_up);
                }
            }
        });


        /**属性动画代码实现  推荐使用代码因为属性的起始值无法提前确定*/
        //改变Activity背景属性
        ll = (LinearLayout) findViewById(R.id.ll);
        ValueAnimator colorAnim =  ObjectAnimator.ofInt(ll,"backgroundColor", colors);
        colorAnim.setDuration(13000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

        //5秒内对View旋转平移,缩放和透明改变
        button4 = (Button) findViewById(R.id.button4);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(button4,"rotationX",0,360),
                ObjectAnimator.ofFloat(button4,"rotationY",0,180),
                ObjectAnimator.ofFloat(button4,"rotation",0,-90),
                ObjectAnimator.ofFloat(button4,"translationX",0,90),
                ObjectAnimator.ofFloat(button4,"translationY",0,90),
                ObjectAnimator.ofFloat(button4,"scaleX",1,1.5f),
                ObjectAnimator.ofFloat(button4,"scaleY",1,1.5f),
                ObjectAnimator.ofFloat(button4,"alpha",1,0.25f,1)
        );
        set.setDuration(5000).start();


        /**使用XML的属性动画*/
        button5 = (Button) findViewById(R.id.button5);
        AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.property_animator);
        set1.setTarget(button5);
        set1.start();


        /**改变Button的宽度*/
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewWrapper wrapper = new ViewWrapper(button6);
                ObjectAnimator.ofInt(wrapper,"width",500).setDuration(5000).start();
            }
        });


    }

    private static class ViewWrapper{
        private View mTarget;

        public ViewWrapper(View mTarget) {
            this.mTarget = mTarget;
        }

        /**提供get and set*/
        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}
