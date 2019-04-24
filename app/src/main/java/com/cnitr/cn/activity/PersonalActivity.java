package com.cnitr.cn.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnitr.cn.R;
import com.cnitr.cn.adapter.PersonalAdapter;
import com.cnitr.cn.entity.ListItemEntity;
import com.cnitr.cn.entity.UserEntity;
import com.cnitr.cn.Constant;
import com.cnitr.cn.service.UserDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 个人资料
 * Created by YangChen on 2018/7/10.
 */

public class PersonalActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private PersonalAdapter adapter;
    private List<ListItemEntity> data;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void init() {
        setUserData();
    }

    /**
     * 设置个人资料
     */
    private void setUserData() {
        final String[] personal_text = getResources().getStringArray(R.array.arrays_mine_data);
        UserEntity user = UserDao.getInstance().getUser();
        data = new ArrayList<ListItemEntity>();
        for (int i = 0; i < personal_text.length; i++) {
            ListItemEntity item = new ListItemEntity();
            item.setTv(personal_text[i]);
            String text = "";
            if (user != null) {
                switch (i) {
                    case 0:
                        text = user.getUsername();
                        break;
                    case 1:
                        text = user.getEmail();
                        break;
                    case 2:
                        text = user.getTelephone();
                        break;
                    case 3:
                        text = user.getSignature();
                        break;
                    default:
                        break;
                }
            }
            item.setText(text);
            data.add(item);
        }
        adapter = new PersonalAdapter(this, data, new PersonalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (data != null) {
                    ListItemEntity item = data.get(postion);
                    if (item != null) {
                        Intent intent = new Intent(PersonalActivity.this, UserEditActivity.class);
                        intent.putExtra("message", item.getText());
                        intent.putExtra("type", postion);
                        intent.putExtra(Constant.INTENT_TITLE, personal_text[postion]);
                        startActivityForResult(intent, 100);
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (resultCode == RESULT_OK) {
            setUserData();
        }
    }

}
