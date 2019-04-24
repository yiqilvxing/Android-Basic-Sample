package com.cnitr.cn.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnitr.cn.R;
import com.cnitr.cn.entity.DiscoveryEntity;
import com.cnitr.cn.util.CommonUtil;
import com.liji.imagezoom.util.ImageZoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangChen on 2018/7/5.
 */

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.MyViewHolder> {

    private Context mContext;
    private List<DiscoveryEntity> data;

    public DiscoveryAdapter(Context context, List<DiscoveryEntity> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.item_discovery, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final DiscoveryEntity entity = data.get(position);
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.width = entity.getWidth();
            params.height = entity.getHeight();
            holder.image.setLayoutParams(params);

            String url = CommonUtil.formatZoomImageDownLoadUrl(entity.getUrl(), params.width, params.height);
            Glide.with(mContext).load(url).placeholder(R.color.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> imgUrls = new ArrayList<String>();
                    for (DiscoveryEntity discoveryEntity : data) {
                        imgUrls.add(discoveryEntity.getUrl());
                    }
                    ImageZoom.show(mContext, position, imgUrls);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (AppCompatImageView) itemView.findViewById(R.id.image);
        }

    }

}
