package com.cnitr.cn.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnitr.cn.R;
import com.cnitr.cn.entity.JokesEntity;
import com.cnitr.cn.util.CommonUtil;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.MyViewHolder> {

    private List<JokesEntity.JokesBaseEntity.Jokes> data;
    private Context mContext;

    public JokesAdapter(Context context, List<JokesEntity.JokesBaseEntity.Jokes> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_jokes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final JokesEntity.JokesBaseEntity.Jokes item = data.get(position);

        String content = item.getContent();
        String time = item.getUpdatetime();
        if (content != null) {
            content = content.replaceAll("&nbsp;", " ");
        }
        holder.title.setText(content == null ? "" : content);
        holder.time.setText(time == null ? "" : CommonUtil.dateFormat(time));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView title;
        private AppCompatTextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
            time = (AppCompatTextView) itemView.findViewById(R.id.time);
        }
    }


}
