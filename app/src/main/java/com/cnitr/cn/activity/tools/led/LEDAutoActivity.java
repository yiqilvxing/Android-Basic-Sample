package com.cnitr.cn.activity.tools.led;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.cnitr.cn.R;
import com.cnitr.cn.Constant;
import com.cnitr.cn.widget.ScrollTextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LEDAutoActivity extends AppCompatActivity {

    private ScrollTextView mContentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledauto);
        init();
    }

    protected void init() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String ledContent = extras.getString(Constant.LED_CONTENT);
        String ledFontcolor = extras.getString(Constant.LED_FONT_COLOR);
        int ledFontsize = extras.getInt(Constant.LED_FONT_SIZE);
        int ledRollspeed = extras.getInt(Constant.LED_ROLL_SPEED);

        mContentView = (ScrollTextView) findViewById(R.id.fullscreen_content);
        if (!TextUtils.isEmpty(ledContent))
            mContentView.setText(ledContent);
        if (!TextUtils.isEmpty(ledFontcolor)) {
            mContentView.setTextColor(Color.parseColor(ledFontcolor));
        }
        if (ledFontsize != 0) {
            mContentView.setTextSize(ledFontsize);
        }
        if (ledRollspeed != 0) {
            mContentView.setSpeed(ledRollspeed);
        }
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContentView != null) {

        }
    }

}
