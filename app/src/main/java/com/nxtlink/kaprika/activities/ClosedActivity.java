package com.nxtlink.kaprika.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.Open;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClosedActivity extends AppCompatActivity {

    @InjectView(R.id.schedule)
    TextView schedule;

    @Inject
    KaprikaApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed);

        ButterKnife.inject(this);
        ((KaprikaApplication) getApplication()).inject(this);

        api.isRestaurantOpen(new Callback<Open>() {
            @Override
            public void success(Open open, Response response) {
                schedule.setVisibility(View.VISIBLE);
                schedule.setText(String.format(getString(R.string.schedule), open.getMorningOpen(), open.getMorningClose(), open.getAfternoonOpen(), open.getAfternoonClose()));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

}
