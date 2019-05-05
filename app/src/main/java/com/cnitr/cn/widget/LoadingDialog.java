package com.cnitr.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cnitr.cn.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by YangChen on 2018/12/4.
 */

public class LoadingDialog extends Dialog {

    // Loading加载动画效果

    // 默认效果BallSpinFadeLoaderIndicator
    public static final String BallPulseIndicator = "BallPulseIndicator";
    public static final String BallGridPulseIndicator = "BallGridPulseIndicator";
    public static final String BallClipRotateIndicator = "BallClipRotateIndicator";
    public static final String BallClipRotatePulseIndicator = "BallClipRotatePulseIndicator";

    public static final String SquareSpinIndicator = "SquareSpinIndicator";
    public static final String BallClipRotateMultipleIndicator = "BallClipRotateMultipleIndicator";
    public static final String BallPulseRiseIndicator = "BallPulseRiseIndicator";
    public static final String BallRotateIndicator = "BallRotateIndicator";

    public static final String CubeTransitionIndicator = "CubeTransitionIndicator";
    public static final String BallZigZagIndicator = "BallZigZagIndicator";
    public static final String BallZigZagDeflectIndicator = "BallZigZagDeflectIndicator";
    public static final String BallTrianglePathIndicator = "BallTrianglePathIndicator";

    public static final String BallScaleIndicator = "BallScaleIndicator";
    public static final String LineScaleIndicator = "LineScaleIndicator";
    public static final String LineScalePartyIndicator = "LineScalePartyIndicator";
    public static final String BallScaleMultipleIndicator = "BallScaleMultipleIndicator";

    public static final String BallPulseSyncIndicator = "BallPulseSyncIndicator";
    public static final String BallBeatIndicator = "BallBeatIndicator";
    public static final String LineScalePulseOutIndicator = "LineScalePulseOutIndicator";
    public static final String LineScalePulseOutRapidIndicator = "LineScalePulseOutRapidIndicator";

    public static final String BallScaleRippleIndicator = "BallScaleRippleIndicator";
    public static final String BallScaleRippleMultipleIndicator = "BallScaleRippleMultipleIndicator";
    public static final String BallSpinFadeLoaderIndicator = "BallSpinFadeLoaderIndicator";
    public static final String LineSpinFadeLoaderIndicator = "LineSpinFadeLoaderIndicator";

    public static final String TriangleSkewSpinIndicator = "TriangleSkewSpinIndicator";
    public static final String PacmanIndicator = "PacmanIndicator";
    public static final String BallGridBeatIndicator = "BallGridBeatIndicator";
    public static final String SemiCircleSpinIndicator = "SemiCircleSpinIndicator";

    private TextView title;
    private AVLoadingIndicatorView avLoading;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        init(context);
    }

    private void init(Context context) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        title = (TextView) view.findViewById(R.id.title);
        avLoading = (AVLoadingIndicatorView) view.findViewById(R.id.avLoading);
        setContentView(view);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (avLoading != null) {
                    avLoading.hide();
                }
            }
        });
    }

    public LoadingDialog setLoadingText(String loadingText) {
        if (loadingText != null && loadingText.length() < 10) {
            title.setText(loadingText == null ? "加载中..." : loadingText);
        }
        return this;
    }

    public LoadingDialog setLoadingAnimation(String indicatorName) {
        if (indicatorName != null) {
            avLoading.setIndicator(indicatorName);
        }
        return this;
    }

    @Override
    public void show() {
        super.show();
    }


}
