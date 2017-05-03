package edu.auburn.smart.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BaseFragment;
import edu.auburn.smart.base.BasePage;
import edu.auburn.smart.ui.fivetabs.ForyouPage;
import edu.auburn.smart.ui.fivetabs.HomePage;
import edu.auburn.smart.ui.fivetabs.SavedPage;
import edu.auburn.smart.ui.fivetabs.SearchPage;
import edu.auburn.smart.ui.fivetabs.SettingPage;
import edu.auburn.smart.view.NoScrollViewPager;

/**
 * 内容fragment
 * Author: Gary
 * Time: 17/1/11
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePage> mPages;//5个标签页的集合
    private ContentAdapter adapter;
    private RadioGroup rgTabs;

    @Override
    public void initData() {

        mPages = new ArrayList<>();
        mPages.add(new ForyouPage(mActivity));
        mPages.add(new HomePage(mActivity));
        mPages.add(new SearchPage(mActivity));
        mPages.add(new SavedPage(mActivity));
        mPages.add(new SettingPage(mActivity));
        adapter = new ContentAdapter();
        mViewPager.setAdapter(adapter);

        rgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbTab1:
                        mViewPager.setCurrentItem(0, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab2:
                        mViewPager.setCurrentItem(1, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab3:
                        mViewPager.setCurrentItem(2, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab4:
                        mViewPager.setCurrentItem(3, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab5:
                        mViewPager.setCurrentItem(4, false);//false表示是否具有滑动效果
                        break;
                }
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage pager = mPages.get(position);
                pager.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPages.get(0).initData();//刚开始调用第一个page初始化
    }





    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgTabs = (RadioGroup) view.findViewById(R.id.rg_tabs);
        return view;
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage basePage = mPages.get(position);//找到当前加载的page对象
            View view = basePage.rootView;
            //basePage.initData();//加载数据，才能显示;
            // viewPage会默认加载下一个页面，为了节省流量和性能，不在此处调用初始化数据的方法
            // 放到页签被选择的时候加载当前数据
            container.addView(view);//增加给容器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
    public SavedPage getSavedPage(){
        return (SavedPage) mPages.get(3);
    }
}
