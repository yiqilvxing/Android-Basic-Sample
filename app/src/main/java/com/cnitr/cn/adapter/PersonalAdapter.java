package com.cnitr.cn.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnitr.cn.R;
import com.cnitr.cn.entity.ListItemEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyViewHolder> {

    private List<ListItemEntity> data;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public PersonalAdapter(Context context, List<ListItemEntity> data, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.data = data;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_personal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ListItemEntity item = data.get(position);
        holder.tv.setText(item.getTv());
        holder.text.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatTextView tv;
        private AppCompatTextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (AppCompatTextView) itemView.findViewById(R.id.tv);
            text = (AppCompatTextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }


}

