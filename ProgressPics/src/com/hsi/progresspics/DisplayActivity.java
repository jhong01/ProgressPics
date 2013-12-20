package com.hsi.progresspics;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class DisplayActivity extends Activity {

	static byte[] imageToShow = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (imageToShow == null) {
			finish();
		} else {
			ImageView iv = new ImageView(this);
			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inMutable = false;
			opts.inSampleSize = 2;

			iv.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow, 0,
					imageToShow.length, opts));
			imageToShow = null;

			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			setContentView(iv);
		}
	}
}
