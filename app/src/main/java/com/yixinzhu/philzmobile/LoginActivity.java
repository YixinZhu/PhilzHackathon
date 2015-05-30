package com.yixinzhu.philzmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity {
    @InjectView(R.id.login_button) LoginButton mLoginButton;

    CallbackManager mCallbackManager;
    AlertDialog mErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initializeFacebook();
    }

    private void initializeFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences.Editor mSharePrefs =
                        getSharedPreferences(MainActivity.PHILZ_PREFS, MODE_PRIVATE).edit();

                mSharePrefs.putString(MainActivity.FB_ACCESS_TOKEN, loginResult.getAccessToken().getToken()).apply();
                mSharePrefs.putString(MainActivity.FB_USER_ID, loginResult.getAccessToken().getUserId()).apply();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                showErrorDialog(e);
            }
        });
    }

    private void showErrorDialog(Exception e) {
        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
        }
        mErrorDialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.getLocalizedMessage())
                .setNegativeButton("Ok", null)
                .show();
    }
}
