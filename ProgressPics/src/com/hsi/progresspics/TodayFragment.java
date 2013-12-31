package com.hsi.progresspics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayFragment extends Fragment implements
		DatabaseHelper.CardListener {
	private ImageButton image;
	private ImageView todayPicture;
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	FragmentSwitcher fragmentSwitcher;
	static final int REQUEST_IMAGE_CAPTURE = 2;
	private ImageView mImageView;
	private String filename;
	private TextView commentText;
	private int position = 1;
	private TextView weightText1;
	private TextView commentsText1;
	private ImageView detailImage1;
	private TextView weightText2;
	private TextView commentsText2;
	private ImageView detailImage2;
	private TextView weightText3;
	private TextView commentsText3;
	private ImageView detailImage3;
	private TextView weightText4;
	private TextView commentsText4;
	private ImageView detailImage4;
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
		commentsText1 = (TextView) rootView.findViewById(R.id.comments1);
		weightText1 = (TextView) rootView.findViewById(R.id.weight1);
		detailImage1 = (ImageView) rootView.findViewById(R.id.detailimage1);
		commentsText2 = (TextView) rootView.findViewById(R.id.comments2);
		weightText2 = (TextView) rootView.findViewById(R.id.weight2);
		detailImage2 = (ImageView) rootView.findViewById(R.id.detailimage2);
		commentsText3 = (TextView) rootView.findViewById(R.id.comments3);
		weightText3 = (TextView) rootView.findViewById(R.id.weight3);
		detailImage3 = (ImageView) rootView.findViewById(R.id.detailimage3);
		commentsText4 = (TextView) rootView.findViewById(R.id.comments4);
		weightText4 = (TextView) rootView.findViewById(R.id.weight4);
		detailImage4 = (ImageView) rootView.findViewById(R.id.detailimage4);
		Intent boolIntent = getActivity().getIntent();
		Bundle boolBundle = boolIntent.getBundleExtra("PictureActivityBundle");
		/*
		 * if (boolBundle != null) { boolean hasImage =
		 * boolBundle.getBoolean("has image"); if (hasImage) {
		 * DatabaseHelper.getInstance(getActivity()).getNoteAsync( position,
		 * this); } }
		 */
		DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] args = { "filename", "weight", "comments" };
		/*
		 * Cursor cursor = db.query("cards", args, "position" + "=?", new
		 * String[] { "2" }, null, null, null, null);
		 */
		Cursor cursor = db.query("cards", null, null, null, null, null,
				"position DESC", null);
		int count = 0;
		if (cursor.moveToFirst() != false) {
			CardInfo card = new CardInfo(cursor.getString(1),
					cursor.getString(2), cursor.getString(3));
			setCard(card);

			while (cursor.moveToNext()) {
				if (count == 3) {
					break;
				}
				CardInfo miniCard = new CardInfo(cursor.getString(1),
						cursor.getString(2), cursor.getString(3));
				setMiniCard(miniCard, count);
				count++;
			}
		}
		// Contact contact = new
		// Contact(Integer.parseInt(cursor.getString(0)),
		// cursor.getString(1), cursor.getString(2));
		// CardInfo card = new CardInfo(cursor.getString(1),
		// cursor.getString(2), cursor.getString(3));
		// setCard(card);

		/*
		 * while (cursor.moveToPrevious()) { CardInfo miniCard = new
		 * CardInfo(cursor.getString(1), cursor.getString(2),
		 * cursor.getString(3)); setMiniCard(miniCard, count); count++; }
		 */

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

	private void setMiniCard(CardInfo miniCard, int count) {

		float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());
		float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				75, getResources().getDisplayMetrics());
		int ht = Math.round(ht_px);
		int wt = Math.round(wt_px);
		switch (count) {
		case 0:
			commentsText2.setText(miniCard.getComments());
			weightText2.setText(miniCard.getWeight());

			Bitmap b = BitmapFactory.decodeFile(miniCard.getFilename());
			detailImage2.setImageBitmap(ImageHelper.getRoundCornerBitmap(
					Bitmap.createScaledBitmap(b, wt, ht, true), 10));
		case 1:
			commentsText3.setText(miniCard.getComments());
			weightText3.setText(miniCard.getWeight());
			Bitmap c = BitmapFactory.decodeFile(miniCard.getFilename());
			detailImage3.setImageBitmap(ImageHelper.getRoundCornerBitmap(
					Bitmap.createScaledBitmap(c, wt, ht, true), 10));
		case 2:
			commentsText4.setText(miniCard.getComments());
			weightText4.setText(miniCard.getWeight());
			Bitmap d = BitmapFactory.decodeFile(miniCard.getFilename());
			detailImage4.setImageBitmap(ImageHelper.getRoundCornerBitmap(
					Bitmap.createScaledBitmap(d, wt, ht, true), 10));
		}

	}

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
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		filename = mediaStorageDir.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg";
		mediaFile = new File(filename);

		return mediaFile;
	}

	@Override
	public void setCard(CardInfo card) {
		// TODO Auto-generated method stub

		commentsText1.setText(card.getComments());
		weightText1.setText(card.getWeight());
		Bitmap b = BitmapFactory.decodeFile(card.getFilename());
		detailImage1.setImageBitmap(ImageHelper.getRoundCornerBitmap(b, 10));

	}

	/*
	 * private void setBitmap() { Bitmap bmp =
	 * BitmapFactory.decodeFile(filename); Matrix matrix = new Matrix(); File
	 * tempfile = new File(filename);
	 * matrix.postRotate(PictureActivity.getCameraPhotoOrientation(this,
	 * Uri.fromFile(tempfile), filename)); Bitmap bitmap =
	 * Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix,
	 * true); float ht_px =
	 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400,
	 * getResources().getDisplayMetrics()); float wt_px =
	 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
	 * getResources().getDisplayMetrics()); int ht = Math.round(ht_px); int wt =
	 * Math.round(wt_px); this.bitmap = scaleCenterCrop(bitmap, ht, wt); }
	 */

}