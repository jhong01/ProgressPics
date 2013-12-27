package com.hsi.progresspics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
	static final int REQUEST_IMAGE_CAPTURE = 2;
	private ImageView mImageView;
	private String filename;
	// ImageView cardimage;
	Context mContext;
	CardInfo cardInfo;

	public interface FragmentSwitcher {
		public void switchFragment();

		public void setCardInfo(CardInfo info);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
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
		todayPicture = (ImageView) rootView.findViewById(R.id.bigimage);
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
				// fragmentSwitcher.switchFragment();
				dispatchTakePictureIntent();
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

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent
				.resolveActivity(getActivity().getPackageManager()) != null) {
			File photoFile = null;
			photoFile = getOutputMediaFile();
			cardInfo = new CardInfo();
			fragmentSwitcher.setCardInfo(cardInfo);
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_CANCELED) {

			if (requestCode == REQUEST_IMAGE_CAPTURE
					&& resultCode == Activity.RESULT_OK) {
				// Used to get thumbnail
				// Bundle extras = data.getExtras();
				// Bitmap imageBitmap = (Bitmap) extras.get("data");

				// Bitmap bt = scaleCenterCrop(bitmap, 100, 100);
				// cardimage.setImageBitmap(bt);
				// todayPicture.setImageBitmap(bt);
				// todayPicture.setVisibility(View.VISIBLE);
				Intent pictureIntent = new Intent(mContext,
						PictureActivity.class);
				Bundle picBundle = new Bundle();
				picBundle.putString("filename", filename);
				pictureIntent.putExtra("bundle", picBundle);
				startActivity(pictureIntent);
			}

		}
	}

	private void reStoreImage(String filename) {

	}

	private File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "ProgressPics");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
		File mediaFile;
		filename = mediaStorageDir.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg";
		mediaFile = new File(filename);

		return mediaFile;
	}

}