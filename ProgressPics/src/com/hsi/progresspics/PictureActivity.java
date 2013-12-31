package com.hsi.progresspics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class PictureActivity extends Activity {
	private String filename;
	private ImageView mImageView;
	private EditText weight;
	private EditText comments;
	private TextView datestring;
	private Button toMain;
	private String date;
	String formattedDate = "";
	Bitmap bitmap;
	CardInfo cardInfo;
	File output;
	String weightString;
	String commentString;
	ScrollView focusLayout;
	int position = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		focusLayout = (ScrollView) findViewById(R.id.focusLayout);
		setupUI(focusLayout);
		mImageView = (ImageView) findViewById(R.id.detailimage);
		weight = (EditText) findViewById(R.id.weight);
		comments = (EditText) findViewById(R.id.comments);
		datestring = (TextView) findViewById(R.id.date);
		toMain = (Button) findViewById(R.id.toMain);

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		formattedDate = df.format(c.getTime());

		toMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(PictureActivity.this,
						MainActivity.class);
				Bundle info = new Bundle();
				info.putBoolean("has image", true);
				mainIntent.putExtra("PictureActivityBundle", info);
				/*
				 * DatabaseHelper.getInstance(PictureActivity.this).saveNoteAsync
				 * ( position, filename, weight.getText().toString(),
				 * comments.getText().toString());
				 */
				DatabaseHelper.getInstance(PictureActivity.this).saveNoteAsync(
						filename, weight.getText().toString(),
						comments.getText().toString(), formattedDate);
				startActivity(mainIntent);
			}

		});
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		filename = bundle.getString("filename");
		setBitmap();
		try {
			overRideBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mImageView.setImageBitmap(ImageHelper.getRoundCornerBitmap(bitmap, 10));
		cardInfo = new CardInfo();
		/*
		 * output = new File(filename); if (output.exists()) { output.delete();
		 * // DELETE existing file output = new File(filename); }
		 */
		weight.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				weightString = weight.getText().toString();
				Log.d("weight string", "User set EditText value to "
						+ weightString);
			}
		});
		comments.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				commentString = comments.getText().toString();
				Log.d("comments string", "User set EditText value to "
						+ commentString);
			}
		});
	}

	private void setBitmap() {
		Bitmap bmp = BitmapFactory.decodeFile(filename);
		Matrix matrix = new Matrix();
		File tempfile = new File(filename);
		matrix.postRotate(PictureActivity.getCameraPhotoOrientation(this,
				Uri.fromFile(tempfile), filename));
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				400, getResources().getDisplayMetrics());
		float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, getResources().getDisplayMetrics());
		int ht = Math.round(ht_px);
		int wt = Math.round(wt_px);
		this.bitmap = scaleCenterCrop(bitmap, ht, wt);
		/*
		 * try { overRideBitmap(this.bitmap); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	private void overRideBitmap(Bitmap bitmap) throws IOException {

		File file = new File(filename);
		FileOutputStream fOut = new FileOutputStream(file);

		bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		fOut.flush();
		fOut.close();
	}

	public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(PictureActivity.this);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	public static int getCameraPhotoOrientation(Context context, Uri imageUri,
			String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.v("EXIF", "Exif orientation: " + orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,
			T... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

}
