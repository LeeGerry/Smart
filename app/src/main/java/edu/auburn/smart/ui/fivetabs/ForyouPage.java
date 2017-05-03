package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BasePage;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.Article;
import edu.auburn.smart.ui.WebActivity;
import edu.auburn.smart.utils.ParserUtils;
import edu.auburn.smart.view.PullToRefreshListView;


/**
 * 首页
 * Author: Gary
 * Time: 17/1/13
 */

public class ForyouPage extends BasePage {
    private PullToRefreshListView listView;
    private List<Article> articles;
    private String url;
    private int start = 0, count = 10;
    private boolean moreData = true;
    ArticleAdapter adapter;

    public ForyouPage(Activity ctx) {
        super(ctx);
        url = AppConfig.createUrlWithStart(start);
        adapter = new ArticleAdapter();
        articles = new ArrayList<>();
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        View v = View.inflate(mActivity, R.layout.article_page, null);
        listView = (PullToRefreshListView) v.findViewById(R.id.lv_article);
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (!moreData) { // 没有下一页了
                    Toast.makeText(mActivity, "no more data", Toast.LENGTH_SHORT).show();
                    listView.OnRefreshComplete(true);
                } else {
                    getMoreData();
                }
            }
        });
        flContent.addView(v);
        return view;
    }

    private void getMoreData() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parserJsonMore(result);
                //收起下拉刷新
                listView.OnRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                //收起下拉刷新
                listView.OnRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        tvTitle.setText("For you");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("article", (Article)adapter.getItem((int)id));
                //intent.putExtra("url", articles.get(position).getArticleUrl());
                mActivity.startActivity(intent);
            }
        });
        adapter = new ArticleAdapter();
        getDataFromServer();
    }

    private void getDataFromServer() {
        start = 0;
        moreData = true;
        url = AppConfig.createUrlWithStart(start);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parserJson(result);
                //收起下拉刷新
                listView.OnRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                //收起下拉刷新
                listView.OnRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parserJson(String result) {
        articles = ParserUtils.parserData(result);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void parserJsonMore(String result) {
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ja.length() == 0) {
            moreData = false;
        } else {
            List<Article> list = ParserUtils.parserData(result);
            articles.addAll(list);
            start += count;
            url = AppConfig.createUrlWithStart(start);
        }
        adapter.notifyDataSetChanged();
    }

    class ArticleAdapter extends BaseAdapter {
        ImageOptions options;

        public ArticleAdapter() {
            options = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.img_bg)
                    .setFailureDrawableId(R.mipmap.img_bg)
                    .build();
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

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.article_item, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Article item = (Article) getItem(position);
            holder.tvTitle.setText(item.getArticleTitle());
            holder.tvTime.setText(item.getArticleDescription());
            String url = item.getArticleImg();
            x.image().bind(holder.ivIcon, url, options);
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvTime;
    }
}
