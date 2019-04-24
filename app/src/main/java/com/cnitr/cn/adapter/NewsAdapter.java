package com.cnitr.cn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.WebViewActivity;
import com.cnitr.cn.entity.WechatItem;
import com.cnitr.cn.util.CommonUtil;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<WechatItem.ResultBean.ListBean> data;
    private Context mContext;

    public NewsAdapter(Context context, List<WechatItem.ResultBean.ListBean> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final WechatItem.ResultBean.ListBean item = data.get(position);

        holder.title.setText(item.getTitle());
        holder.browse.setText(" " + item.getHitCount());
        holder.date.setText(" " + CommonUtil.dateFormat(item.getPubTime()));

        String imgUrl = CommonUtil.formatZoomImageDownLoadUrl(mContext, item.getThumbnails(), 86, 64);
        Glide.with(mContext).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).animate(R.anim.alpha_in).into(holder.image);

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", item.getSourceUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layoutItem;
        private AppCompatImageView image;
        private AppCompatTextView title;
        private TextView browse;
        private TextView date;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutItem = (RelativeLayout) itemView.findViewById(R.id.layoutItem);
            image = (AppCompatImageView) itemView.findViewById(R.id.image);
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
            browse = (AppCompatTextView) itemView.findViewById(R.id.browse);
            date = (AppCompatTextView) itemView.findViewById(R.id.date);
        }
    }


}
