package edu.auburn.smart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BaseActivity;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.utils.PrefUtils;

public class PreActivity extends BaseActivity {
    private static final String TAG = "PreActivity";
    @ViewInject(R.id.btnDone)
    Button btnDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);
        x.view().inject(this);

    }
    @Event(value = R.id.btnDone, type = View.OnClickListener.class)
    private void done(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        PrefUtils.setBoolean(PreActivity.this, AppConfig.IS_FIRST_ENTER, false);
        this.finish();
    }
}
