package com.nxtlink.kaprika.modules;

/**
 * Created by goofyahead on 9/8/14.
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nxtlink.kaprika.activities.MainActivity;
import com.nxtlink.kaprika.db.DataHelper;
import com.nxtlink.kaprika.interfaces.KaprikaApiInterface;
import com.nxtlink.kaprika.base.Credentials;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;
import com.nxtlink.kaprika.volley.BitmapLruCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(injects = {
        MainActivity.class
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
    RequestQueue provideQueue() {
        return Volley.newRequestQueue(mContext);
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(RequestQueue mRequestQueue) {
        return new ImageLoader(mRequestQueue, new BitmapLruCache(50));
    }

    @Provides
    @Singleton
    KaprikaSharedPrefs provideSharedPrefs() {
        return new KaprikaSharedPrefs(mContext);
    }

    @Provides
    @Singleton
    DataHelper provideDataHelper () {
        return new DataHelper(mContext);
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