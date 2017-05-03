package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BasePage;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.ui.SearchResultActivity;


/**
 * Search Page
 * Author: Gary
 * Time: 17/1/13
 */

public class SearchPage extends BasePage implements View.OnClickListener{
    private TextView tv1,tv2,tv3,tv4,tv5;
    public SearchPage(Activity ctx) {
        super(ctx);
    }
    private String url ;
    private EditText etKeyWords;
    private Button btnSearch;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);

        View pager = View.inflate(mActivity, R.layout.search_fragment, null);
        tv1 = (TextView) pager.findViewById(R.id.tv1);
        tv2 = (TextView) pager.findViewById(R.id.tv2);
        tv3 = (TextView) pager.findViewById(R.id.tv3);
        tv4 = (TextView) pager.findViewById(R.id.tv4);
        tv5 = (TextView) pager.findViewById(R.id.tv5);
        btnSearch = (Button) pager.findViewById(R.id.btnSearch);
        etKeyWords = (EditText) pager.findViewById(R.id.etKeyWords);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        flContent.addView(pager);
        return view;
    }

    @Override
    public void initData() {
        tvTitle.setText("Search");
    }

    @Override
    public void onClick(View v) {
        String key;
        if (v.getId() == R.id.btnSearch){
            key = etKeyWords.getText().toString().trim();
        }else{
            key = ((TextView) v).getText().toString().trim();
        }
        if (TextUtils.isEmpty(key)){
            Toast.makeText(mActivity,"Please input keywords",Toast.LENGTH_SHORT).show();
            return;
        }
        url = AppConfig.createUrlWithKeywords(0, key);
        if (!TextUtils.isEmpty(url)){
            Intent intent = new Intent(mActivity, SearchResultActivity.class);
            intent.putExtra("url",url);
            mActivity.startActivity(intent);
        }
    }
}
