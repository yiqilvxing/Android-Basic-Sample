package com.cnitr.cn.activity.tools.note;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.adapter.NoteAdapter;
import com.cnitr.cn.greendao.dao.NoteDao;
import com.cnitr.cn.greendao.entity.Note;
import com.cnitr.cn.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by YangChen on 2018/11/1.
 */

public class NoteListActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.no_data)
    View no_data;

    @Bind(R.id.layoutOperation)
    LinearLayout layoutOperation;

    @Bind(R.id.btnDelete)
    AppCompatButton btnDelete;

    @Bind(R.id.btnCancel)
    AppCompatButton btnCancel;

    private NoteAdapter adapter;
    private List<Note> data;
    private Map<Integer, Boolean> selectsMap;

    public static final String ACTION_NOTELISTACTIVITY = "NoteListActivity";
    public static final String ACTION_NOTELISTACTIVITY_OPERATION = "NOTELISTACTIVITY_OPERATION";
    public static final int CODE_ADD = 1001;
    public static final int CODE_EDIT = 1002;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent != null && action.equals(ACTION_NOTELISTACTIVITY)) {
                refreshData();
            } else if (intent != null && action.equals(ACTION_NOTELISTACTIVITY_OPERATION)) {
                setOnLongClickListener();
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void init() {
        refreshData();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTELISTACTIVITY);
        filter.addAction(ACTION_NOTELISTACTIVITY_OPERATION);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        selectsMap = new HashMap<Integer, Boolean>();
        data = new ArrayList<>();
        if (MyApplication.getDaoSession().getNoteDao() != null) {
            List<Note> noteEntityList = MyApplication.getDaoSession().getNoteDao().queryBuilder().orderDesc(NoteDao.Properties.Time).list();
            if (noteEntityList != null) {
                data.addAll(noteEntityList);
                for (int i = 0; i < data.size(); i++) {
                    selectsMap.put(i, false);
                }
            }
        }

        adapter = new NoteAdapter(this, data);
        adapter.setSelectsMap(selectsMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (data.isEmpty()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
        layoutOperation.setVisibility(View.GONE);
        adapter.setOperation(false);
    }

    // 长按事件
    private void setOnLongClickListener() {
        if (selectsMap == null) {
            selectsMap = new HashMap<>();
        }
        selectsMap.clear();
        for (int i = 0; i < data.size(); i++) {
            selectsMap.put(i, false);
        }
        layoutOperation.setVisibility(View.VISIBLE);
        adapter.setOperation(true);
        adapter.setSelectsMap(selectsMap);
        adapter.notifyDataSetChanged();

        // 删除
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (int i = data.size() - 1; i >= 0; i--) {
                    Note item = data.get(i);
                    if (selectsMap.get(i)) {
                        count++;
                        MyApplication.getDaoSession().getNoteDao().delete(item);
                        data.remove(i);
                    }
                }
                if (count > 0) {
                    CommonUtil.showActionMessage("删除成功", v);
                }
                refreshData();
            }
        });

        // 取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.edit) {
            setOnLongClickListener();
            return true;
        }
        if (id == R.id.add) {
            Intent intent = new Intent(this, NoteEditActivity.class);
            startActivityForResult(intent, CODE_ADD);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // 新增
            if (requestCode == CODE_ADD) {
                refreshData();
            }
            // 编辑
            else if (requestCode == CODE_EDIT) {
                refreshData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

}
