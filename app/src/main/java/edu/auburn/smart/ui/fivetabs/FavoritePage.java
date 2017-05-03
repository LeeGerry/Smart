package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BasePage;


/**
 * 首页
 * Author: Gary
 * Time: 17/1/13
 */

public class FavoritePage extends BasePage {
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    public FavoritePage(Activity ctx) {
        super(ctx);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        View pager = View.inflate(mActivity, R.layout.fragment_favorite, null);
        x.view().inject(this, pager);
        flContent.addView(pager);
        return view;
    }

    @Override
    public void initData() {
        TextView tv = new TextView(mActivity);
        tv.setText("Favorite");
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        flContent.addView(tv);
        tvTitle.setText("Favorite");
        //ibLeftButton.setVisibility(View.GONE);
    }
}
