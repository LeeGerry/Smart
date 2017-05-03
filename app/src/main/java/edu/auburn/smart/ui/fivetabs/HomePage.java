package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BasePage;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.Category;
import edu.auburn.smart.ui.ArticlePage;


/**
 * 首页
 * Author: Gary
 * Time: 17/1/13
 */

public class HomePage extends BasePage implements ViewPager.OnPageChangeListener{
    private List<ArticlePage> articlePages;
    private ViewPager viewPager;
    private TabPageIndicator mIndicator;
    private ArticleAdapter adapter;
    private List<Category> categoryList;
    public HomePage(Activity ctx) {
        super(ctx);
        categoryList = new ArrayList<>();
        articlePages = new ArrayList<>();
        for (int i = 0; i<AppConfig.categories.length;i++){
            Category category = new Category();
            category.setId(i+1);
            category.setName(AppConfig.categories[i]);
            categoryList.add(category);
            articlePages.add(new ArticlePage(mActivity, category));
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        View pager = View.inflate(mActivity, R.layout.fragment_favorite, null);
        viewPager = (ViewPager) pager.findViewById(R.id.viewpager);
        mIndicator = (TabPageIndicator) pager.findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        adapter = new ArticleAdapter();
        flContent.addView(pager);
        return view;
    }

    @Override
    public void initData() {
        tvTitle.setText("Favorite");
        viewPager.setAdapter(adapter);
        mIndicator.setViewPager(viewPager);
        mIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("position"+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ArticleAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryList.get(position).getName();
        }

        @Override
        public int getCount() {
            return articlePages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ArticlePage pager = articlePages.get(position);
            View v = pager.mRootView;
            pager.initData();
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
