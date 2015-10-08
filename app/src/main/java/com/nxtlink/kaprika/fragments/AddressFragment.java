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

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 17/09/15.
 */



public class AddressFragment extends Fragment {
    private static final String TAG = AddressFragment.class.getName();
    @InjectView(R.id.finish_register)
    Button finishRegister;
    @InjectView(R.id.address_street)
    EditText addressStreet;
    @InjectView(R.id.address_floor)
    EditText addressFloor;
    @InjectView(R.id.address_postal_code)
    EditText postalCode;
    @InjectView(R.id.adress_extra_info)
    EditText extraInfo;
    @InjectView(R.id.phone)
    EditText phone;

    @Inject
    KaprikaApiInterface api;
    @Inject
    KaprikaSharedPrefs prefs;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // callback check if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.address_fragment, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        finishRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fieldsAreCorrect()) {
                    //post to server data of user
                    UserInfo user = new UserInfo(prefs.getUserName(), prefs.getUserFbId(),
                            prefs.getUserEmail(), addressStreet.getText().toString(),
                            addressFloor.getText().toString(), extraInfo.getText().toString(),
                            postalCode.getText().toString(), phone.getText().toString());

                    api.postUserInfo(user, new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            Log.d(TAG, "User saved");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "User not saved " + error.getMessage());
                        }
                    });
                    getActivity().finish();
                }
            }
        });
    }

    private boolean fieldsAreCorrect() {
        if (addressStreet.length() < 1){
            addressStreet.setError(getString(R.string.error_street));
            return false;
        }
        if (addressFloor.length() < 1) {
            addressFloor.setError(getString(R.string.error_street));
            return false;
        }
        if (postalCode.length() < 5) {
            postalCode.setError(getString(R.string.postal_code_error));
            return false;
        }

        if (phone.length() < 9) {
            phone.setError(getString(R.string.error_phone));
            return false;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static AddressFragment newInstance() {
        AddressFragment myFragment = new AddressFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

}
