package com.mgl.tpvkpk.modules;

/**
 * Created by goofyahead on 9/8/14.
 */

import android.app.DownloadManager;
import android.content.Context;

import com.mgl.tpvkpk.activities.MainActivity;

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
        ApiHelper.class
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

//    @Provides
//    @Singleton
//    RequestQueue provideQueue() {
//        return Volley.newRequestQueue(mContext);
//    }
//
//    @Provides
//    @Singleton
//    ImageLoader provideImageLoader(RequestQueue mRequestQueue) {
//        return new ImageLoader(mRequestQueue, new BitmapLruCache(50));
//    }
//
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