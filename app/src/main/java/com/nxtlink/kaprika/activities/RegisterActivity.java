package com.nxtlink.kaprika.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.nxtlink.kaprika.fragments.AddressFragment;
import com.nxtlink.kaprika.fragments.FbLoginFragment;
import com.nxtlink.kaprika.interfaces.ScrollToNext;
import com.nxtlink.kaprika.pagerAdapters.RegistrationFragmentPagerAdapter;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements ScrollToNext {

    @InjectView(R.id.registration_pager)
    ViewPager pager;
    @InjectView(R.id.fb_login_button)
    LoginButton login;

    @Inject
    KaprikaSharedPrefs prefs;

    private String TAG = RegisterActivity.class.getName();
    private CallbackManager callbackManager;
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_register);

        ButterKnife.inject(this);
        ((KaprikaApplication) getApplication()).inject(this);

        pager.setVisibility(View.GONE);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nxtlink.kaprika", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage());
        }

        LinkedList<Fragment> registrationFragments = new LinkedList<>();
        final FbLoginFragment fbInfo = FbLoginFragment.newInstance();
        registrationFragments.add(fbInfo);
        AddressFragment addressFragment = AddressFragment.newInstance();
        registrationFragments.add(addressFragment);

        pager.setAdapter(new RegistrationFragmentPagerAdapter(this.getSupportFragmentManager(), registrationFragments));

        login.setReadPermissions("email");

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.getAccessToken().getUserId());
                prefs.setRegistered(true);
                prefs.setFbToken(loginResult.getAccessToken().getToken());
                prefs.setUserFbId(loginResult.getAccessToken().getUserId());

                GraphRequest request = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + loginResult.getAccessToken().getUserId(),
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */
                                try {
                                    String email = response.getJSONObject().getString("email");
                                    String name = response.getJSONObject().getString("name");
                                    fbInfo.setFbParams(email, name);
                                    pager.setVisibility(View.VISIBLE);
                                    login.setVisibility(View.GONE);
                                    Log.d(TAG, "response: " + response.getJSONObject().getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name");
                request.setParameters(parameters);
                request.executeAsync();
                // update picture, name, etc and button next fragment rolling
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "login cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void scrollToNext() {
        pager.setCurrentItem(++current, true);
    }
}
