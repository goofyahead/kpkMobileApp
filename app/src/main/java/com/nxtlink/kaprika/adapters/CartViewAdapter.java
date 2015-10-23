package com.nxtlink.kaprika.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.interfaces.CartUpdated;

import kpklib.models.Cart;

/**
 * Created by goofyahead on 8/09/15.
 */
public class CartViewAdapter  extends BaseAdapter {

    private Cart mCart;
    private LayoutInflater mInflater;
    private CartUpdated mCallback;

    public CartViewAdapter(Context context, Cart cart, CartUpdated checkoutActivity) {
        this.mCart = cart;
        this.mInflater = LayoutInflater.from(context);
        this.mCallback = checkoutActivity;
    }

    @Override
    public int getCount() {
        return mCart.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return mCart.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.cart_item_layout, null);
            holder.name = (TextView) convertView.findViewById(R.id.cart_item_name);
            holder.discard = (ImageView) convertView.findViewById(R.id.cart_item_discard);
            holder.more = (ImageView) convertView.findViewById(R.id.cart_item_more);
            holder.less = (ImageView) convertView.findViewById(R.id.cart_item_less);
            holder.quantity = (TextView) convertView.findViewById(R.id.cart_item_qty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mCart.getItem(position).getItem().getName());
        holder.quantity.setText("" + mCart.getItem(position).getQuantity());

        holder.discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.deleteItem(position);
                CartViewAdapter.this.notifyDataSetChanged();
                mCallback.updatedCart(mCart);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.getItem(position).setQuantity(mCart.getItem(position).getQuantity() + 1);
                CartViewAdapter.this.notifyDataSetChanged();
                mCallback.updatedCart(mCart);
            }
        });

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCart.getItem(position).getQuantity() > 1) {
                    mCart.getItem(position).setQuantity(mCart.getItem(position).getQuantity() - 1);
                    CartViewAdapter.this.notifyDataSetChanged();
                    mCallback.updatedCart(mCart);
                }
            }
        });
        return convertView;
    }


    private class ViewHolder {
        private TextView name;
        private TextView quantity;
        private ImageView more;
        private ImageView less;
        private ImageView discard;
    }
}
