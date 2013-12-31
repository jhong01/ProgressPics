package com.hsi.progresspics;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlingAdapter extends PagerAdapter {

	private Context context;
	ArrayList<String> adapterList;
	ArrayList<String> weightList;
	ArrayList<String> commentList;
	ArrayList<String> dateList;

	public FlingAdapter(Context context, ArrayList<String> adapterList,
			ArrayList<String> weightList, ArrayList<String> commentList,
			ArrayList<String> dateList) {
		this.context = context;
		this.adapterList = adapterList;
		this.weightList = weightList;
		this.commentList = commentList;
		this.dateList = dateList;
	}

	public View instantiateItem(ViewGroup container, int position) {

		PhotoView photoView = new PhotoView(container.getContext());

		String photo_path = adapterList.get(position);
		String weight = weightList.get(position);
		String comment = commentList.get(position);
		String date = dateList.get(position);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		container.addView(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		File file = new File(photo_path);
		Bitmap bmp_thumb = BitmapFactory.decodeFile(file.getAbsolutePath());
		photoView.setImageBitmap(bmp_thumb);

		// Now just add PhotoView to ViewPager and return it
		layout.addView(photoView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		TextView weightText = new TextView(container.getContext());
		weightText.setText(weight);
		layout.addView(weightText, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		return layout;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.adapterList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view == object;

	}
}