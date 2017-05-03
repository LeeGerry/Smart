package edu.auburn.smart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import edu.auburn.smart.R;
import edu.auburn.smart.config.AppConfig;
import edu.auburn.smart.model.User;
import edu.auburn.smart.model.UserResult;
import edu.auburn.smart.utils.PrefUtils;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @ViewInject(R.id.input_name)
    EditText etName;
    @ViewInject(R.id.input_address)
    EditText etAddress;
    @ViewInject(R.id.input_email)
    EditText etEmail;
    @ViewInject(R.id.input_mobile)
    EditText etMobile;
    @ViewInject(R.id.input_password)
    EditText etPwd;
    @ViewInject(R.id.input_reEnterPassword)
    EditText etRePwd;
    @ViewInject(R.id.btn_signup)
    Button btnSignup;
    @ViewInject(R.id.link_login)
    TextView tvLogin;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        x.view().inject(this);
        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);


        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPwd.getText().toString();
        String reEnterPassword = etRePwd.getText().toString();
        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setEmail(email);
        user.setTelephone(Long.parseLong(mobile));
        user.setPwd(password);
        // TODO: Implement your own signup logic here.

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

                */
        getDataFromServer(user);
    }
    private UserResult userResult;
    private void getDataFromServer(User user) {
        RequestParams params = new RequestParams(AppConfig.API_REGISTER);
        String user_json = new Gson().toJson(user);

        params.setAsJsonContent(true);
        params.setBodyContent(user_json);

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
                Toast.makeText(SignupActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
                String message = userResult.getMessage();
                if (message.equals("success")){
                    onSignupSuccess();
                }else if (message.equals("exist")){
                    Toast.makeText(getBaseContext(), "user name exist", Toast.LENGTH_SHORT).show();
                    btnSignup.setEnabled(true);
                }else{
                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    btnSignup.setEnabled(true);
                }
            }
        });
    }

    public void onSignupSuccess() {
//        btnSignup.setEnabled(true);
//        setResult(RESULT_OK, null);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
        Toast.makeText(getBaseContext(), "success", Toast.LENGTH_SHORT).show();
        // 1. setting user in xml
        PrefUtils.setString(SignupActivity.this, AppConfig.USER,new Gson().toJson(userResult.getUser()));
        // 2. open main activity
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        SignupActivity.this.finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPwd.getText().toString();
        String reEnterPassword = etRePwd.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etName.setError("at least 3 characters");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (address.isEmpty()) {
            etAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            etAddress.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            etMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            etMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPwd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPwd.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            etRePwd.setError("Password Do not match");
            valid = false;
        } else {
            etRePwd.setError(null);
        }

        return valid;
    }
}