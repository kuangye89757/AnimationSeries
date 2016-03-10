详见G:\android_studio_jikexueyuan\AnimationSeries
================================================================================================================
	第6章 Android的动画深入分析
		* View动画的作用对象是View,支持4种动画效果：
			平移	TranslateAnimation	<translate>
			缩放	ScaleAnimation		<scale>
			旋转	RotateAnimation		<rotate>	
			透明度	AlphaAnimation		<alpha>
		* 避免使用帧动画,容易引起OOM,必须使用时需要尺寸较小图片
		* 转场动画LayoutAnimation作用于ViewGroup
			
============================================================================================================================================================			
		一、XML文件 res/anim/xxx.xml

			<?xml version="1.0" encoding="utf-8"?>
			<set xmlns:android="http://schemas.android.com/apk/res/android"
				 android:duration="2000"										--持续时间
				 android:fillAfter="true"										--动画结束位置 true停留 false不停留
				 android:interpolator="@android:anim/linear_interpolator"		--插值器:影响动画速度	
				 android:shareInterpolator="true">								--集合中的动画是否共享一个插值器

				<alpha
					android:fromAlpha="0.0"										--透明度起始值
					android:toAlpha="1.0"/>										--透明度结束值

				<scale
					android:fromXScale="0.5"									--水平缩放起始位
					android:fromYScale="0.5"									--竖直缩放起始位
					android:pivotX="50%"										--缩放轴心坐标X
					android:pivotY="50%"										--缩放轴心坐标Y
					android:toXScale="1.2"										--水平缩放结束位
					android:toYScale="1.2"/>									--竖直缩放结束位

				<translate
					android:fromXDelta="0"										--X起始值
					android:fromYDelta="0"										--Y起始值
					android:toXDelta="100"										--X结束值		
					android:toYDelta="200"/>									--Y结束值

				<rotate
					android:fromDegrees="0"										--旋转开始角度
					android:pivotX="50%"										--旋转轴心坐标X
					android:pivotY="50%"										--旋转轴心坐标Y
					android:toDegrees="180"/>									--旋转结束角度
			</set>
		
============================================================================================================================================================		
		二、转场动画LayoutAnimation的用法
				①定义LayoutAnimation动画 
					res/anim/anim_layout.xml  
				
				<?xml version="1.0" encoding="utf-8"?>
				<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"
								 android:animation="@anim/anim_item"							--子元素指定具体的入场动画
								 android:animationOrder="normal"								--子元素动画顺序
								 android:delay="0.5"											--每个item子元素入场延时是item子元素时长的0.5倍 这里是150ms
					/>

				②子元素指定具体的入场动画
					res/anim/anim_item.xml
					
				<?xml version="1.0" encoding="utf-8"?>
				<set xmlns:android="http://schemas.android.com/apk/res/android"
					 android:duration="300"														--子元素时长
					 android:interpolator="@android:anim/accelerate_interpolator"				--子元素插值器
					 android:shareInterpolator="true"
					>

					<alpha
						android:fromAlpha="0.0"
						android:toAlpha="1.0"/>
					<translate
						android:fromXDelta="500"
						android:toXDelta="0"/>

				</set>	
				
				③在listView中使用(XML形式)
					<ListView
						android:id="@+id/list"
						android:layout_width="match_parent"
						android:layout_height="wrap_content"
						android:background="#fff4f7f9"
						android:cacheColorHint="#00000000"
						android:divider="#dddbdb"
						android:dividerHeight="1dp"
						android:entries="@array/decorate"
						android:layoutAnimation="@anim/anim_layout"
						android:listSelector="@android:color/transparent"
					/>
					
					或代码形式
					 
					listView = (ListView) findViewById(R.id.list);
					Animation listAnimation = AnimationUtils.loadAnimation(this,R.anim.anim_item);
					LayoutAnimationController controller = new LayoutAnimationController(listAnimation);
					controller.setDelay(0.5f);
					controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
					listView.setLayoutAnimation(controller);
