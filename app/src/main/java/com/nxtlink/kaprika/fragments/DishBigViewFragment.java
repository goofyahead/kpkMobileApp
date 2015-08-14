package com.nxtlink.kaprika.fragments;

import java.io.File;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.db.DataHelper;
import com.nxtlink.kaprika.models.Dish;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DishBigViewFragment extends Fragment {

	@Inject DataHelper dataHelper;
	@InjectView(R.id.videoViewer) VideoView videoView;
	@InjectView(R.id.dishDescription) TextView dishDescriptionTextView;
	@InjectView(R.id.dishprice) TextView dishPriceTextView;
	@InjectView(R.id.dishTitle) TextView dishTitleTextView;
	private static final String DISH_ID = "dish_id";
	public static final String TAG = DishBigViewFragment.class.getName();

	public static DishBigViewFragment newInstance(String dishId) {
		DishBigViewFragment myFragment = new DishBigViewFragment();

		Bundle args = new Bundle();
		args.putString(DISH_ID, dishId);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dish_view_fragment, container, false);
		ButterKnife.inject(this, v);
		((KaprikaApplication) getActivity().getApplication()).inject(this);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		String dishId = getArguments().getString(DISH_ID);
		Log.d(TAG, "dish id " + dishId);

		Dish mDish = dataHelper.getDishById(dishId);
		Log.d(TAG,"dish retrieved with name " + mDish.getName());

		dishPriceTextView.setText("" + mDish.getPrice() + " �");
		dishTitleTextView.setText(mDish.getName());
		dishDescriptionTextView.setText(mDish.getDescription());

		final File path = getActivity().getFileStreamPath(mDish.getVideo());
		Log.d(TAG,"path is " + path.toString());


		videoView.setVideoPath(path.toString());
		
		videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				videoView.start();
			}
		});
		
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	                //I have a log statment here, so I can see that it is making it this far.
	                mp.reset(); // <--- I added this recently to try to fix the problem
	                videoView.setVideoPath(path.toString());
	        }
	    });
	}

}
