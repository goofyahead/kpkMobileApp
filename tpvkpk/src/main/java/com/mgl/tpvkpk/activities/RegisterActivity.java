package com.mgl.tpvkpk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.base.TpvKpkApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.Cart;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 11/3/15.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String PHONE_NUMBER = RegisterActivity.class.getName();
    private static final String TAG = RegisterActivity.class.getName();

    @InjectView(R.id.client_name)
    EditText clientName;
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
    @InjectView(R.id.client_email)
    EditText email;
    @Inject
    KaprikaApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        ButterKnife.inject(this);
        ((TpvKpkApplication) getApplication()).inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        phone.setText(getIntent().getStringExtra(PHONE_NUMBER));

        finishRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserInfo user = new UserInfo(clientName.getText().toString(), "",  email.getText().toString(), addressStreet.getText().toString(), addressFloor.getText().toString(),
                        extraInfo.getText().toString(), postalCode.getText().toString(), phone.getText().toString(), "");
                api.postUserInfo(user, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d(TAG, "success " + s);
                        // load order with payload, finish this activity
                        Intent selectItems = new Intent(RegisterActivity.this, OrderActivity.class);
                        selectItems.putExtra(OrderActivity.USER_PAYLOAD, user);
                        startActivity(selectItems);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "error " + error.getMessage());

                    }
                });
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
}
