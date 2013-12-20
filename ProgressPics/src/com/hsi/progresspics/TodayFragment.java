package com.hsi.progresspics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class TodayFragment extends Fragment {
	private ImageButton image;
	private ImageView todayPicture;
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	FragmentSwitcher fragmentSwitcher;

	public interface FragmentSwitcher {
		public void switchFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fragmentSwitcher = (FragmentSwitcher) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentSwitcher");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_today, container,
				false);
		image = (ImageButton) rootView.findViewById(R.id.cameraButton);
		/*
		 * todayPicture = new ImageView(getActivity());
		 * 
		 * FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
		 * FrameLayout.LayoutParams.MATCH_PARENT,
		 * FrameLayout.LayoutParams.MATCH_PARENT);
		 * 
		 * todayPicture.setScaleType(ImageView.ScaleType.MATRIX);
		 * todayPicture.setRotation(90); todayPicture.setAdjustViewBounds(true);
		 * 
		 * FrameLayout frame =
		 * (FrameLayout)rootView.findViewById(R.id.testframe);
		 * frame.addView(todayPicture, 0, params);
		 */

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(getActivity(),
				// PictureActivity.class);

				/*
				 * intent.setAction(Intent.ACTION_GET_CONTENT);
				 * intent.addCategory(Intent.CATEGORY_OPENABLE);
				 */
				// startActivityForResult(intent, REQUEST_CODE);
				fragmentSwitcher.switchFragment();
			}

		});

		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 * android.content.Intent)
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		InputStream stream = null;
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			// recyle unused bitmaps
			if (bitmap != null) {
				bitmap.recycle();
			}
			String filename = data.getStringExtra("result");
			/*
			 * stream =
			 * getActivity().getContentResolver().openInputStream(data.getData
			 * ()); bitmap = BitmapFactory.decodeStream(stream);
			 */
			File file = getActivity().getFilesDir();
			filename = file.getPath() + File.separator + "Test";
			File newFile = new File(filename);
			todayPicture.setImageURI(Uri.fromFile(newFile));

			// image.setImageBitmap(bitmap);
		}
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
