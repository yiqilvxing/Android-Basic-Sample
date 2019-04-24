package com.cnitr.cn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.WebViewActivity;
import com.cnitr.cn.entity.CommonWebEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class WebLinkAdapter extends RecyclerView.Adapter<WebLinkAdapter.MyViewHolder> {

    private List<CommonWebEntity> data;
    private Context mContext;

    public WebLinkAdapter(Context context, List<CommonWebEntity> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_web, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final CommonWebEntity item = data.get(position);

        holder.title.setText(item.getTitle());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", item.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layoutItem;
        private AppCompatImageView image;
        private AppCompatTextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.layoutItem);
            image = (AppCompatImageView) itemView.findViewById(R.id.image);
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
        }
    }


}
