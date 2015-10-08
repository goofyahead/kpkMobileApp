package com.nxtlink.kaprika.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxtlink.kaprika.R;

import java.util.ArrayList;

import kpklib.models.MenuCategory;
import kpklib.models.MenuDrawerCategory;

/**
 * Created by goofyahead on 9/11/14.
 */
public class MenuAdapter extends BaseAdapter {

    //    private HashMap<String, Integer> categoriesMap = new HashMap<>();
    private ArrayList<MenuDrawerCategory> categories;
    private LayoutInflater mInflater;

    public MenuAdapter(Context context, ArrayList<MenuDrawerCategory> categories) {
        this.categories = categories;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
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
            convertView = mInflater.inflate(R.layout.categories_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.categories_item_name);
            holder.image = (ImageView) convertView.findViewById(R.id.category_image);
            holder.badge = (TextView) convertView.findViewById(R.id.badge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(categories.get(position).getCategoryName());

        if (categories.get(position).getBadgeCount() == 0) {
            holder.badge.setVisibility(View.INVISIBLE);
        } else {
            holder.badge.setVisibility(View.VISIBLE);
            holder.badge.setText(categories.get(position).getBadgeCount());
        }
        if (categories.get(position).getImg() != null){
            holder.image.setImageDrawable(categories.get(position).getImg());
        }

        return convertView;
    }


    private class ViewHolder {
        private TextView name;
        private ImageView image;
        private TextView badge;
    }
}
