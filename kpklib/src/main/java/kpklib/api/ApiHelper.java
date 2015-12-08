package kpklib.api;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import kpklib.base.KpkLibPrefs;
import kpklib.db.DataHelper;
import kpklib.interfaces.DbUpdater;
import kpklib.interfaces.Injector;
import kpklib.models.Category;
import kpklib.models.Dish;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 10/1/15.
 */
public class ApiHelper {
    private BroadcastReceiver receiver;
    private final DbUpdater cb;
    @Inject
    KaprikaApiInterface api;
    @Inject
    DataHelper dataHelper;
    @Inject
    KpkLibPrefs prefs;

    private String TAG = ApiHelper.class.getName();
    private int current = 1;
    private int max;

    public ApiHelper(final DbUpdater cb, Context context) {

        ((Injector) context.getApplicationContext()).inject(this);

        this.cb = cb;

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.d(TAG, "A file was downloaded");
                    if (current < max) {
                        cb.updateTo(current);
                        current++;
                    } else {
                        Log.d(TAG, "max reached, calling finished");
                        cb.finished();
                        context.unregisterReceiver(receiver);
                    }

                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void updateIfNecesary () {
        api.getLastUpdate(new Callback<Integer>() {
            @Override
            public void success(final Integer timeStampFromServer, Response response) {
                if (prefs.getLastUpdate() < timeStampFromServer) {

                    Log.d(TAG, "Must update dB");
                    dataHelper.deleteDB();

                    api.getCurrentMenu(new Callback<List<Dish>>() {
                        @Override
                        public void success(List<Dish> dishes, Response response) {
                            Log.d(TAG, "saving dishes in db " + dishes.size());
                            max = dishes.size();
                            cb.setMax(max);

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
                            cb.error(error.getMessage());
                        }
                    });

                    api.getCategories(new Callback<List<Category>>() {
                        @Override
                        public void success(List<Category> categories, Response response) {
                            for (Category category : categories) {
                                dataHelper.saveCategory(category);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, error.getMessage());
                        }
                    });

                } else {
                    cb.finished();
                    Log.d(TAG, "DB is up to date");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                cb.error(error.getMessage());
            }
        });

    }
}
