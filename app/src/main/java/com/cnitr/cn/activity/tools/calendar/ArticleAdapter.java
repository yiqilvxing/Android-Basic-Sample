package com.cnitr.cn.activity.tools.calendar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.MainActivity;
import com.cnitr.cn.activity.WebViewActivity;
import com.cnitr.cn.entity.WechatItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;
    private Context mContext;

    public ArticleAdapter(Context context) {
        super(context);
        this.mContext = context;
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日热点", create(MainActivity.hotNewsList, 0));
        titles.add("今日热点");
        resetGroups(map, titles);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, final Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextTitle.setText(item.getTitle());
        h.mTextContent.setText(item.getContent());
        mLoader.load(item.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into(h.mImageView);
        h.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", item.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle,
                mTextContent;
        private ImageView mImageView;
        private LinearLayout layoutItem;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTextContent = (TextView) itemView.findViewById(R.id.tv_content);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.layoutItem);
        }
    }

    private static List<Article> create(List<WechatItem.ResultBean.ListBean> hotList, int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0 && hotList != null) {
            for (WechatItem.ResultBean.ListBean bean : hotList) {
                Article article = new Article();
                article.setTitle(bean.getTitle());
                article.setContent(bean.getSubTitle());
                article.setImgUrl(bean.getThumbnails());
                article.setUrl(bean.getSourceUrl());
                list.add(article);
            }
        }
        return list;
    }

}
