package com.yixinzhu.philzmobile;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yixinzhu.philzmobile.Network.JSLandingPageData;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;

public class MainPageFragment extends Fragment {
    @InjectView(R.id.backgroundImage) ImageView mImageView;
    @InjectView(R.id.quoteTextView) TextView mTextView;

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
                        Uri uri = Uri.parse(jsLandingPageData.imageUrl);
                        Picasso.with(getActivity()).load(uri).resize(rootView.getWidth(), rootView.getHeight())
                                .centerCrop().into(mImageView);
                        mTextView.setText(jsLandingPageData.quote);
                    }
                });
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
