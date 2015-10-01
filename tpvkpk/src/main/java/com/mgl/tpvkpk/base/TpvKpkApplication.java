package com.mgl.tpvkpk.base;

import android.app.Application;
import android.content.Context;

import com.mgl.tpvkpk.modules.AndroidModule;
import com.mgl.tpvkpk.modules.CustomModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import kpklib.interfaces.Injector;

/**
 * Created by goofyahead on 10/1/15.
 */
public class TpvKpkApplication extends Application implements Injector{
    private ObjectGraph graph;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        graph = ObjectGraph.create(getModules().toArray());
    }

    public static Context getAppContext() {
        return mContext;
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new CustomModule()// you can add more modules here
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return graph;
    }
}
