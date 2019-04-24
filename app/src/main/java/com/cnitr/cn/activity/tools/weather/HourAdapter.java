package com.cnitr.cn.activity.tools.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnitr.cn.R;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;

/**
 * Created by len_titude on 2017/5/13.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

    private List<HourlyBase> mhourList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourDegree;
        TextView hourTime;
        TextView hourText;

        public ViewHolder(View itemView) {
            super(itemView);
            hourDegree = (TextView) itemView.findViewById(R.id.hour_degree);
            hourTime = (TextView) itemView.findViewById(R.id.hout_time);
            hourText = (TextView) itemView.findViewById(R.id.hour_text);
        }
    }

    public HourAdapter(List<HourlyBase> hourList) {
        mhourList = hourList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hourly_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyBase hour = mhourList.get(position);
        holder.hourDegree.setText(hour.getTmp() + "°C");
        holder.hourText.setText(hour.getCond_txt());
        holder.hourTime.setText(hour.getTime().split(" ")[1]);
    }

    @Override
    public int getItemCount() {
        return mhourList.size();
    }
}
