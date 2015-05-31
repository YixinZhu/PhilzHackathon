package com.yixinzhu.philzmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaristaMatchActivity extends Activity {

    @InjectView(R.id.baristaImg) ImageView mImageView;
    @InjectView(R.id.baristaText) TextView mTextView;
    @InjectView(R.id.orderEditText) EditText mEditText;
    @InjectView(R.id.submitOrder) Button mButton;
    @InjectView(R.id.ratingBar) RatingBar mRatingBar;

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private boolean orderSubmited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barista_match);
        mContext = this;
        ButterKnife.inject(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Requesting your personal Barista");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doneWaiting();
            }
        }, 5000);

        MainActivity.mfireBaseRef.child("orderRequest/pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue().equals(false) && orderSubmited) {
                    mEditText.setVisibility(View.GONE);
                    mRatingBar.setVisibility(View.VISIBLE);
                    mTextView.setText("Please rate Megan!");
                    mButton.setText("Submit Rating");
                    mButton.setEnabled(true);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(new Intent(BaristaMatchActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mfireBaseRef.child("orderRequest/order").setValue(mEditText.getText().toString());
                MainActivity.mfireBaseRef.child("orderRequest/pending").setValue(true);
                mButton.setEnabled(false);
                orderSubmited = true;
            }
        });
    }

    private void doneWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                Picasso.with(BaristaMatchActivity.this).load(R.drawable.barista).into(mImageView);
                mTextView.setText("Megan");
            }
        });
    }
}
