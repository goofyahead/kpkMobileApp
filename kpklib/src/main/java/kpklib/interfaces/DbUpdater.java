package kpklib.interfaces;

/**
 * Created by goofyahead on 10/1/15.
 */
public interface DbUpdater {
    void finished();
    void setMax(int max);
    void error(String message);
    void updateTo(int current);
}
