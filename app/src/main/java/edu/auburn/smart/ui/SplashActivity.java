package edu.auburn.smart.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import edu.auburn.smart.R;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.utils.PrefUtils;

public class SplashActivity extends Activity {
    public final static int MAIN_ACTIVITY = 0;
    private final static int LOGIN_ACTIVITY = 1;
    private final static int PRE_ACTIVITY = 2;
    @ViewInject(R.id.rl_root)
    private RelativeLayout rlRoot;
    private Handler mHandler = new Handler(){
        Intent intent;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MAIN_ACTIVITY:
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    break;
                case LOGIN_ACTIVITY:
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    break;
                case PRE_ACTIVITY:
                    intent = new Intent(SplashActivity.this, PreActivity.class);
                    break;
            }

            startActivity(intent);
            SplashActivity.this.finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        x.view().inject(this);
        showApp();
        /*
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        sa.setDuration(1000);
        sa.setFillAfter(true);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotateAnimation);
        set.addAnimation(sa);
        set.addAnimation(aa);
        rlRoot.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean is_first = PrefUtils.getBoolean(SplashActivity.this, AppConfig.IS_FIRST_ENTER, true);
                Intent intent;
                if (is_first) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        */
    }
    private void showApp(){
        if (mHandler != null){
            boolean is_first = PrefUtils.getBoolean(SplashActivity.this, AppConfig.IS_FIRST_ENTER, true);
            if (is_first)//first time
                mHandler.sendEmptyMessageDelayed(PRE_ACTIVITY, 2500);
            else{
                String user = PrefUtils.getString(SplashActivity.this, AppConfig.USER, "");
                if (!TextUtils.isEmpty(user)){//has token
                    mHandler.sendEmptyMessageDelayed(MAIN_ACTIVITY, 2500);
                }else{// need login
                    mHandler.sendEmptyMessageDelayed(LOGIN_ACTIVITY, 2500);
                }
            }
        }
    }
}
