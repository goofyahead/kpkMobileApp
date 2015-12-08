package com.mgl.tpvkpk.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.printservice.PrintService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.base.TpvKpkApplication;
import com.mgl.tpvkpk.dialog.ProgressDialogFragment;
import com.mgl.tpvkpk.services.PrinterService;
import com.zj.btsdk.PrintPic;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.ApiHelper;
import kpklib.api.KaprikaApiInterface;
import kpklib.interfaces.DbUpdater;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DbUpdater {

    private static final String TAG = MainActivity.class.getName();
    private ProgressDialogFragment updateDialog;
    @InjectView(R.id.start_order)
    Button startOrder;
    @InjectView(R.id.phone_number)
    EditText phoneNumber;
    @Inject KaprikaApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PrinterService.class);
        startService(intent);

        ((TpvKpkApplication) getApplication()).inject(this);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        ApiHelper helper = new ApiHelper(this, MainActivity.this);
        helper.updateIfNecesary();

        phoneNumber.setText("");

        updateDialog = new ProgressDialogFragment();
        updateDialog.show(getFragmentManager(), "me");

        startOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().length() < 9 || !( phoneNumber.getText().toString().startsWith("6") || phoneNumber.getText().toString().startsWith("9"))) {
                    phoneNumber.setError(getString(R.string.phone_must_be_correct));
                } else {
                    api.getClientByPhone(phoneNumber.getText().toString(), new Callback<UserInfo>() {
                        @Override
                        public void success(UserInfo userInfo, Response response) {
                            if (userInfo == null) {
                                Log.d(TAG, "user not exist, create it");
                                Intent registerUser = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(registerUser);
                            } else {
                                Log.d(TAG, "user is: " + userInfo.getName());
                                Intent selectItems = new Intent(MainActivity.this, OrderActivity.class);
                                selectItems.putExtra(OrderActivity.USER_PAYLOAD, userInfo);
                                startActivity(selectItems);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "error retrieving user: " + error.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finished() {
        updateDialog.dismiss();
        Log.d(TAG, "Finished");
    }

    @Override
    public void setMax(int max) {
        updateDialog.setMax(max);
        Log.d(TAG, "Max is: " + max);
    }

    @Override
    public void error(String message) {
        Log.d(TAG, "Error: " + message);
    }

    @Override
    public void updateTo(int i) {
        updateDialog.updateProgress(i);
        Log.d(TAG, "status " + i);
    }

}
