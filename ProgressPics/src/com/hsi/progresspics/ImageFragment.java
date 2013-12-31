package com.hsi.progresspics;

import java.util.ArrayList;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageFragment extends Fragment {

	private FlingViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewPager = new FlingViewPager(getActivity());
		DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
		ArrayList<String> photoArrayList = new ArrayList<String>();
		ArrayList<String> weightList = new ArrayList<String>();
		ArrayList<String> commentList = new ArrayList<String>();
		ArrayList<String> dateList = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("cards", null, null, null, null, null, null,
				null);
		if (cursor.moveToFirst() != false) {
			while (cursor.moveToNext()) {
				photoArrayList.add(cursor.getString(1));
				weightList.add(cursor.getString(2));
				commentList.add(cursor.getString(3));
				dateList.add(cursor.getString(4));
			}

			FlingAdapter adapter = new FlingAdapter(getActivity(),
					photoArrayList, weightList, commentList, dateList);
			viewPager.setAdapter(adapter);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return viewPager;
	}
}
