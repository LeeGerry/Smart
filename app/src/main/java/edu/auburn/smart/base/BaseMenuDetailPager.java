package edu.auburn.smart.base;

import android.app.Activity;
import android.view.View;

/**
 * BaseMenuDetailPager核心区域内容界面的基类
 * Author: Gary
 * Time: 17/1/15
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//菜单详情页的根布局
    public BaseMenuDetailPager(Activity activity){
        this.mActivity = activity;
        mRootView = initView();
    }

    /**
     * 初始化布局，子类必须实现
     */
    public abstract View initView();

    /**
     * 初始化数据，当需要加载或更新数据时调用
     */
    public void initData(){}
}
