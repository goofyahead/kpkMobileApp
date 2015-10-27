package com.nxtlink.kaprika.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.activities.CheckoutActivity;
import com.nxtlink.kaprika.fragments.OrderListFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Locale;

import kpklib.models.Cart;

/**
 * Created by goofyahead on 10/27/15.
 */
public class OrderListAdapter extends BaseAdapter {

    private static final String TAG = OrderListFragment.class.getName();
    private LinkedList<Cart> orders;
    private LayoutInflater mInflater;
    private Context mContext;

    public OrderListAdapter (LinkedList<Cart> orders, Context mContext) {
        this.orders = orders;
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.order_item, null);
            holder.date = (TextView) convertView.findViewById(R.id.order_date);
            holder.time = (TextView) convertView.findViewById(R.id.order_time);
            holder.firstLine = (TextView) convertView.findViewById(R.id.order_first_line);
            holder.secondLine = (TextView) convertView.findViewById(R.id.order_second_line);
            holder.deliveryOption = (TextView) convertView.findViewById(R.id.order_type_delivery);
            holder.totalamount = (TextView) convertView.findViewById(R.id.order_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cart currentOrder = orders.get(position);
        holder.totalamount.setText(String.format(mContext.getString(R.string.price_euro),Float.valueOf(currentOrder.getAmount())));

        if (currentOrder.getDeliveryOption().equalsIgnoreCase(CheckoutActivity.DELIVERY)){
            holder.deliveryOption.setText(mContext.getResources().getString(R.string.delivery_option));
        } else {
            holder.deliveryOption.setText(mContext.getResources().getString(R.string.pick_up_option));
        }

        DateTime dateTime = new DateTime(Long.parseLong(currentOrder.getTimestamp()));

        DateTimeFormatter day = DateTimeFormat.forPattern("EEEE d MMMM").withLocale(Locale.getDefault());
        DateTimeFormatter hour = DateTimeFormat.forPattern("HH:mm");

        holder.time.setText(hour.print(dateTime) );
        holder.date.setText(day.print(dateTime));

        holder.firstLine.setText(String.format(mContext.getString(R.string.order_line), currentOrder.getItem(0).getQuantity(), currentOrder.getItem(0).getItem().getName()));

        if (currentOrder.getItemCount() > 1) {
            holder.secondLine.setText(String.format(mContext.getString(R.string.order_line), currentOrder.getItem(1).getQuantity(), currentOrder.getItem(1).getItem().getName()));
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView date;
        private TextView time;
        private TextView firstLine;
        private TextView secondLine;
        private TextView deliveryOption;
        private TextView totalamount;
        private RelativeLayout addToFav;
    }
}
