package com.cnitr.cn.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cnitr.cn.R;
import com.cnitr.cn.adapter.MessageAdapter;
import com.cnitr.cn.entity.MessageEntity;
import com.cnitr.cn.service.MessageDao;
import com.cnitr.cn.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 极光推送消息
 * Created by YangChen on 2018/11/1.
 */

public class JPushMessageActivity extends BaseActivity {

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

    private MessageAdapter adapter;
    private List<MessageEntity> data;
    private Map<Integer, Boolean> selectsMap;

    public static final String ACTION_NOTELISTACTIVITY_OPERATION = "JPUSHMESSAGEACTIVITY_OPERATION";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent != null && action.equals(ACTION_NOTELISTACTIVITY_OPERATION)) {
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
        List<MessageEntity> noteEntityList = MessageDao.getInstance().getAll();
        if (noteEntityList != null) {
            for (int i = noteEntityList.size() - 1; i >= 0; i--) {
                data.add(noteEntityList.get(i));
                selectsMap.put(i, false);
            }
        }
        adapter = new MessageAdapter(this, data);
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
                    MessageEntity item = data.get(i);
                    if (selectsMap.get(i)) {
                        count++;
                        if (MessageDao.getInstance().delete(item)) {
                            data.remove(i);
                        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

}
