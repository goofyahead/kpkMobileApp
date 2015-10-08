package com.nxtlink.kaprika.pagerAdapters;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kpklib.constants.Credentials;
import kpklib.models.Dish;


public class CarrouselAdapter extends PagerAdapter {
    private ArrayList<Dish> elements;
    private Context mContext;
    private LayoutInflater mInflater;

    public CarrouselAdapter(ArrayList<Dish> dishes, Context contex) {
        this.elements = dishes;
        this.mContext = contex;
        mInflater = LayoutInflater.from(contex);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == ((View) o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View holder = mInflater.inflate(R.layout.highlight_item, null);
        Dish currenDish = elements.get(position);
        String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + Credentials.FOLDER_KPK_PICTURES + currenDish.getImage();
        Picasso.with(mContext).load(new File(imagePath)).into(((ImageView) holder.findViewById(R.id.highlight_dish_image)));
        ((TextView) holder.findViewById(R.id.highlight_dish_name)).setText(currenDish.getName());
        ((TextView) holder.findViewById(R.id.highlight_dish_description)).setText(currenDish.getDescription());
        final int currentPos = position;

//        holder.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("es.mgl.hai." + elements.get(currentPos).getModelType());
//                intent.putExtra("ID", info.getId());
//                mContext.startActivity(intent);
//            }
//        });

        container.addView(holder, 0);
        return holder;
    }
}
