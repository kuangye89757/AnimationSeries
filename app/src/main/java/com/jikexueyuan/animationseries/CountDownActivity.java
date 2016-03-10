 package com.jikexueyuan.animationseries;

 import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

 /**
  * 自定义动画
  */
 public class CountDownActivity extends AppCompatActivity {

     private static final String TAG = "CountDownActivity";
     private ViewFlipper min_vf;// 分针

     private ViewFlipper sec_vf;// 秒针
     private Timer secTimer;//秒针倒数计时器

     private ViewFlipper agent_vf;// 经纪人个数
     private Timer agentTimer;//经纪人计时器

     private ImageView iv_releasing;

     private Button intent_btn;

     private String yxid = "";
     private Random countRandom;//倒计时随机数
     private int countResult = 0;//生成的120~175以内随机数
     private int secCount = 0;// 秒针
     private int minCount = 0;// 分针
     private int agentCount = 1;// 经纪人个数

     private static final int ANIMATION_EACH_OFFSET = 600; // 雷达的每个动画的播放时间间隔
     private static final int ANIMATION_VIEW_FLIPPER = 1000; // ViewFlipper的播放时间间隔
     private static final int ANIMATION_AGENT_VIEW_FLIPPER = 500; // 经纪人的播放时间间隔

     private AnimationSet extendSet, extendSet2, extendSet3;
     private AnimationSet shrinkSet, shrinkSet2, shrinkSet3;
     private ImageView wave1, wave2, wave3;
     private Timer timer;
     private Random random;//经纪人轮播随机数
     private int result = 0;//生成的0~5随机数

     private Handler handler = new Handler() {

         @Override
         public void handleMessage(Message msg) {
             if (msg.what == 0x002) {
                 wave2.setVisibility(View.VISIBLE);
                 wave2.startAnimation(extendSet2);
             } else if (msg.what == 0x003) {
                 wave3.setVisibility(View.VISIBLE);
                 wave3.startAnimation(extendSet3);
             } else if (msg.what == 0x004) {
                 wave3.startAnimation(shrinkSet3);
                 wave3.setVisibility(View.GONE);
             } else if (msg.what == 0x005) {
                 wave2.startAnimation(shrinkSet2);
                 wave2.setVisibility(View.GONE);
             } else if (msg.what == 0x006) {
                 wave1.startAnimation(shrinkSet);
             } else if (msg.what == 0x007) {
                 resetAnimation();
             } else if (msg.what == 0x1000) {
                 countUp();//正计时
             } else if (msg.what == 0x1001) {
                 //经纪人数量轮播
                 agent_vf.showNext();
                 TextView agentCurrentTV = (TextView)agent_vf.getCurrentView();
                 agentCurrentTV.setText(agentCount+"");

                 //添加0~5随机数
                 random = new Random();
                 result = random.nextInt(5);
                 agentCount = agentCount+result;
             }
             super.handleMessage(msg);
         }

     };

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_count_down);

		 initView();

         intent_btn.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 goToReceiveTender();//跳转到该意向的标书
             }
         });

         countRandom = new Random();
         countResult = countRandom.nextInt(55) + 120;

         extendSet = getExtendAnimationSet();
         extendSet2 = getExtendAnimationSet();
         extendSet3 = getExtendAnimationSet();

         shrinkSet = getShrinkAnimationSet();
         shrinkSet2 = getShrinkAnimationSet();
         shrinkSet3 = getShrinkAnimationSet();

         /**波纹动画*/
         showWaveAnimation();

         /**波纹定时器*/
         timer = new Timer(true);
         timer.schedule(new TimerTask() {

             @Override
             public void run() {
                 Message message = handler.obtainMessage();
                 message.what = 0x007;
                 handler.sendMessage(message);
             }
         }, ANIMATION_EACH_OFFSET * 9, ANIMATION_EACH_OFFSET * 9);

         /**logo轮播动画*/
         final AnimationDrawable releasingAnimation = (AnimationDrawable) iv_releasing.getBackground();
         iv_releasing.post(new Runnable() {
             @Override
             public void run() {
                 releasingAnimation.start();
             }
         });

         /**倒计时定时器*/
         secTimer = new Timer(true);
         secTimer.schedule(new TimerTask() {

             @Override
             public void run() {
                 Message message = handler.obtainMessage();
                 message.what = 0x1000;
                 handler.sendMessage(message);
             }
         }, 0, ANIMATION_VIEW_FLIPPER);


         /**经纪人定时器*/
         agentTimer = new Timer(true);
         agentTimer.schedule(new TimerTask() {

             @Override
             public void run() {
                 Message message = handler.obtainMessage();
                 message.what = 0x1001;
                 handler.sendMessage(message);
             }
         }, 0, ANIMATION_AGENT_VIEW_FLIPPER);


     }

	 private void initView() {
		 min_vf = (ViewFlipper) findViewById(R.id.min_vf);
		 sec_vf = (ViewFlipper) findViewById(R.id.sec_vf);
		 agent_vf = (ViewFlipper) findViewById(R.id.agent_vf);
		 iv_releasing = (ImageView) findViewById(R.id.iv_releasing);
		 intent_btn = (Button) findViewById(R.id.intent_btn);
		 wave1 = (ImageView) findViewById(R.id.wave1);
		 wave2 = (ImageView) findViewById(R.id.wave2);
		 wave3 = (ImageView) findViewById(R.id.wave3);
	 }

	 /**
      * 波纹动画重置
      */
     private void resetAnimation() {
         cancalWaveAnimation();
         showWaveAnimation();
     }

     /**
      * 波纹扩展动画设置
      * @return
      */
     private AnimationSet getExtendAnimationSet() {
         AnimationSet animationSet = new AnimationSet(true);

         ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                 ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                 ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

         AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1f);

         animationSet.setDuration(ANIMATION_EACH_OFFSET * 3);
         animationSet.addAnimation(scaleAnimation);
         animationSet.addAnimation(alphaAnimation);
         return animationSet;
     }

     /**
      * 波纹收缩动画设置
      * @return
      */
     private AnimationSet getShrinkAnimationSet() {
         AnimationSet animationSet = new AnimationSet(true);

         ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f,
                 ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                 ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

         AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.1f);

         animationSet.setDuration(ANIMATION_EACH_OFFSET * 3);
         animationSet.addAnimation(scaleAnimation);
         animationSet.addAnimation(alphaAnimation);
         return animationSet;
     }

     /**
      * 波纹动画执行
      */
     private void showWaveAnimation() {
         wave1.startAnimation(extendSet);//启动wave1
         handler.sendEmptyMessageDelayed(0x002, ANIMATION_EACH_OFFSET * 1);//延时一个单位再启动wave2
         handler.sendEmptyMessageDelayed(0x003, ANIMATION_EACH_OFFSET * 2);//延时一个单位再启动wave3
         handler.sendEmptyMessageDelayed(0x004, ANIMATION_EACH_OFFSET * 5);//等待wave3扩大到最大后,即5个单位时启动收缩wave3
         handler.sendEmptyMessageDelayed(0x005, ANIMATION_EACH_OFFSET * 6);//延时一个单位再启动收缩wave2
         handler.sendEmptyMessageDelayed(0x006, ANIMATION_EACH_OFFSET * 7);//延时一个单位再启动收缩wave1
     }

     /**
      * 清除雷达动画
      */
     private void cancalWaveAnimation() {
         wave1.clearAnimation();
         wave2.clearAnimation();
         wave3.clearAnimation();
     }

     /**
      * 正计时
      */
     private void countUp() {

         // 根据随机时间停止计时页面
         countResult--;
         if(countResult == 0){
             goToReceiveTender();//跳转到该意向的标书
             return;
         }

         sec_vf.showNext();//显示下一个View
         TextView secCurrentTV = (TextView)sec_vf.getCurrentView();
         secCount++;


         if (secCount >= 10) {
             if(secCount != 60){
                 secCurrentTV.setText("" + secCount);
             }
             if (secCount == 60) {//当秒针是60是分针加1
                 if (minCount != 3) {
                     min_vf.showNext();//显示下一个View
                     minCount++;
                 }else{
                     return;//最长3分钟结束
                 }
                 secCount = 0;
                 secCurrentTV.setText("0" + secCount);
             }
         } else {
             secCurrentTV.setText("0" + secCount);
         }

     }

     private void goToReceiveTender() {
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
         finish();
     }



     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
             return false;//禁用
         } else {
             return super.onKeyDown(keyCode, event);
         }
     }

 }