============================================================================================================================================================					
		三、Actvity的切换效果 (Fragment可以通过FragmentTransaction.setCustomAnimations()来添加切换动画)
		
				/**启动一个Activity时*/
				int version = Integer.valueOf(android.os.Build.VERSION.SDK_INT); 
				Intent intent = new Intent();
				intent.setClass(getActivity(), ReleaseIntentActivity.class);
				startActivity(intent);
				if(version > 5){
					getActivity().overridePendingTransition(R.anim.move_in_up, R.anim.move_out_up);
				}
				
				res/anim/move_in_up.xml  (Y值从底到上 入场动画)
				
					<?xml version="1.0" encoding="utf-8"?>
					<translate xmlns:android="http://schemas.android.com/apk/res/android"
						android:duration="500"
						android:fromYDelta="100%p"
						android:interpolator="@android:anim/accelerate_interpolator"
						android:toYDelta="0" >

					</translate>
					
				res/anim/move_out_up.xml (Y值从上到屏幕外  出场动画)	
				
					<?xml version="1.0" encoding="utf-8"?>
					<translate xmlns:android="http://schemas.android.com/apk/res/android"
						android:duration="500"
						android:fromYDelta="0"
						android:interpolator="@android:anim/accelerate_interpolator"
						android:toYDelta="-100%p" >

					</translate>
					
				res/anim/move_in_down.xml  (Y值从屏幕外到上 入场动画)
				
					<?xml version="1.0" encoding="utf-8"?>
					<translate xmlns:android="http://schemas.android.com/apk/res/android"
						android:duration="500"
						android:fromYDelta="-100%p"
						android:interpolator="@android:anim/accelerate_interpolator"
						android:toYDelta="0" >

					</translate>
					
				res/anim/move_out_down.xml (Y值从上到底  出场动画)	
				
					<?xml version="1.0" encoding="utf-8"?>
					<translate xmlns:android="http://schemas.android.com/apk/res/android"
						android:duration="500"
						android:fromYDelta="0"
						android:interpolator="@android:anim/accelerate_interpolator"
						android:toYDelta="100%p" >

					</translate>	
					
					
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
						
					
				/**退出一个Activity时*/	
				/**
				 *  在你的activity动作完成的时候，或者Activity需要关闭的时候，调用此方法。
					当你调用此方法的时候，系统只是将最上面的Activity移出了栈，并没有及时的调用onDestory（）方法
					finish会调用到onDestroy方法
				 */
				@Override
				public void finish() {
					super.finish();
					overridePendingTransition(R.anim.move_in_down, R.anim.move_out_down);
				}
============================================================================================================================================================				
		四、属性动画
			* 可以对任意对象的属性进行动画而不仅仅是View,默认时间间隔300ms
			* 属性动画几乎无所不能,只要对象有这个属性就能实现动画效果,建议使用兼容包Nineoldandroids,通过代理View动画实现
			
			
			/**属性动画代码实现 */
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
			res/animator/property_animator.xml	
				
			<?xml version="1.0" encoding="utf-8"?>
			<set xmlns:android="http://schemas.android.com/apk/res/android"
				android:ordering="together">

				<!--x属性300ms内到200-->
				<objectAnimator
					android:propertyName="x"
					android:duration="3000"
					android:valueTo="200"
					android:valueType="intType"
					/>

				<!--y属性300ms内到300-->
				<objectAnimator
					android:propertyName="y"
					android:duration="3000"
					android:valueTo="300"
					android:valueType="intType"
					/>

			</set>
			
			/**使用XML的属性动画*/
			button5 = (Button) findViewById(R.id.button5);
			AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.property_animator);
			set1.setTarget(button5);
			set1.start();
			
============================================================================================================================================================				
		五、插值器 -- 根据时间流逝的百分比来计算当前属性值改变的百分比
				系统预设： LinearInterpolator(匀速动画)
						   AccelerateDecelerateInterpolator(动画左右快中间慢)
						   
						   
			估值器 -- 根据时间流逝的百分比来计算改变后的属性值
				系统预设:  TypeEvaluator
								IntEvaluator(针对整型属性)		FloatEvaluator(针对浮点型属性) 			ArgbEvaluator(针对Color属性)
														
			自定义插值器需要实现TimeInterpolator
			自定义估值器需要实现TypeEvaluator
			
			
			public class IntEvaluator implements TypeEvaluator<Integer> {
				/**估值小数,开始值,结束值*/
				public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
					int startInt = startValue;
					return (int)(startInt + fraction * (endValue - startInt));
				}
			}
			
============================================================================================================================================================				
		六、对任意属性做动画
				对Object的属性xxx做动画,如果要其生效需要满足两个条件:
					①object必须提供setXXX方法,用来设置数值
									getXXX方法,使得系统去取xxx属性(否则直接crash)
					②object中的xxx要通过某种形式或方法反映出来(否则动画无效果)

				※为什么对Button的width属性做动画没有效果?
					由于Button继承自TextView中,提供的width的set方法不是改变视图大小;
					
				解决方案:
					1. 如果有权限的话,给你的属性添加get和set方法,并使得该属性有所体现
					2. 用一个类来包装原始对象,间接提供get和set方法