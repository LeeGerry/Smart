package edu.auburn.smart.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import edu.auburn.smart.R;
import edu.auburn.smart.base.BaseActivity;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.User;
import edu.auburn.smart.model.UserResult;
import edu.auburn.smart.utils.PrefUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @ViewInject(R.id.input_email) EditText etEmail;
    @ViewInject(R.id.input_password) EditText etPwd;
    @ViewInject(R.id.btn_login) Button btnLogin;
    @ViewInject(R.id.link_signup) TextView tvSignup;
    @ViewInject(R.id.svRoot)
    ScrollView svRoot;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        svRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void login() {
        View v = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        if (!progressDialog.isShowing())
            progressDialog.show();

        String email = etEmail.getText().toString();
        String password = etPwd.getText().toString();

        getDataFromServer(email, password);
    }




    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        PrefUtils.setString(LoginActivity.this, AppConfig.USER,new Gson().toJson(userResult.getUser()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = etEmail.getText().toString();
        String password = etPwd.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etEmail.setError("at least 3 characters");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPwd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPwd.setError(null);
        }

        return valid;
    }

    private UserResult userResult;
    private void getDataFromServer(String user, String pwd) {
        RequestParams params = new RequestParams(AppConfig.API_LOGIN);
        User u = new User();
        u.setName(user);
        u.setPwd(pwd);
        String user_json = new Gson().toJson(u);

        params.setAsJsonContent(true);
        params.setBodyContent(user_json);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                userResult = new Gson().fromJson(result, UserResult.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (progressDialog!=null)
                    progressDialog.hide();
                if (userResult == null || TextUtils.isEmpty(userResult.getMessage()) || "success".equals(userResult.getMessage())){
                    onLoginSuccess();
                }else{
                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
                btnLogin.setEnabled(true);
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
