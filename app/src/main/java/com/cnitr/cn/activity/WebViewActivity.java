package com.cnitr.cn.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cnitr.cn.R;
import com.cnitr.cn.entity.CommonWebEntity;
import com.cnitr.cn.Constant;
import com.cnitr.cn.service.BookmarkDao;
import com.cnitr.cn.util.CommonUtil;
import com.just.agentweb.AgentWeb;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * WebViewActivity
 * Created by YangChen on 2018/7/4.
 */

public class WebViewActivity extends SwipeBackBaseActivity {

    private AgentWeb mAgentWeb;
    private DownloadCompleteReceiver receiver;

    @Bind(R.id.layoutActionBar)
    RelativeLayout layoutActionBar;

    @Bind(R.id.layoutContent)
    LinearLayout layoutContent;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 绑定视图组件
        ButterKnife.bind(this);

        // 初始化导航栏
        initActionBar();

        // 初始化
        init();
    }

    /**
     * 初始化导航栏
     */
    protected void initActionBar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(layoutContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(getUrl());
        mAgentWeb.getWebCreator().getWebView().setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                // 判断文件是否已下载
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file != null && file.exists()) {
                    SharedPreferences mSharedPreferences = getSharedPreferences(Constant.SYSTEM_CODE, Context.MODE_PRIVATE);
                    Intent intent = new Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, mSharedPreferences.getLong(fileName, -1));
                    sendBroadcast(intent);
                } else {
                    // 调用系统下载管理下载
                    downloadBySystem(url, contentDisposition, mimetype);
                }
            }
        });

        // 注册附件下载监听
        receiver = new DownloadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);
    }

    private String getUrl() {
        String url = getIntent().getStringExtra("url");
        return url;
    }

    // WebViewClient
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            toolbar.setTitle(title != null ? title : "");
        }
    };

    // WebChromeClient
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        ButterKnife.unbind(this);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!(mAgentWeb.handleKeyEvent(keyCode, event))) {
            return super.onKeyDown(keyCode, event);
        } else {
            return true;
        }
    }

    /**
     * 下载附件
     */
    private void downloadBySystem(String url, String contentDisposition, String mimeType) {

        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 设置下载文件保存的路径和文件名
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(request);
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.SYSTEM_CODE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putLong(fileName, id).commit();
    }

    /**
     * DownloadCompleteReceiver
     */
    private class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                    Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                    if (uri != null) {
                        Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            handlerIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            handlerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        handlerIntent.setDataAndType(uri, type);
                        context.startActivity(handlerIntent);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.openBrowser) {
            if (mAgentWeb != null) {
                Uri uri = Uri.parse(mAgentWeb.getWebCreator().getWebView().getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.copyUrl) {
            if (mAgentWeb != null) {
                String url = mAgentWeb.getWebCreator().getWebView().getUrl();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("XApp Url", url);
                clipboardManager.setPrimaryClip(clipData);
                CommonUtil.showActionMessage("已复制到剪切板", layoutContent);
            }
            return true;
        }
        if (id == R.id.addBookmark) {
            String url = mAgentWeb.getWebCreator().getWebView().getUrl();
            String title = mAgentWeb.getWebCreator().getWebView().getTitle();

            CommonWebEntity entity = new CommonWebEntity();
            entity.setTitle(title);
            entity.setUrl(url);

            if (BookmarkDao.getInstance().getBookmark(url) == null) {
                BookmarkDao.getInstance().saveBookmark(entity);
            }
            CommonUtil.showActionMessage("已添加到我的书签", layoutContent);
        }
        return super.onOptionsItemSelected(item);
    }

}
