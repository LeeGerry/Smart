package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import edu.auburn.smart.utils.PrefUtils;


/**
 * Saved Page
 * Author: Gary
 * Time: 17/1/13
 */

public class SavedPage extends BasePage {
    public static int SAVEDPAGE_RETURN_CODE = 888;
    private List<Article> articles;
    private String url ;
    ArticleAdapter adapter;
    private GridView gv;
    private TextView tvNoData;
    public SavedPage(Activity ctx) {
        super(ctx);
//        url = AppConfig.API_ARTICLE + "?start="+10+"&count="+20+"&categoryid="+8;
        adapter = new ArticleAdapter();
        articles = new ArrayList<>();
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        View v = View.inflate(mActivity, R.layout.result_grid, null);
        gv = (GridView) v.findViewById(R.id.gv_result);
        tvNoData = (TextView) v.findViewById(R.id.tvTipNoData);
        flContent.addView(v);
        return view;
    }

    @Override
    public void initData() {
        tvTitle.setText("Saved");

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("article", (Article)adapter.getItem((int)id));
                mActivity.startActivityForResult(intent,SAVEDPAGE_RETURN_CODE);
            }
        });
        articles = new ArrayList<>();
        adapter = new ArticleAdapter();
        gv.setAdapter(adapter);
        getDataFromServer();
    }

    private void getDataFromServer(){
        tvNoData.setVisibility(View.GONE);
        String ids = PrefUtils.getString(mActivity, AppConfig.COLLECT_ID,"");
        if (TextUtils.isEmpty(ids)){
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        url = AppConfig.createUrlBySavedIds(ids);
//        url = AppConfig.API_ARTICLE + "?start="+10+"&count="+20+"&categoryid="+8;
        Log.i("url",url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                parserJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
        try {
            JSONArray ja = new JSONArray(result);
            for(int i=0; i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                int id = jo.getInt("articleId");
                long date = jo.getLong("articleDate");
                String title = jo.getString("articleTitle");
                String img_url = jo.getString("articleImg");
                String desc = jo.getString("articleDescription");
                String url = jo.getString("articleUrl");
                int c_id = jo.getInt("category_id");
                int s_id = jo.getInt("source_id");
                Article a = new Article(id,date,title,img_url,desc,url,c_id,s_id);
                articles.add(a);
            }
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ArticleAdapter extends BaseAdapter {
        ImageOptions options;
        public ArticleAdapter(){
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

            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.article_item_gv,null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
//                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            Article item = (Article) getItem(position);
            holder.tvTitle.setText(item.getArticleTitle());
            String url = item.getArticleImg();
            x.image().bind(holder.ivIcon, url, options);
            return convertView;
        }
    }
    private static class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
//        TextView tvTime;
    }
}
