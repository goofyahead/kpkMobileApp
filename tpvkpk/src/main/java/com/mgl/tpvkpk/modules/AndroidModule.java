package com.mgl.tpvkpk.modules;

/**
 * Created by goofyahead on 9/8/14.
 */

import android.app.DownloadManager;
import android.content.Context;

import com.mgl.tpvkpk.activities.CheckoutActivity;
import com.mgl.tpvkpk.activities.MainActivity;
import com.mgl.tpvkpk.activities.OrderActivity;
import com.mgl.tpvkpk.activities.RegisterActivity;
import com.mgl.tpvkpk.adapters.DishCursorAdapter;
import com.mgl.tpvkpk.dialog.SelectQuantityDialog;
import com.mgl.tpvkpk.fragments.DishListViewFragment;
import com.mgl.tpvkpk.services.PrinterService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kpklib.api.ApiHelper;
import kpklib.api.KaprikaApiInterface;
import kpklib.base.KpkLibPrefs;
import kpklib.constants.Credentials;
import kpklib.db.DataHelper;
import retrofit.RestAdapter;

@Module(injects = {
        MainActivity.class,
        DataHelper.class,
        ApiHelper.class,
        PrinterService.class,
        RegisterActivity.class,
        CheckoutActivity.class,
        OrderActivity.class,
        DishCursorAdapter.class,
        DishListViewFragment.class,
        SelectQuantityDialog.class
},
        library = true)
public class AndroidModule {
    private final Context mContext;

    public AndroidModule(Context context) {
        this.mContext = context;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mContext;
    }

    @Provides
    @Singleton
    KpkLibPrefs provideSharedPrefs() {
        return new KpkLibPrefs(mContext);
    }


    @Provides
    @Singleton
    DataHelper provideDataHelper () {
        return new DataHelper(mContext);
    }

    @Provides
    @Singleton
    DownloadManager provideDownloadManager() {
        return (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(Credentials.SERVER_IP)
                .build();
    }

    @Provides
    @Singleton
    KaprikaApiInterface provideApi() {
        return provideRestAdapter().create(KaprikaApiInterface.class);
    }
}