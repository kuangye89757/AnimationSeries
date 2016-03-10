package com.jikexueyuan.animationseries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.move_in_down, R.anim.move_out_down);
    }
}
