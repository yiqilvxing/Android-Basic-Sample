package com.cnitr.cn.activity.tools.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.greendao.entity.Note;
import com.cnitr.cn.util.CommonUtil;

import butterknife.Bind;

/**
 * Created by YangChen on 2018/11/1.
 */

public class NoteEditActivity extends BaseActivity {

    @Bind(R.id.tViewDate)
    AppCompatTextView tViewDate;

    @Bind(R.id.editText)
    AppCompatEditText editText;

    @Bind(R.id.btnOk)
    AppCompatButton btnOk;

    private Note noteEntity;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_note_edit;
    }

    @Override
    protected void init() {
        noteEntity = (Note) getIntent().getSerializableExtra("NoteEntity");
        if (noteEntity == null) {
            getSupportActionBar().setTitle("添加备忘录");
        } else {
            getSupportActionBar().setTitle("编辑备忘录");
        }
        tViewDate.setText(CommonUtil.dateFormat(System.currentTimeMillis()));

        // 设置自动换行
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setGravity(Gravity.TOP);
        editText.setSingleLine(false);
        editText.setHorizontallyScrolling(false);
        if (noteEntity != null) {
            editText.setText(noteEntity.getContent());
            editText.setSelection(editText.getText().length());
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String descs = editText.getText().toString().trim();
                if (TextUtils.isEmpty(descs)) {
                    CommonUtil.showActionMessage("请输入内容", v);
                    return;
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(NoteEditActivity.this);
                mBuilder.setTitle("是否确认保存？");
                mBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (noteEntity == null) {
                            Note entity = new Note();
                            entity.setContent(descs);
                            entity.setTime(System.currentTimeMillis());
                            MyApplication.getDaoSession().getNoteDao().insert(entity);
                        } else {
                            noteEntity.setContent(descs);
                            noteEntity.setTime(System.currentTimeMillis());
                            MyApplication.getDaoSession().getNoteDao().save(noteEntity);
                        }
                        setResult(RESULT_OK);
                        NoteEditActivity.this.finish();
                    }
                });
                mBuilder.setNegativeButton(android.R.string.no, null);
                mBuilder.show();
            }
        });
    }


}
