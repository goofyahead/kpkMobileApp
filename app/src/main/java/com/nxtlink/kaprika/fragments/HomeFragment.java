package com.nxtlink.kaprika.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.LoadCart;
import com.nxtlink.kaprika.pagerAdapters.CarrouselAdapter;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.LinkedList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.db.DataHelper;
import kpklib.models.Cart;
import kpklib.models.Dish;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getName();
    @InjectView(R.id.featured_slider)
    ViewPager featured;
    @Inject
    DataHelper dbHelper;
    @Inject
    KaprikaApiInterface api;
    @Inject
    KaprikaSharedPrefs prefs;
    @InjectView(R.id.last_order_date)
    TextView lastOrderDate;
    @InjectView(R.id.last_order_first_line)
    TextView lastOrderFirstLine;
    @InjectView(R.id.separator_last)
    View separatorLastView;
    @InjectView(R.id.repeat_last_order)
    LinearLayout repeatLastOrder;

    private LinkedList<Dish> featuredDishes = new LinkedList<>();
    private Cart mLastCartOrder;
    private LoadCart mCallback;
    private Callback<LinkedList<Cart>> mApiCallback;
    private Handler handler = new Handler();
    private Runnable nextSlide;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

         mCallback = (LoadCart) activity;
        // callback check if needed
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        featuredDishes = dbHelper.getDemoDishes();
        featured.setAdapter(new CarrouselAdapter(featuredDishes, getActivity()));

        nextSlide = new Runnable() {
            @Override
            public void run() {
                int position = featured.getCurrentItem() == featured.getAdapter().getCount() - 1 ?  0 : featured.getCurrentItem() + 1;
                featured.setCurrentItem( position, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(nextSlide, 3000);

        repeatLastOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.loadCart(mLastCartOrder);
            }
        });


            if (prefs.getUserFbId().length() > 1) {
                mApiCallback = new Callback<LinkedList<Cart>>() {
                    @Override
                    public void success(LinkedList<Cart> carts, Response response) {
                        if (carts != null && carts.size() > 0) {
                            if (isAdded()) {
                                mLastCartOrder = carts.get(0);
                                DateTime date = new DateTime(Long.parseLong(carts.get(0).getTimestamp()));
                                DateTimeFormatter day = DateTimeFormat.forPattern("d MMMM HH:mm").withLocale(Locale.getDefault());
                                lastOrderDate.setText(day.print(date));
                                lastOrderFirstLine.setText(String.format(getResources().getString(R.string.order_line), mLastCartOrder.getItem(0).getQuantity(), mLastCartOrder.getItem(0).getItem().getName()));
                            }
                        } else {
                            lastOrderFirstLine.setText(getActivity().getString(R.string.here_will_be_last_orders));

                            repeatLastOrder.setVisibility(View.GONE);
                            separatorLastView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error retrieving last orders" + error);
                        lastOrderFirstLine.setText(getActivity().getString(R.string.here_will_be_last_orders));

                        repeatLastOrder.setVisibility(View.GONE);
                        separatorLastView.setVisibility(View.GONE);
                    }
                };

                api.getClientHistoryOrders(prefs.getUserFbId(), mApiCallback);

            } else {
                lastOrderFirstLine.setText(getActivity().getString(R.string.here_will_be_last_orders));

                repeatLastOrder.setVisibility(View.GONE);
                separatorLastView.setVisibility(View.GONE);
            }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        handler.removeCallbacks(nextSlide);
    }


}
