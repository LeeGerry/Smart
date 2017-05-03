package edu.auburn.smart.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import edu.auburn.smart.R;
import edu.auburn.smart.model.Article;

/**
 * Author: Gary
 * Time: 4/18/17
 */

public class ArticleListAdapter extends BaseAdapter {
    private ImageOptions options;
    private Activity ctx;
    private List<Article> articles;
    public ArticleListAdapter(Activity activity, List<Article> list){
        options = new ImageOptions.Builder()
                .setLoadingDrawableId(R.mipmap.img_bg)
                .setFailureDrawableId(R.mipmap.img_bg)
                .build();
        this.ctx = activity;
        this.articles = list;
    }
    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = View.inflate(ctx, R.layout.article_item,null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article item = (Article) getItem(position);
        holder.tvTitle.setText(item.getArticleTitle());
        holder.tvTime.setText(item.getArticleDescription());
        String url = item.getArticleImg();
        x.image().bind(holder.ivIcon, url, options);
        return convertView;
    }
    static class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvTime;
    }
}
