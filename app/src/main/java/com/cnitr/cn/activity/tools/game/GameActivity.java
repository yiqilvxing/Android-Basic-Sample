package com.cnitr.cn.activity.tools.game;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;

import butterknife.Bind;

public class GameActivity extends BaseActivity {

    @Bind(R.id.gameView)
    GameView gameView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_game;
    }

    @Override
    protected void init() {
        int[] bitmapIds = {
                R.drawable.plane,
                R.drawable.explosion,
                R.drawable.yellow_bullet,
                R.drawable.blue_bullet,
                R.drawable.small,
                R.drawable.middle,
                R.drawable.big,
                R.drawable.bomb_award,
                R.drawable.bullet_award,
                R.drawable.pause1,
                R.drawable.pause2,
                R.drawable.bomb
        };
        gameView.start(bitmapIds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView != null) {
            gameView.destroy();
        }
        gameView = null;
    }
}