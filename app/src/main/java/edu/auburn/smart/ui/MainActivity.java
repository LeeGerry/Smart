package edu.auburn.smart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import edu.auburn.smart.R;
import edu.auburn.smart.ui.fivetabs.SavedPage;


public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_FRAGMENT_MAIN = "tag_fragment_main";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu menu = getSlidingMenu();
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffset(500);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        initFragment();
    }
    private ContentFragment ct;
    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//开始事务
        ct = new ContentFragment();
//        transaction.replace(R.id.fl_left, new LeftMenuFragment(), TAG_FRAGMENT_MENU);//用于fragment替换
        transaction.replace(R.id.fl_main, ct,TAG_FRAGMENT_MAIN);
        transaction.commit();//提交事务
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SavedPage.SAVEDPAGE_RETURN_CODE){

            ct.getSavedPage().initData();
        }
    }
}
