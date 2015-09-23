package com.nxtlink.kaprika.activities;

import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.MenuAdapter;
import com.nxtlink.kaprika.api.KaprikaApiInterface;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.db.DataHelper;
import com.nxtlink.kaprika.dialogs.ProgressDialogFragment;
import com.nxtlink.kaprika.dialogs.SelectQuantityDialog;
import com.nxtlink.kaprika.fragments.DishListViewFragment;
import com.nxtlink.kaprika.fragments.DishViewFragment;
import com.nxtlink.kaprika.interfaces.AddToCart;
import com.nxtlink.kaprika.interfaces.SelectQuantityInterface;
import com.nxtlink.kaprika.models.AccessToken;
import com.nxtlink.kaprika.models.Cart;
import com.nxtlink.kaprika.models.Category;
import com.nxtlink.kaprika.models.Dish;
import com.nxtlink.kaprika.models.MenuCategory;
import com.nxtlink.kaprika.picassoTransformers.CircleTransform;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<Integer>, DishListViewFragment.OnDishSelectedListener, AddToCart, SelectQuantityInterface {

    private static String TAG = MainActivity.class.getName();

    @Inject
    KaprikaApiInterface api;
    @Inject
    KaprikaSharedPrefs prefs;
    @Inject
    DataHelper dataHelper;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.listView_categories)
    ListView drawer;
    @InjectView(R.id.left_drawer)
    RelativeLayout drawerHolder;
    @InjectView(R.id.profile_pic)
    ImageView profilePic;

    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<MenuCategory> options = new ArrayList<>();
    private ProgressDialogFragment progress;
    private BroadcastReceiver receiver;
    private TextView orderCount;
    private View cartView;
    private Dish currentDishSelected;
    private Cart currentCart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.inject(this);
        ((KaprikaApplication) getApplication()).inject(this);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.isRegistered()){
                    // show my profile
                    Intent registration = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registration);
                } else {
                    Intent registration = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registration);
                }
            }
        });

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryId = ((MenuCategory) drawer.getAdapter().getItem(position)).getId();
                mTitle = ((MenuCategory) drawer.getAdapter().getItem(position)).getCategoryName();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DishListViewFragment dlView = DishListViewFragment.newInstance(categoryId);
                ft.replace(R.id.fragment_holder, dlView);
                ft.commit();

                drawer.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerHolder);

                getSupportActionBar().setTitle(mTitle);
            }
        });


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.d(TAG, "A file was downloaded");
                    progress.incrementProgressBy(1);
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        cartView = menu.findItem(R.id.action_cart).getActionView();
        orderCount = (TextView) cartView.findViewById(R.id.cart_count);

        updateCartCounter();

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.isRegistered()) {
                    Log.d(TAG, "clicked in click handler");
                    Intent checkout = new Intent(MainActivity.this, CheckoutActivity.class);
                    checkout.putExtra(CheckoutActivity.PAYLOAD, currentCart);
                    startActivityForResult(checkout, 0);
                } else {
                    Intent registerActivity = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerActivity);
                }
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentCart = (Cart) data.getSerializableExtra(CheckoutActivity.PAYLOAD);
        updateCartCounter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(TAG, "Something clicked in action bar " + id);

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

    @Override
    public void success(final Integer timeStampFromServer, Response response) {
        Log.d(TAG, "last update was in " + timeStampFromServer);

        if (prefs.getLastUpdate() < timeStampFromServer) {
            progress = new ProgressDialogFragment();
            progress.show(getFragmentManager(), "me");

            Log.d(TAG, "Must update dB");
            dataHelper.deleteDB();

            api.getCurrentMenu(new Callback<List<Dish>>() {
                @Override
                public void success(List<Dish> dishes, Response response) {
                    Log.d(TAG, "saving dishes in db " + dishes.size());
                    progress.setMax(dishes.size() * 2);

                    for (Dish dish : dishes) {
                        Log.d(TAG, "SAVING: " + dish.getName());
                        dataHelper.saveDish(dish);
                        Log.d(TAG, "Dishes saved, getting images");
                    }

                    Log.d(TAG, "done mark as updated now");
                    prefs.setLastUpdated(timeStampFromServer);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Error " + error.getMessage());
                }
            });

            api.getCategories(new Callback<List<Category>>() {
                @Override
                public void success(List<Category> categories, Response response) {
                    for (Category category : categories) {
                        dataHelper.saveCategory(category);
                    }

                    for (Category category : dataHelper.getCategories()){
                        options.add(new MenuCategory(category.getName(), 0, category.getId()));
                    }

                    MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, options);
                    drawer.setAdapter(menuAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });

        } else {
            Log.d(TAG, "DB is up to date");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        api.getLastUpdate(this);

        options.clear();

        for (Category category : dataHelper.getCategories()){
            options.add(new MenuCategory(category.getName(), 0, category.getId()));
        }

        MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, options);
        drawer.setAdapter(menuAdapter);

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if(prefs.isRegistered()) {
            Picasso.with(this).load("https://graph.facebook.com/" + prefs.getUserFbId()+ "/picture?type=large").transform(new CircleTransform()).into(profilePic);
        } else {
            Picasso.with(this).load(R.drawable.default_profile).transform(new CircleTransform()).into(profilePic);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void failure(RetrofitError error) {

    }

    @Override
    public void onDishSelected(String id) {
        Log.d(TAG, "dish selected: " + id);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DishViewFragment dlView = DishViewFragment.newInstance(id);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.fragment_holder, dlView);
        ft.addToBackStack(DishViewFragment.class.getName());
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onDishAdded(Dish dish) {
        Log.d(TAG, "dish added " + dish.getName());
        currentDishSelected = dish;
        new SelectQuantityDialog().show(getFragmentManager(), "qtty");
    }

    private void updateCartCounter() {
        if (currentCart.getItemsCount() == 0) {
            orderCount.setVisibility(View.INVISIBLE);
        } else {
            orderCount.setVisibility(View.VISIBLE);
            orderCount.setText("" + currentCart.getItemsCount());
        }
    }

    @Override
    public void quantitySelected(int quantity) {
        if (quantity == 0) {
            currentDishSelected = null;
        } else {
            //add to order not just the number
            currentCart.addItemToCart(currentDishSelected, quantity);
            updateCartCounter();
        }
    }
}
