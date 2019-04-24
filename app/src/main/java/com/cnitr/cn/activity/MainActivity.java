package com.cnitr.cn.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cnitr.cn.Constant;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.fragment.DiscoveryFragment;
import com.cnitr.cn.activity.fragment.HomeFragment;
import com.cnitr.cn.activity.fragment.MineFragment;
import com.cnitr.cn.activity.tools.ExpressActivity;
import com.cnitr.cn.activity.tools.JokesActivity;
import com.cnitr.cn.activity.tools.MessageActivity;
import com.cnitr.cn.activity.tools.calendar.CalendarActivity;
import com.cnitr.cn.activity.tools.game.GameActivity;
import com.cnitr.cn.activity.tools.led.LEDActivity;
import com.cnitr.cn.activity.tools.weather.WeatherActivity;
import com.cnitr.cn.adapter.SaveStateViewPagerAdapter;
import com.cnitr.cn.entity.UserEntity;
import com.cnitr.cn.entity.WechatItem;
import com.cnitr.cn.service.UserDao;
import com.cnitr.cn.wxapi.WXShareManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * MainActivity
 * Created by YangChen on 2018/7/4.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    private CircleImageView headImage;
    private AppCompatTextView tViewUserName, tViewEmail, tViewSignture;

    private SaveStateViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private MenuItem currentMenuItem;
    private Fragment mHomeFragment, mDiscoveryFragment, mMimeFragment;

    public static final int REQUEST_CODE_HEAD = 101;
    public static final int REQUEST_CODE_SAO = 102;
    private MainBroadcastReceiver mainBroadcastReceiver;

    public static List<WechatItem.ResultBean.ListBean> hotNewsList = new ArrayList<>();

    // BroadcastReceiver
    private class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals(Constant.ACTION_REFRESH_HEADER)) {
                    refreshPersonal();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissions();

        // 初始化导航菜单栏
        initToolbar();

        // 初始化ViewPager
        initViewPager();

        // 注册广播
        mainBroadcastReceiver = new MainBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_REFRESH_HEADER);
        registerReceiver(mainBroadcastReceiver, filter);

        // 打开系统消息
        boolean SYSTEM_MESSAGE = getIntent().getBooleanExtra("SYSTEM_MESSAGE", false);
        if (SYSTEM_MESSAGE) {
            Intent intent = new Intent(this, JPushMessageActivity.class);
            startActivity(intent);
        }

    }


    /**
     * 刷新个人资料
     */
    private void refreshPersonal() {

        // 刷新头像
        refreshHeader();
        UserEntity user = UserDao.getInstance().getUser();
        if (user != null) {
            String username = user.getUsername();
            String email = user.getEmail();
            String signture = user.getSignature();
            tViewUserName.setText(TextUtils.isEmpty(username) ? getString(R.string.app_name) : username);
            tViewEmail.setText(TextUtils.isEmpty(email) ? getString(R.string.email) : email);
            tViewSignture.setText(TextUtils.isEmpty(signture) ? getString(R.string.signature) : signture);
        }
    }

    /**
     * 初始化导航菜单栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView);
        headImage = (CircleImageView) view.findViewById(R.id.headImage);
        tViewUserName = (AppCompatTextView) view.findViewById(R.id.tViewUserName);
        tViewEmail = (AppCompatTextView) view.findViewById(R.id.tViewEmail);
        tViewSignture = (AppCompatTextView) view.findViewById(R.id.tViewSignture);

        // 刷新个人资料
        refreshPersonal();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        fragmentList = new ArrayList<Fragment>();
        mHomeFragment = new HomeFragment();
        mDiscoveryFragment = new DiscoveryFragment();
        mMimeFragment = new MineFragment();

        fragmentList.add(mHomeFragment);
        fragmentList.add(mDiscoveryFragment);
        fragmentList.add(mMimeFragment);

        adapter = new SaveStateViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (currentMenuItem != null) {
                    currentMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                currentMenuItem = bottomNavigationView.getMenu().getItem(position);
                currentMenuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // refreshHeader
    private void refreshHeader() {
        UserEntity entity = UserDao.getInstance().getUser();
        if (entity != null && entity.getHeadimgurl() != null) {
            Glide.with(this).load(Uri.parse(entity.getHeadimgurl())).into(headImage);
        }
    }

    // OnNavigationItemSelectedListener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_news:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.item_mm:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.item_me:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String shareText = getString(R.string.my_share);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (id == R.id.shareFriends) {
            WXShareManager.getInstance().sendWebPageObjectWXShare(WXShareManager.WX_SHARE_SESSION, Constant.APP_DOWNLOAD, shareText, Constant.APP_DOWNLOAD, bitmap);
            return true;
        }
        if (id == R.id.shareMoments) {
            WXShareManager.getInstance().sendWebPageObjectWXShare(WXShareManager.WX_SHARE_TIMELINE, Constant.APP_DOWNLOAD, shareText, Constant.APP_DOWNLOAD, bitmap);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.tool_calendar) {
            // 日历
            intent = new Intent(this, CalendarActivity.class);
        } else if (id == R.id.tool_weather) {
            // 天气
            intent = new Intent(this, WeatherActivity.class);
        } else if (id == R.id.tool_express) {
            // 快递查询
            intent = new Intent(this, ExpressActivity.class);
        } else if (id == R.id.tool_led) {
            // 手机弹幕
            intent = new Intent(this, LEDActivity.class);
        } else if (id == R.id.tool_jokes) {
            // 冷笑话
            intent = new Intent(this, JokesActivity.class);
        } else if (id == R.id.tool_games) {
            // 小游戏
            intent = new Intent(this, GameActivity.class);
        }
        if (intent != null) {
            intent.putExtra(Constant.INTENT_TITLE, item.getTitle());
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_HEAD) {
                if (mMimeFragment != null) {
                    mMimeFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
            if (requestCode == REQUEST_CODE_SAO) {
                if (data != null) {
                    String result = data.getStringExtra(com.cnitr.cn.activity.tools.zxing.common.Constant.CODED_CONTENT);
                    if (!TextUtils.isEmpty(result)) {
                        if (Patterns.WEB_URL.matcher(result).matches()) {
                            Uri uri = Uri.parse(result);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, MessageActivity.class);
                            intent.putExtra(Constant.INTENT_TITLE, getString(R.string.title_scan));
                            intent.putExtra("message", result);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    // 再按一次退出程序
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (secondTime - firstTime < 2000) {
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        if (mainBroadcastReceiver != null) {
            unregisterReceiver(mainBroadcastReceiver);
        }
        super.onDestroy();
    }

    // 运行时权限申请
    private static final String[] EXTERNAL_STORAGE = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int RC_EXTERNAL_STORAGE = 101;

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, EXTERNAL_STORAGE)) {

        } else {
            EasyPermissions.requestPermissions(this, "需要文件系统权限", RC_EXTERNAL_STORAGE, EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.requestPermissions(this, "需要文件系统权限", RC_EXTERNAL_STORAGE, EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
