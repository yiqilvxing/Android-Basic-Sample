package com.cnitr.cn.activity.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cnitr.cn.Constant;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.MainActivity;
import com.cnitr.cn.activity.MyBookmarksActivity;
import com.cnitr.cn.activity.PersonalActivity;
import com.cnitr.cn.activity.SettingsActivity;
import com.cnitr.cn.activity.tools.game.GameActivity;
import com.cnitr.cn.activity.tools.note.NoteListActivity;
import com.cnitr.cn.activity.tools.zxing.android.CaptureActivity;
import com.cnitr.cn.entity.MenuEntity;
import com.cnitr.cn.entity.UserEntity;
import com.cnitr.cn.service.UserDao;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.header.flyrefresh.FlyView;
import com.scwang.smartrefresh.header.flyrefresh.MountainSceneView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 我的
 * Created by YangChen on 2018/7/10.
 */

public class MineFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.mountain)
    MountainSceneView mSceneView;

    @Bind(R.id.flyView)
    FlyView mFlyView;

    @Bind(R.id.flyRefresh)
    FlyRefreshHeader mFlyRefreshHeader;

    @Bind(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.appbar)
    AppBarLayout appbar;

    @Bind(R.id.recyclerView)
    RecyclerView mListView;

    @Bind(R.id.toolbarLayout)
    CollapsingToolbarLayout mToolbarLayout;

    @Bind(R.id.fab)
    FloatingActionButton mActionButton;

    @Bind(R.id.headImage)
    CircleImageView headImage;

    @Bind(R.id.tViewSetHeader)
    AppCompatTextView tViewSetHeader;

    private MineAdapter mineAdapter;
    private ArrayList<MenuEntity> menuData = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    private static final int[] MENU_ICON = {R.mipmap.icon_me_data, R.mipmap.icon_me_bookmark, R.mipmap.icon_me_note, R.mipmap.icon_me_sao, R.mipmap.icon_me_game, R.mipmap.icon_me_setting};

    private boolean isScan;

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        initHeaderView();
        initRefeshView();
    }

    /**
     * 初始化头像
     */
    private void initHeaderView() {
        refreshHeader();
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScan = false;
                checkPermissions();
            }
        });

        tViewSetHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScan = false;
                checkPermissions();
            }
        });
    }

    public void refreshHeader() {
        UserEntity entity = UserDao.getInstance().getUser();
        if (entity != null && entity.getHeadimgurl() != null) {
            Glide.with(getActivity()).load(Uri.parse(entity.getHeadimgurl())).into(headImage);
            tViewSetHeader.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化下拉视图
     */
    private void initRefeshView() {
        String[] MENU_NAME = getResources().getStringArray(R.array.arrays_mine);
        for (int i = 0; i < MENU_NAME.length; i++) {
            MenuEntity item = new MenuEntity();
            item.setTitle(MENU_NAME[i]);
            item.setIcon(MENU_ICON[i]);
            menuData.add(item);
        }

        mFlyRefreshHeader.setUp(mSceneView, mFlyView);//绑定场景和纸飞机
        mRefreshLayout.setReboundInterpolator(new ElasticOutInterpolator());//设置回弹插值器，会带有弹簧震动效果
        mRefreshLayout.setReboundDuration(800);//设置回弹动画时长
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                View child = mListView.getChildAt(0);
                if (child != null) {
                    //开始刷新的时候个第一个item设置动画效果
                    bounceAnimateView(child.findViewById(R.id.icon));
                }
                mRefreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //通知刷新完成，这里改为通知Header，让纸飞机飞回来
                        mFlyRefreshHeader.finishRefresh(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                //在纸飞机回到原位之后添加数据效果更真实
                                refreshHeader();
                            }
                        });
                    }
                }, 2000);//模拟两秒的后台数据加载
            }
        });
        //设置 让 AppBarLayout 和 RefreshLayout 的滚动同步 并不保持 toolbar 位置不变
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                appbar.setTranslationY(offset);
            }
        });

        mineAdapter = new MineAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mListView.setAdapter(mineAdapter);
        /*
         * 设置点击 ActionButton 时候触发自动刷新 并改变主题颜色
         */
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshLayout.autoRefresh();
            }
        });
        /*
         * 监听 AppBarLayout 的关闭和开启 给 FlyView（纸飞机） 和 ActionButton 设置关闭隐藏动画
         */
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean misAppbarExpand = true;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                if (fraction < 0.1 && misAppbarExpand && getUserVisibleHint()) {
                    misAppbarExpand = false;
                    mActionButton.animate().scaleX(0).scaleY(0);
                    mFlyView.animate().scaleX(0).scaleY(0);
                    ValueAnimator animator = ValueAnimator.ofInt(mListView.getPaddingTop(), 0);
                    animator.setDuration(300);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mListView.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
                        }
                    });
                    animator.start();
                }
                if (fraction > 0.8 && !misAppbarExpand && getUserVisibleHint()) {
                    misAppbarExpand = true;
                    mActionButton.animate().scaleX(1).scaleY(1);
                    mFlyView.animate().scaleX(1).scaleY(1);
                    ValueAnimator animator = ValueAnimator.ofInt(mListView.getPaddingTop(), DensityUtil.dp2px(25));
                    animator.setDuration(300);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mListView.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
                        }
                    });
                    animator.start();
                }
            }
        });
    }

    /**
     * 动画效果
     */
    private void bounceAnimateView(final View view) {
        if (view == null) {
            return;
        }
        ValueAnimator swing = ValueAnimator.ofFloat(0, 60, -40, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setRotationX((float) animation.getAnimatedValue());
            }
        });
        swing.start();
    }

    /**
     * ElasticOutInterpolator
     */
    public class ElasticOutInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float t) {
            if (t == 0) return 0;
            if (t >= 1) return 1;
            float p = .3f;
            float s = p / 4;
            return ((float) Math.pow(2, -10 * t) * (float) Math.sin((t - s) * (2 * (float) Math.PI) / p) + 1);
        }
    }

    /**
     * MineAdapter
     */
    private class MineAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private LayoutInflater mInflater;

        MineAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.item_mine, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, final int i) {
            final MenuEntity menu = menuData.get(i);
            itemViewHolder.icon.setImageResource(menu.getIcon());
            itemViewHolder.title.setText(menu.getTitle());
            itemViewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMenuOnClickLinstener(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuData.size();
        }
    }

    /**
     * ItemViewHolder
     */
    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView icon;
        AppCompatTextView title;
        LinearLayout layoutItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            icon = (AppCompatImageView) itemView.findViewById(R.id.icon);
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.layoutItem);
        }

    }

    /**
     * 设置菜单列表监听器
     */
    private void setMenuOnClickLinstener(int position) {
        MenuEntity entity = menuData.get(position);
        String title = entity.getTitle();
        Intent intent = null;
        switch (position) {
            case 0:// 个人资料
                intent = new Intent(getActivity(), PersonalActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, title);
                break;
            case 1:// 我的书签
                intent = new Intent(getActivity(), MyBookmarksActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, title);
                break;
            case 2:// 备忘录
                intent = new Intent(getActivity(), NoteListActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, title);
                break;
            case 3:// 扫一扫
                isScan = true;
                checkPermissions();
                break;
            case 4:// 小游戏
                intent = new Intent(getActivity(), GameActivity.class);
                break;
            case 5:// 设置
                intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, title);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == MainActivity.REQUEST_CODE_HEAD) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            if (mSelected != null && !mSelected.isEmpty()) {
                if (headImage != null) {
                    Glide.with(getActivity()).load(mSelected.get(0)).into(headImage);
                    UserEntity entity = UserDao.getInstance().getUser();
                    if (entity == null) {
                        entity = new UserEntity();
                    }
                    entity.setHeadimgurl(mSelected.get(0).toString());
                    UserDao.getInstance().saveUser(entity);
                    Intent intent = new Intent(Constant.ACTION_REFRESH_HEADER);
                    getActivity().sendBroadcast(intent);
                    tViewSetHeader.setVisibility(View.GONE);
                }
            }
        }
    }

    // selectPhotos
    private void selectPhotos() {
        Matisse.from(getActivity())
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.cnitr.cn.fileprovider"))
                .theme(R.style.Matisse_Zhihu)
                .maxSelectable(1) // 图片选择的最多数量
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(MainActivity.REQUEST_CODE_HEAD);
    }

    /**
     * 扫一扫
     */
    private void startCaptureActivity() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, MainActivity.REQUEST_CODE_SAO);
    }


    // 运行时权限申请
    private static final String[] CALL_CAMERA = {Manifest.permission.CAMERA};
    private static final int RC_CALL_CAMERA = 101;

    @AfterPermissionGranted(RC_CALL_CAMERA)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(getActivity(), CALL_CAMERA)) {
            if (isScan) {
                startCaptureActivity();
            } else {
                selectPhotos();
            }
        } else {
            EasyPermissions.requestPermissions(this, "需要访问拍照权限", RC_CALL_CAMERA, CALL_CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.requestPermissions(this, "需要访问拍照权限", RC_CALL_CAMERA, CALL_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RC_CALL_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isScan) {
                    startCaptureActivity();
                } else {
                    selectPhotos();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
