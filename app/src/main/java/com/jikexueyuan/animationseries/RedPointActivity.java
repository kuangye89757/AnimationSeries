package com.jikexueyuan.animationseries;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jikexueyuan.animationseries.redpoint.RedPointTextView;
import com.jikexueyuan.animationseries.redpoint.RedPointViewHelper;

public class RedPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_point);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llll);

        //TextView--红点数字
        RedPointTextView redPointTextView = new RedPointTextView(this);
//        redPointTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        redPointTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        redPointTextView.setBackgroundColor(Color.parseColor("#037BFF"));
        redPointTextView.setText("代码红点");
        redPointTextView.setTextColor(Color.parseColor("#1e1e1e"));
        redPointTextView.setTextSize(16);
        redPointTextView.setId(redPointTextView.hashCode());
        redPointTextView.setPadding(UIUtils.dp2px(15),UIUtils.dp2px(15), UIUtils.dp2px(15),UIUtils.dp2px(15));
        redPointTextView.setGravity(Gravity.CENTER);
        RedPointViewHelper viewHelper = redPointTextView.getRedPointViewHelper();
        viewHelper.setBadgePaddingDp(4);
        viewHelper.setRedPointGravity(RedPointViewHelper.RedPointGravity.RightTop);
        linearLayout.addView(redPointTextView);
        viewHelper.showTextBadge("10");
    }
}
