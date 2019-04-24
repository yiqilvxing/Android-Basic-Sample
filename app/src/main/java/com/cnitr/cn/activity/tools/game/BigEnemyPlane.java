package com.cnitr.cn.activity.tools.game;

import android.graphics.Bitmap;

/**
 * 大敌机类，体积大，抗打击能力强
 */
public class BigEnemyPlane extends EnemyPlane {

    public BigEnemyPlane(Bitmap bitmap){
        super(bitmap);
        setPower(8);//大敌机抗抵抗能力为6，即需要6颗子弹才能销毁大敌机
        setValue(10000);//销毁一个大敌机可以得30000分
    }

}