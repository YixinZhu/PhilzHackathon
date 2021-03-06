package com.yixinzhu.philzmobile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yixinzhu.philzmobile.Network.JSLandingPageData;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;

public class MainPageFragment extends Fragment {
    @InjectView(R.id.backgroundImage) ImageView mImageView;
    @InjectView(R.id.quoteTextView) TextView mTextView;
    @InjectView(R.id.authorTextView) TextView mAuthorTextView;
    @InjectView(R.id.requestBaristaButton) Button mButton;

    public static MainPageFragment newInstance() {
        MainPageFragment mainPageFragment = new MainPageFragment();
        return mainPageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        ButterKnife.inject(this, rootView);
        NetworkClient.getInstance().getLandingPageData().subscribe(new Action1<JSLandingPageData>() {
            @Override
            public void call(final JSLandingPageData jsLandingPageData) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int index = new Random().nextInt(jsLandingPageData.quotes.length);
                        Uri uri = Uri.parse(jsLandingPageData.imageUrls[index]);
                        Picasso.with(getActivity()).load(uri).resize(mImageView.getWidth(), mImageView.getHeight())
                                .centerCrop().into(mImageView);
                        mTextView.setText(jsLandingPageData.quotes[index]);
                        mAuthorTextView.setText(jsLandingPageData.authors[index]);
                    }
                });
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), BaristaMatchActivity.class));
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(0);
    }
}
