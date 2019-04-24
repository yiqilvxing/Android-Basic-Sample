package com.cnitr.cn.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cnitr.cn.Constant;
import com.cnitr.cn.R;
import com.cnitr.cn.entity.UserEntity;
import com.cnitr.cn.service.UserDao;
import com.cnitr.cn.util.RegexUtil;
import com.cnitr.cn.widget.ClearEditText;

import butterknife.Bind;

/**
 * 资料编辑
 * Created by YangChen on 2018/7/10.
 */

public class UserEditActivity extends BaseActivity {

    @Bind(R.id.editText)
    ClearEditText editText;

    @Bind(R.id.btnOk)
    AppCompatButton btnOk;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_edit;
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        final int type = intent.getIntExtra("type", -1);
        if (type == 2) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (message != null) {
            editText.setText(message);
            editText.setSelection(editText.getText().length());
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                UserEntity user = UserDao.getInstance().getUser();
                if (user == null) {
                    user = new UserEntity();
                }
                switch (type) {
                    case 0:
                        user.setUsername(text);
                        break;
                    case 1:
                        if (!TextUtils.isEmpty(text) && !RegexUtil.commonRegex(text, RegexUtil.REGEX_EMAIL)) {
                            Toast.makeText(UserEditActivity.this, "请输入正确格式的邮箱地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user.setEmail(text);
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(text) && !RegexUtil.commonRegex(text, RegexUtil.REGEX_MOBILE)) {
                            Toast.makeText(UserEditActivity.this, "请输入正确格式的手机号码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user.setTelephone(text);
                        break;
                    case 3:
                        user.setSignature(text);
                        break;
                    default:
                        break;
                }
                UserDao.getInstance().saveUser(user);
                Intent intent = new Intent(Constant.ACTION_REFRESH_HEADER);
                sendBroadcast(intent);
                UserEditActivity.this.setResult(RESULT_OK);
                UserEditActivity.this.finish();
            }
        });
    }

}
