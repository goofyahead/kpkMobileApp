package com.nxtlink.kaprika.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.AddToCart;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import kpklib.constants.Credentials;
import kpklib.db.DataHelper;
import kpklib.db.DbHelper;
import kpklib.models.Dish;
import kpklib.providers.DishProvider;

public class DishCursorAdapter extends CursorAdapter {
    private static final String TAG = DishCursorAdapter.class.getName();
    private final LayoutInflater inflater;
	private Context mContext;
	private int imageColumIndex;
	private int nameColumnIndex;
	private int priceColumnIndex;
	private int indexColumnIndex;
    private AddToCart activityInterface;
    private int descriptionColumnIndex;

	@Inject
    DataHelper dataHelper;

    public DishCursorAdapter(Context context, AddToCart iface) {
		super(context, null, false);

        ((KaprikaApplication) context.getApplicationContext()).inject(this);
		mContext = context;
		inflater = LayoutInflater.from(context);
        activityInterface = iface;
	}

	@Override
	public void changeCursor(Cursor cursor) {
		super.changeCursor(cursor);
		if (cursor != null) {
			indexColumnIndex = cursor.getColumnIndex(DbHelper.DISH_ID);
			imageColumIndex = cursor.getColumnIndex(DbHelper.DISH_IMAGE);
			nameColumnIndex = cursor.getColumnIndex(DbHelper.DISH_NAME);
			priceColumnIndex = cursor.getColumnIndex(DbHelper.DISH_PRICE);
            descriptionColumnIndex = cursor.getColumnIndex(DbHelper.DISH_DESCRIPTION);
		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
//        final Dish currentDish = new Dish(cursor.getString(indexColumnIndex), cursor.getString(nameColumnIndex), cursor.getString(descriptionColumnIndex),
//                cursor.getFloat(priceColumnIndex), cursor.getString(imageColumIndex), null, false, null, null, null, null);

		final Dish currentDish = dataHelper.getDishById(cursor.getString(indexColumnIndex));
        String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + Credentials.FOLDER_KPK_PICTURES + cursor.getString(imageColumIndex);
        Log.d(TAG, "imagePath; " + imagePath);
        Picasso.with(context).load(new File(imagePath)).into(holder.dishImage);
		holder.dishName.setText(cursor.getString(nameColumnIndex));
		holder.dishPrice.setText(String.format( "%.2f", cursor.getFloat(priceColumnIndex)) +  " €");

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "add to cart item");
                activityInterface.onDishAdded(currentDish);
            }
        });

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "more info about me");
            }
        });
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View created = inflater.inflate(R.layout.dish_item_square, null);
		ViewHolder holder = new ViewHolder();
		holder.dishImage = (ImageView) created.findViewById(R.id.dish_image);
		holder.dishName = (TextView) created.findViewById(R.id.dish_name);
		holder.dishPrice = (TextView) created.findViewById(R.id.dish_price);
		holder.addToCart = (RelativeLayout) created.findViewById(R.id.add_to_cart_relative_button);
		holder.moreInfo = (RelativeLayout) created.findViewById(R.id.more_info_relative_button);
		created.setTag(holder);
		return created;
	}

	@Override
	public String getItem(int position) {
		int previousPos = this.getCursor().getPosition();
		this.getCursor().moveToPosition(position);
		String id = this.getCursor().getString(indexColumnIndex);
		this.getCursor().moveToPosition(previousPos);
		return id;
	}

	public static class ViewHolder {
		private ImageView dishImage;
		private TextView dishName;
		private TextView dishPrice;
		private RelativeLayout addToCart;
		private RelativeLayout moreInfo;
	}
}
