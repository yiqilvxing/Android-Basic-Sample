package com.cnitr.cn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.tools.note.NoteEditActivity;
import com.cnitr.cn.activity.tools.note.NoteListActivity;
import com.cnitr.cn.greendao.entity.Note;
import com.cnitr.cn.util.CommonUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by YangChen on 2018/7/4.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private List<Note> data;
    private Context mContext;
    private Map<Integer, Boolean> selectsMap;
    private boolean isOperation;

    public NoteAdapter(Context context, List<Note> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Note item = data.get(position);

        holder.title.setText(item.getContent());
        holder.time.setText(CommonUtil.dateFormat(item.getTime()));
        holder.checkbox.setChecked(selectsMap.get(position));
        if (isOperation) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectsMap.put(position, true);
                } else {
                    selectsMap.put(position, false);
                }
            }
        });
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOperation) {
                    if (holder.checkbox.isChecked()) {
                        holder.checkbox.setChecked(false);
                    } else {
                        holder.checkbox.setChecked(true);
                    }
                } else {
                    Intent intent = new Intent(mContext, NoteEditActivity.class);
                    intent.putExtra("NoteEntity", item);
                    NoteListActivity activity = (NoteListActivity) mContext;
                    activity.startActivityForResult(intent, NoteListActivity.CODE_EDIT);
                }
            }
        });
        holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(NoteListActivity.ACTION_NOTELISTACTIVITY_OPERATION);
                mContext.sendBroadcast(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layoutItem;
        private AppCompatTextView time;
        private AppCompatTextView title;
        private AppCompatCheckBox checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.layoutItem);
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
            time = (AppCompatTextView) itemView.findViewById(R.id.time);
            checkbox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    public void setSelectsMap(Map<Integer, Boolean> selectsMap) {
        this.selectsMap = selectsMap;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }

    public boolean isOperation() {
        return isOperation;
    }

}
