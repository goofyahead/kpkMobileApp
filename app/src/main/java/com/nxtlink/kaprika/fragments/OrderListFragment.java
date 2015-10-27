package com.nxtlink.kaprika.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.OrderListAdapter;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.Cart;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 10/26/15.
 */
public class OrderListFragment extends Fragment {

    private static final String TAG = OrderListFragment.class.getName();
    public static final int ACTIVE = 0;
    public static final int HISTORY = 1;
    public static final int FAV = 2;
    @InjectView(R.id.orderList)
    ListView orderList;
    @Inject
    KaprikaApiInterface api;
    @Inject
    KaprikaSharedPrefs prefs;

    private static final String STATUS = "STATUS";

    public static OrderListFragment newInstance(int status) {
        OrderListFragment myFragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(STATUS, status);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_list_fragment, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (getArguments().getInt(STATUS)){
            case 0:
                api.getClientActiveOrders(prefs.getUserFbId(), new Callback<LinkedList<Cart>>() {
                    @Override
                    public void success(LinkedList<Cart> carts, Response response) {
                        Log.d(TAG, "Orders from " + prefs.getUserFbId() + " last order status: " + carts.get(0).getStatus());
                        orderList.setAdapter(new OrderListAdapter(carts, getActivity()));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
                break;
            case 1:
                api.getClientHistoryOrders(prefs.getUserFbId(), new Callback<LinkedList<Cart>>() {
                    @Override
                    public void success(LinkedList<Cart> carts, Response response) {
                        Log.d(TAG, "Orders from " + prefs.getUserFbId() + " last order status: " + carts.get(0).getStatus());
                        orderList.setAdapter(new OrderListAdapter(carts, getActivity()));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
                break;
            case 2:
                api.getClientFavOrders(prefs.getUserFbId(), new Callback<LinkedList<Cart>>() {
                    @Override
                    public void success(LinkedList<Cart> carts, Response response) {
                        Log.d(TAG, "Orders from " + prefs.getUserFbId() + " last order status: " + carts.get(0).getStatus());
                        orderList.setAdapter(new OrderListAdapter(carts, getActivity()));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
                break;
        }

    }
}
