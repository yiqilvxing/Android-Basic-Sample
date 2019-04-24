package com.cnitr.cn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnitr.cn.R;
import com.cnitr.cn.entity.ExpressEntity;
import com.cnitr.cn.util.CommonUtil;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class ExpressAdapter extends BaseAdapter {

    private Context mContext;
    private List<ExpressEntity.Express> data;

    public ExpressAdapter(Context context, List<ExpressEntity.Express> data) {

        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_express, null);

            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.viewLineTop = convertView.findViewById(R.id.viewLineTop);
            holder.viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            holder.tViewDate = (TextView) convertView.findViewById(R.id.tViewDate);
            holder.tViewTime = (TextView) convertView.findViewById(R.id.tViewTime);
            holder.tViewContent = (TextView) convertView.findViewById(R.id.tViewContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            ExpressEntity.Express info = data.get(position);

            String timeStr = info.getTime();
            String message = info.getContext();

            if (timeStr != null) {
                timeStr = CommonUtil.dateFormat(timeStr, "yyyy-MM-dd HH:mm");
            }

            String date = "";
            String time = "";
            if (timeStr != null) {
                String[] array = timeStr.split(" ");
                if (array != null && array.length >= 2) {
                    date = array[0];
                    time = array[1];
                }
            }
            holder.tViewDate.setText(date == null ? "" : date);
            holder.tViewTime.setText(time == null ? "" : time);
            holder.tViewContent.setText(message == null ? "" : message);

            if (position == 0) {
                holder.img.setImageResource(R.mipmap.state_on);
                holder.tViewContent.setTextColor(Color.parseColor("#666666"));
                holder.tViewDate.setTextColor(Color.parseColor("#666666"));
                holder.tViewTime.setTextColor(Color.parseColor("#666666"));
            } else {
                holder.img.setImageResource(R.mipmap.state_off);
                holder.tViewContent.setTextColor(Color.parseColor("#999999"));
                holder.tViewDate.setTextColor(Color.parseColor("#999999"));
                holder.tViewTime.setTextColor(Color.parseColor("#999999"));
            }

            if (position == 0) {
                holder.viewLineTop.setVisibility(View.INVISIBLE);
                holder.viewLineBottom.setVisibility(View.VISIBLE);
            } else {
                holder.viewLineTop.setVisibility(View.VISIBLE);
                holder.viewLineBottom.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView img;
        private View viewLineTop;
        private View viewLineBottom;
        private TextView tViewDate;
        private TextView tViewTime;
        private TextView tViewContent;
    }
}


