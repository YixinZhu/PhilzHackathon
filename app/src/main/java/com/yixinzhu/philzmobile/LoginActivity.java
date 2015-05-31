package com.yixinzhu.philzmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.yixinzhu.philzmobile.Network.JSLandingPageData;
import com.yixinzhu.philzmobile.Network.JSLoginData;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;

public class LoginActivity extends Activity {
    @InjectView(R.id.login_button) LoginButton mLoginButton;
    @InjectView(R.id.loginBackground) ImageView mImageView;
    @InjectView(R.id.quoteTextView) TextView mQuoteTextView;
    @InjectView(R.id.authorTextView) TextView mAuthorTextView;

    CallbackManager mCallbackManager;
    AlertDialog mErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initializeFacebook();
        initializeBackground();
    }

    private void initializeBackground() {
        NetworkClient.getInstance().getLoginData().subscribe(new Action1<JSLoginData>() {
            @Override
            public void call(final JSLoginData jsLoginData) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = Uri.parse(jsLoginData.imageUrl);
                        Picasso.with(LoginActivity.this).load(uri).resize(mImageView.getWidth(), mImageView.getHeight())
                                .centerCrop().into(mImageView);
                        mQuoteTextView.setText(jsLoginData.quote);
                        mAuthorTextView.setText(jsLoginData.author);
                    }
                });
            }
        });
    }

    private void initializeFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences.Editor mSharePrefs =
                        getSharedPreferences(MainActivity.PHILZ_PREFS, MODE_PRIVATE).edit();

                mSharePrefs.putString(MainActivity.FB_ACCESS_TOKEN, loginResult.getAccessToken().getToken()).apply();
                mSharePrefs.putString(MainActivity.FB_USER_ID, loginResult.getAccessToken().getUserId()).apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
