package com.cnitr.cn.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.MyBookmarksActivity;
import com.cnitr.cn.activity.WebViewActivity;
import com.cnitr.cn.entity.CommonWebEntity;
import com.cnitr.cn.service.BookmarkDao;
import com.cnitr.cn.util.CommonUtil;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class MyBookmarksAdapter extends RecyclerView.Adapter<MyBookmarksAdapter.MyViewHolder> {

    private List<CommonWebEntity> data;
    private Context mContext;

    public MyBookmarksAdapter(Context context, List<CommonWebEntity> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_my_bookmarks, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

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

        holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setMessage("是否删除此书签?");
                mBuilder.setNegativeButton(android.R.string.no, null);
                mBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        BookmarkDao.getInstance().deleteBookmark(item.get_id());
                        CommonUtil.showActionMessage("删除成功", v);
                        Intent intent = new Intent(MyBookmarksActivity.ACTION_MYBOOKMARKSACTIVITY);
                        mContext.sendBroadcast(intent);
                    }
                });
                mBuilder.create().show();
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
