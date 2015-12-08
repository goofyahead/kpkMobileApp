package com.mgl.tpvkpk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.base.TpvKpkApplication;
import com.mgl.tpvkpk.dialog.SelectQuantityDialog;
import com.mgl.tpvkpk.interfaces.AddToCart;
import com.mgl.tpvkpk.interfaces.SelectQuantityInterface;

import java.util.HashMap;

import butterknife.ButterKnife;
import kpklib.models.Cart;
import kpklib.models.Dish;
import kpklib.models.UserInfo;

/**
 * Created by goofyahead on 11/3/15.
 */
public class OrderActivity extends AppCompatActivity implements AddToCart, SelectQuantityInterface {
    public static final String USER_PAYLOAD = "USER_PAYLOAD";
    private static final String TAG = OrderActivity.class.getName();
    private UserInfo currentClient;
    private Dish currentDishSelected;
    private Cart currentCart;
    private View cartView;
    private TextView orderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        ButterKnife.inject(this);
        ((TpvKpkApplication) getApplication()).inject(this);
        currentClient = (UserInfo) getIntent().getSerializableExtra(USER_PAYLOAD);

        currentCart = new Cart("", "");
    }

    @Override
    public void onDishAdded(Dish dish) {
        Log.d(TAG, "dish added " + dish.getName() + " options: " + dish.getOptions().keySet().toString());
        currentDishSelected = dish;
        SelectQuantityDialog.newInstance(dish).show(getFragmentManager(), "qtty");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        cartView = menu.findItem(R.id.action_cart).getActionView();
        orderCount = (TextView) cartView.findViewById(R.id.cart_count);

        updateCartCounter();

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked in click handler");
                Intent checkout = new Intent(OrderActivity.this, CheckoutActivity.class);
                checkout.putExtra(CheckoutActivity.PAYLOAD, currentCart);
                checkout.putExtra(USER_PAYLOAD, currentClient);
                startActivityForResult(checkout, 0);
            }
        });
        return true;
    }

    private void updateCartCounter() {
        if (currentCart.getItemsCount() == 0) {
            orderCount.setVisibility(View.INVISIBLE);
        } else {
            orderCount.setVisibility(View.VISIBLE);
            orderCount.setText("" + currentCart.getItemsCount());
        }
    }

    @Override
    public void quantitySelected(int quantity, HashMap<String, String> optionSelected) {
        if (quantity == 0) {
            currentDishSelected = null;
        } else {
            //add to order not just the number
            currentCart.addItemToCart(currentDishSelected, quantity, optionSelected);
            updateCartCounter();
        }
    }
}
