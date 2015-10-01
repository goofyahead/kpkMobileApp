package kpklib.interfaces;

import dagger.ObjectGraph;

/**
 * Created by goofyahead on 10/1/15.
 */
public interface Injector {
    void inject(Object object);
    public ObjectGraph getObjectGraph();
}
