package com.nxtlink.kaprika.activities;

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

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.CartViewAdapter;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.CartUpdated;
import com.nxtlink.kaprika.sharedprefs.KaprikaSharedPrefs;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.api.KaprikaApiInterface;
import kpklib.models.AccessToken;
import kpklib.models.Cart;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutActivity extends AppCompatActivity implements CartUpdated{

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
    @Inject
    KaprikaSharedPrefs prefs;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ButterKnife.inject(this);
        ((KaprikaApplication) getApplication()).inject(this);
        mCart = (Cart) getIntent().getSerializableExtra(PAYLOAD);
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

        checkoutBuy.setEnabled(false);
        checkoutBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBraintreeSubmit();
            }
        });

        api.getTokenClient(new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                checkoutBuy.setEnabled(true);
                token = accessToken.getAccessToken();
                Log.d(TAG, "Token retrieved " + token);
                checkMinimun();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ERROR GETTING TOKEN FOR TS " + error.getMessage());
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
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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

    public void onBraintreeSubmit() {
        Intent intent = new Intent(this, BraintreePaymentActivity.class);
        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription(this.getString(R.string.your_purchase))
                .secondaryDescription(getResources().getQuantityString(R.plurals.item_quantity, mCart.getItemsCount(), mCart.getItemsCount()))
                .amount("€" + String.format("%.2f", mCart.getTotal()))
                        .submitButtonText(this.getString(R.string.checkout_buy))
                        .build();
        intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, token);
        startActivityForResult(intent, REQUEST_CODE);
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
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                mCart.setNonce(paymentMethodNonce);
                api.notifyCartTransaction( mCart, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d(TAG, "SUCCESS: " + s);
                        Intent payload = new Intent();
                        payload.putExtra(PAYLOAD, new Cart("", ""));
                        setResult(resultCode, payload);
                        CheckoutActivity.this.finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "FAILURE: " + error.getMessage());
                    }
                });
            }
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
