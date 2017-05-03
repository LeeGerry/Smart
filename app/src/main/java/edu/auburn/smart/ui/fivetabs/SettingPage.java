package edu.auburn.smart.ui.fivetabs;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.auburn.smart.R;
import edu.auburn.smart.base.BasePage;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.User;
import edu.auburn.smart.ui.LoginActivity;
import edu.auburn.smart.utils.PrefUtils;


/**
 * Setting Page
 * Author: Gary
 * Time: 17/1/13
 */

public class SettingPage extends BasePage{
    private TextView tvLogout, tvUpdate, tvName;
    public SettingPage(Activity ctx) {
        super(ctx);
    }
    private CircleImageView ivHead;
    private LinearLayout llHead;
    private User user;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);

        View pager = View.inflate(mActivity, R.layout.setting_fragment, null);
        tvLogout = (TextView) pager.findViewById(R.id.tvLogout);
        llHead = (LinearLayout) pager.findViewById(R.id.llHead);

        String user_str = PrefUtils.getString(mActivity, AppConfig.USER, "");

        tvName = (TextView) pager.findViewById(R.id.tvUserName);
        ivHead = (CircleImageView) pager.findViewById(R.id.ivHead);
        if(TextUtils.isEmpty(user_str)) {
            tvName.setText("Login");
            tvLogout.setText("Login");
            ivHead.setImageResource(R.mipmap.ic_launcher);
        }else{
            user = new Gson().fromJson(user_str, User.class);
            tvName.setText(user.getName());
            ivHead.setImageResource(R.mipmap.head);
            tvLogout.setText("Logout");
        }


        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvUpdate = (TextView) pager.findViewById(R.id.tvUpData);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        flContent.addView(pager);
        return view;
    }

    @Override
    public void initData() {
        tvTitle.setText("Setting");
    }



    private void login(){
        String userStr = PrefUtils.getString(mActivity, AppConfig.USER, "");
        if (TextUtils.isEmpty(userStr)){// has not login
//            ivHead.setImageResource(R.mipmap.head);
//            tvName.setText(user.getName());
//            tvLogout.setText("Login");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(intent);
        }else{
            ivHead.setImageResource(R.mipmap.ic_launcher);
            tvName.setText("Login");
            tvLogout.setText("Login");
            PrefUtils.setString(mActivity, AppConfig.USER, "");
        }

    }
}
