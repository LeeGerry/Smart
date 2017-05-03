package edu.auburn.smart.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BaseActivity;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.Article;
import edu.auburn.smart.utils.PrefUtils;

/**
 * Author: Gary
 * Time: 4/11/17
 */

public class WebActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.tv_title)
    private TextView title;
    @ViewInject(R.id.ib_left_menu)
    private ImageButton ibBack;
    @ViewInject(R.id.ib_right_fav)
    private ImageButton ibFav;
    @ViewInject(R.id.wv_news_details)
    private WebView wvNews;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;
    private boolean isload = false;
    private String url;
    private Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        x.view().inject(this);
//        url = getIntent().getStringExtra("url");
        article = (Article) getIntent().getSerializableExtra("article");
        url = article.getArticleUrl();
        ibBack.setVisibility(View.VISIBLE);
        title.setText("Article Details");
        ibBack.setImageResource(R.drawable.back);
        ibBack.setOnClickListener(this);
        ibFav.setVisibility(View.VISIBLE);
        ibFav.setOnClickListener(this);
        wvNews.loadUrl(url);
        log(article.getArticleId()+"|||"+url);
        WebSettings settings = wvNews.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        pbLoading.setVisibility(View.VISIBLE);
        if (checkId(article.getArticleId())){
            ibFav.setImageResource(R.mipmap.fav_select);
        }else{
            ibFav.setImageResource(R.mipmap.fav_normal);
        }
        wvNews.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //return super.shouldOverrideUrlLoading(view, request);
                view.loadUrl(request.getUrl().toString());
                //Log.i("webview","overideurlloading");
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //Log.i("webview","started");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.i("webview", "finished");
            }
        });
        wvNews.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //Log.i("webview", "load..."+newProgress);
                if (newProgress > 50){//WebViewClient中的 pageFinished 方法有bug,不执行，
                    // 这个progress也不准，现提前结束。
                    pbLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //Log.i("webview", "title:" + title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_left_menu:
                finish();
                break;
            case R.id.ib_right_fav:
                if (checkId(article.getArticleId())){
                    delId(article.getArticleId());
                    ibFav.setImageResource(R.mipmap.fav_normal);
                }else{
                    saveId(article.getArticleId());
                    ibFav.setImageResource(R.mipmap.fav_select);
                }
                break;
        }
    }

    /**
     * add article id into xml
     * @param id
     */
    private void saveId(int id){
        String id_str = PrefUtils.getString(this, AppConfig.COLLECT_ID, "");
        String[] ids = id_str.split("-");
        List<String> list = new ArrayList<>();
        for (String temp : ids){
            temp = temp.trim();
            if("".equals(temp))
                continue;
            list.add(temp);
        }
        if (!list.contains(id+"")){
            list.add(id+"");
            StringBuilder sb = new StringBuilder();
            for(String s: list){
                sb.append(s).append("-");
            }
            sb.deleteCharAt(sb.length()-1);
            PrefUtils.setString(this, AppConfig.COLLECT_ID, sb.toString());
        }
    }

    /**
     * delete article id from xml
     * @param id
     */
    private void delId(int id){
        String id_str = PrefUtils.getString(this, AppConfig.COLLECT_ID, "");
        if (!TextUtils.isEmpty(id_str)){
            String[] ids = id_str.split("-");
            List<String> list = new ArrayList<>();
            for (String temp : ids){
                temp = temp.trim();
                if("".equals(temp))
                    continue;
                list.add(temp);
            }

            String temp = id + "";
            if (list.contains(temp)){
                list.remove(temp);
                StringBuilder sb = new StringBuilder();
                for(String s: list){
                    sb.append(s).append("-");
                }
                if (sb.length()>0 && sb.charAt(sb.length()-1)=='-')
                    sb.deleteCharAt(sb.length()-1);
                PrefUtils.setString(this, AppConfig.COLLECT_ID, sb.toString());
            }
        }
    }

    /**
     * check id
     * @param id
     * @return
     */
    private boolean checkId(int id){
        String id_str = PrefUtils.getString(this, AppConfig.COLLECT_ID, "");
        if (!TextUtils.isEmpty(id_str)){
            String[] ids = id_str.split("-");
            List<String> list = new ArrayList<>();
            for (String temp : ids){
                temp = temp.trim();
                if("".equals(temp))
                    continue;
                list.add(temp);
            }
            if (list.contains(id+"")){
                return true;
            }
        }
        return false;
    }
    private int mWhich ;
    private int mCurrentWhich = 2;
    private void showChooseSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_size_setting));
        String[] items = new String[]{"Super big", "Big","Normal","Small","Super small"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mWhich = which;
            }
        });
        builder.setPositiveButton("Comfirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wvNews.getSettings();
                switch (mWhich){
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(75);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;
                }
                mCurrentWhich = mWhich;
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        url = null;
//        wvNews.destroy();
        wvNews.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    wvNews.destroy();
                } catch (Exception ex) {

                }
            }
        }, 3000);
    }
}
