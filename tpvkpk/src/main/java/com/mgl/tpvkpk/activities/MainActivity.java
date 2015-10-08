package com.mgl.tpvkpk.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.base.TpvKpkApplication;
import com.zj.btsdk.PrintPic;

import javax.inject.Inject;

import kpklib.api.ApiHelper;
import kpklib.api.KaprikaApiInterface;
import kpklib.interfaces.DbUpdater;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DbUpdater {

    private static final String TAG = MainActivity.class.getName();

    @Inject
    KaprikaApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TpvKpkApplication) getApplication()).inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ApiHelper helper = new ApiHelper(this, MainActivity.this);
        helper.updateIfNecesary();


    }

    @Override
    protected void onResume() {
        super.onResume();


        PrintPic pic = new PrintPic();

        // Get a set of currently paired devices

        // if No printers are connected or need to connect more
//        Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);      //��������һ����Ļ
//        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
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
        Log.d(TAG, "Finished");
    }

    @Override
    public void setMax(int max) {
        Log.d(TAG, "Max is: " + max);
    }

    @Override
    public void error(String message) {
        Log.d(TAG, "Error: " + message);
    }

    @Override
    public void updateTo(int i) {
        Log.d(TAG, "status " + i);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_ENABLE_BT:      //���������
//                if (resultCode == Activity.RESULT_OK) {   //�����Ѿ���
//                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
//                } else {                 //�û������������
//                    finish();
//                }
//                break;
//            case  REQUEST_CONNECT_DEVICE:     //��������ĳһ�����豸
//                if (resultCode == Activity.RESULT_OK) {   //�ѵ�������б��е�ĳ���豸��
//                    String address = data.getExtras()
//                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //��ȡ�б������豸��mac��ַ
//                    con_dev = mService.getDevByMac(address);
//
//                    mService.connect(con_dev);
//                }
//                break;
//        }
//    }


}
