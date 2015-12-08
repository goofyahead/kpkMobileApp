package com.mgl.tpvkpk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.adapters.CartViewAdapter;
import com.mgl.tpvkpk.base.TpvKpkApplication;
import com.mgl.tpvkpk.interfaces.CartUpdated;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.AccessToken;
import kpklib.models.Cart;
import kpklib.models.OrderFromKpk;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutActivity extends AppCompatActivity implements CartUpdated {

    public static final String PAYLOAD = "PAYLOAD";
    public static final String TOKEN = "TOKEN";
    public static final String DELIVERY = "DELIVERY";
    public static final String PICK_UP = "PICK_UP";
    private static final int RESULT_BACK_TO_BUY = 12345;
    private static final float MINIMUM_ORDER = 12;
    private static final String TAG = CheckoutActivity.class.getName();
    private static final int REQUEST_CODE = 112;
    private Cart mCart;
    private String deliveryOption;



    @InjectView(R.id.cart_item_list)
    ListView cartListView;
    @InjectView(R.id.checkout_buy)
    Button checkoutBuy;
    @InjectView(R.id.checkout_discard)
    Button checkoutDiscard;
    @InjectView(R.id.checkout_price_sum)
    TextView cartSum;
    @InjectView(R.id.pick_up_button)
    Button pickUp;
    @InjectView(R.id.take_away_button)
    Button takeAway;
    @InjectView(R.id.alert_view)
    RelativeLayout alertView;
    @InjectView(R.id.minimun_order)
    TextView minimumOrder;

    @Inject
    KaprikaApiInterface api;

    private String token;
    private UserInfo mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ButterKnife.inject(this);
        ((TpvKpkApplication) getApplication()).inject(this);

        mCart = (Cart) getIntent().getSerializableExtra(PAYLOAD);
        mClient = (UserInfo) getIntent().getSerializableExtra(OrderActivity.USER_PAYLOAD);

        mCart.setDeliveryOption(DELIVERY);
        cartListView.setAdapter(new CartViewAdapter(this, mCart, this));

        minimumOrder.setText(String.format(getResources().getString(R.string.minimun_order), MINIMUM_ORDER));

        cartSum.setText(String.format("%.2f €", mCart.getTotal()));

        checkoutDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(PAYLOAD, mCart);
                setResult(RESULT_BACK_TO_BUY, data);
                finish();
            }
        });

        checkMinimun();

        checkoutBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.postOrderFromKpk(new OrderFromKpk(mCart, mClient), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d(TAG, "OK: " + s);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "ERROR " + error.getMessage());
                    }
                });
            }
        });


        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.setDeliveryOption(PICK_UP);
                takeAway.setBackground(getResources().getDrawable(R.drawable.custom_btn_outline_gray));
                takeAway.setTextColor(getResources().getColor(R.color.neutral));

                pickUp.setBackground(getResources().getDrawable(R.drawable.custom_btn_outline_orange));
                pickUp.setTextColor(getResources().getColor(R.color.error));
            }
        });

        takeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.setDeliveryOption(DELIVERY);
                takeAway.setBackground(getResources().getDrawable(R.drawable.custom_btn_outline_orange));
                takeAway.setTextColor(getResources().getColor(R.color.error));

                pickUp.setBackground(getResources().getDrawable(R.drawable.custom_btn_outline_gray));
                pickUp.setTextColor(getResources().getColor(R.color.neutral));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void updatedCart(Cart updatedCart) {
        this.mCart = updatedCart;
        cartSum.setText(String.format( "%.2f €", mCart.getTotal()));
        // update total, etc

        checkMinimun();
    }

    private void checkMinimun() {
        if (mCart.getTotal() >= MINIMUM_ORDER) {
            alertView.setVisibility(View.GONE);
            checkoutBuy.setEnabled(true);
        } else {
            alertView.setVisibility(View.VISIBLE);
            checkoutBuy.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(PAYLOAD, mCart);
        setResult(RESULT_BACK_TO_BUY, data);
        super.onBackPressed();
    }
}
