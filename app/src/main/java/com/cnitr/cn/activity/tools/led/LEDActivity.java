package com.cnitr.cn.activity.tools.led;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.Constant;

import butterknife.Bind;

public class LEDActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.content_led)
    TextInputEditText mContentLed;

    @Bind(R.id.textsize_seekbar_led)
    AppCompatSeekBar mTextSizeSeekbarLed;

    @Bind(R.id.rollspeed_seekbar_led)
    AppCompatSeekBar mRollspeedSeekbarLed;

    @Bind(R.id.start_btn_led)
    AppCompatButton mStartBtnLed;

    @Bind(R.id.spinner_magicstyle_led)
    AppCompatSpinner mCompatSpinner;

    public String mFontColor;
    public int mProgress;
    private int mTextSize;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_led;
    }

    @Override
    protected void init() {
        mCompatSpinner.setSelection(0);
        mTextSizeSeekbarLed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextSize = 100 + progress * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mRollspeedSeekbarLed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mCompatSpinner.setOnItemSelectedListener(this);
        mStartBtnLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShowLed();
            }
        });
    }

    /**
     * 开始显示
     */
    private void startShowLed() {
        String mContentLedText = mContentLed.getText().toString();
        if (TextUtils.isEmpty(mFontColor)) {
            mFontColor = "#FFFFFF";
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constant.LED_CONTENT, mContentLedText);
        bundle.putString(Constant.LED_FONT_COLOR, mFontColor);
        bundle.putInt(Constant.LED_FONT_SIZE, mTextSize);
        bundle.putInt(Constant.LED_ROLL_SPEED, mProgress);

        startActivity(new Intent(this, LEDAutoActivity.class).putExtras(bundle));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mFontColor = "#FFFFFF";
                break;
            case 1:
                mFontColor = "#FF3300";
                break;
            case 2:
                mFontColor = "#FF66CC";
                break;
            case 3:
                mFontColor = "#3366FF";
                break;
            case 4:
                mFontColor = "#FFFF00";
                break;
            case 5:
                mFontColor = "#00CC00";
                break;
            case 6:
                mFontColor = "#CC66FF";
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
