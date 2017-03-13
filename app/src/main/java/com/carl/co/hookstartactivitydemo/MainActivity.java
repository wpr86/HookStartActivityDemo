package com.carl.co.hookstartactivitydemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        HookHelper.hookAMS();
        HookHelper.hookPMS(this);
        HookHelper.hookHandlerCallback();
    }

    @OnClick(R.id.button)
    public void onClick() {
        Intent intent = new Intent(MainActivity.this,TargetActivity.class);
        startActivity(intent);
    }
}
