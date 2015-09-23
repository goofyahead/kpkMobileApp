package com.nxtlink.kaprika.fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.Credentials;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.db.DataHelper;
import com.nxtlink.kaprika.models.Dish;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DishViewFragment extends Fragment {

	@Inject DataHelper dataHelper;

    @InjectView(R.id.dish_view_videoview)
    VideoView videoView;

	private static final String DISH_ID = "dish_id";
	public static final String TAG = DishViewFragment.class.getName();

	public static DishViewFragment newInstance(String dishId) {
		DishViewFragment myFragment = new DishViewFragment();

		Bundle args = new Bundle();
		args.putString(DISH_ID, dishId);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dish_view, container, false);
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

        Log.d(TAG, "COUNT IS WHEN FRAGMENT LOADED: " + getFragmentManager().getBackStackEntryCount());
//
//		dishPriceTextView.setText("" + mDish.getPrice() + " �");
//		dishTitleTextView.setText(mDish.getName());
//		dishDescriptionTextView.setText(mDish.getDescription());

        String videoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + Credentials.FOLDER_KPK_VIDEOS + mDish.getVideo();

		Log.d(TAG,"path is " + videoPath.toString());

		videoView.setVideoPath(videoPath);

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				videoView.start();
			}
		});

		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				videoView.seekTo(0);
				videoView.start();
				//I have a log statment here, so I can see that it is making it this far.
//	                mp.reset(); // <--- I added this recently to try to fix the problem
//	                videoView.setVideoPath(path.toString());
			}
		});
	}

}
