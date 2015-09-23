package com.nxtlink.kaprika.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.ScrollToNext;
import com.nxtlink.kaprika.picassoTransformers.CircleTransform;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FbLoginFragment extends Fragment {

    @Inject
    KaprikaSharedPrefs prefs;

    @InjectView(R.id.fb_user_email)
    EditText userEmail;
    @InjectView(R.id.fb_user_name)
    EditText userName;
    @InjectView(R.id.fb_image_profile)
    ImageView profileImage;
    @InjectView(R.id.continue_button)
    Button continueRegister;

    private String TAG = FbLoginFragment.class.getName();
    private String currentUserEmail;
    private String currentUserName;
    private ScrollToNext callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // callback check if needed
        callback = (ScrollToNext) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fb_login, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        prefs.setUserName(currentUserName);
        prefs.setUserEmail(currentUserEmail);

        Picasso.with(getActivity()).load("https://graph.facebook.com/" + prefs.getUserFbId()+ "/picture?type=large").transform(new CircleTransform()).into(profileImage);
        userName.setText(currentUserName);
        userEmail.setText(currentUserEmail);

        continueRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.scrollToNext();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static FbLoginFragment newInstance() {
        FbLoginFragment myFragment = new FbLoginFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    public void setFbParams(String email, String name ) {
        currentUserEmail = email;
        currentUserName = name;
    }
}
