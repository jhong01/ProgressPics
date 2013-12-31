package com.hsi.progresspics;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class FlingViewPager extends ViewPager {

	public FlingViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}