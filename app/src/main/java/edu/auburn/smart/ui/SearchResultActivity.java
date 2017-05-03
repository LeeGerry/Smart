package edu.auburn.smart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BaseActivity;
import edu.auburn.smart.model.Article;
import edu.auburn.smart.utils.ParserUtils;
import edu.auburn.smart.view.ArticleListAdapter;

/**
 * Author: Gary
 * Time: 4/11/17
 */

public class SearchResultActivity extends BaseActivity {
    @ViewInject(R.id.ib_left_menu)
    private ImageButton ibBack;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.lv_result)
    private ListView listView;
    private List<Article> articles;
    private String url ;
    ArticleListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        x.view().inject(this);
        articles = new ArrayList<>();
        url = getIntent().getStringExtra("url");
        tvTitle.setText("Result");
        ibBack.setImageResource(R.drawable.back);
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResultActivity.this, WebActivity.class);
                intent.putExtra("article", (Article)adapter.getItem((int)id));
                startActivity(intent);
            }
        });
        adapter = new ArticleListAdapter(this, articles);
        getDataFromServer();
    }

    private void getDataFromServer(){
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
        articles.addAll(ParserUtils.parserData(result));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
