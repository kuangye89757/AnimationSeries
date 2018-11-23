package com.jikexueyuan.animationseries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jikexueyuan.animationseries.view.CircleImageLayout;

public class PraiseActivity extends AppCompatActivity {

    private CircleImageLayout mCircleImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);
        mCircleImageLayout = (CircleImageLayout) findViewById(R.id.cil);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleImageLayout.startRightDirct();
            }
        });
    }
}
