package edu.auburn.smart.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.smart.model.Article;

import static edu.auburn.smart.R.layout.result;

/**
 * Author: Gary
 * Time: 4/18/17
 */

public class ParserUtils {
    public static List<Article> parserData(String data){
        List<Article> articles = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(data);
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
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) continue;
                Article a = new Article(id,date,title,img_url,desc,url,c_id,s_id);
                articles.add(a);
            }
            return articles;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
