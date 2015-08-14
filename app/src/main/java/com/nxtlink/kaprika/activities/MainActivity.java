package com.nxtlink.kaprika.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.MenuAdapter;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.KaprikaApiInterface;
import com.nxtlink.kaprika.models.Category;
import com.nxtlink.kaprika.models.MenuCategory;
import com.nxtlink.kaprika.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    @Inject
    KaprikaApiInterface api;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.listView_categories)
    ListView drawer;
    @InjectView(R.id.version_app)
    TextView versionApp;
    @InjectView(R.id.left_drawer)
    RelativeLayout drawerHolder;

    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<MenuCategory> options = new ArrayList<>();
    private List<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.inject(this);
        ((KaprikaApplication) getApplication()).inject(this);

        versionApp.setText(Utils.getPrettyAppVersion(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mTitle);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        mTitle = getResources().getString(R.string.categories);
        getSupportActionBar().setTitle(mTitle);

        api.getCategories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                mCategories = categories;

                for (Category category : categories) {
                    options.add(new MenuCategory(category.getName(), 0));
                }

                MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, options);
                drawer.setAdapter(menuAdapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                // all calls to the list are the elements and must be scrollable user opts should be a button
                // call listDishes with the id retrieved from position
                mCategories.get(position).getId();
//                switch (position) {
//                    case 0:
//                        mTitle = getResources().getString(R.string.create_nowfie);
//                        TakeSelfie selfieFragment = TakeSelfie.getInstance(null);
//                        ft.replace(R.id.fragment_holder, selfieFragment);
//                        ft.commit();
//                        break;
//                    case 1:
//                        mTitle = getResources().getString(R.string.my_active_nowfies);
//                        MyNowfies nowfiesListOpen = MyNowfies.getInstance(true);
//                        ft.replace(R.id.fragment_holder, nowfiesListOpen);
//                        ft.commit();
//                        break;
//                    case 2:
//                        mTitle = getResources().getString(R.string.my_nowfies);
//                        MyNowfies nowfiesList = MyNowfies.getInstance(false);
//                        ft.replace(R.id.fragment_holder, nowfiesList);
//                        ft.commit();
//                        break;
//                }

                drawer.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerHolder);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
