package com.hsi.progresspics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hsi.progresspics.CameraFrag.FlipView;
import com.hsi.progresspics.TodayFragment.FragmentSwitcher;

public class MainActivity extends FragmentActivity implements FragmentSwitcher,
		CameraFrag.Contract, FlipView {
	private String[] navtitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String STATE_SINGLE_SHOT = "single_shot";
	private static final int CONTENT_REQUEST = 1337;
	private CameraFrag std = null;
	private CameraFrag ffc = null;
	private CameraFrag current = null;
	private boolean hasTwoCameras = (Camera.getNumberOfCameras() > 1);
	private boolean singleShot = false;
	boolean flip = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navdrawer);

		navtitles = getResources().getStringArray(R.array.nav);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, navtitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		Fragment fragment = new TodayFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		mDrawerList.setItemChecked(0, true);

	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			// selectItem(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment;
		Intent intent;
		Bundle args = new Bundle();
		switch (position) {
		case 0:
			fragment = new TodayFragment();
		case 1:
			// fragment = new PreviousFragment();
		case 2:
			// fragment = new ChartsFragment();
		case 3:
			// fragment = new WatchProgressFragment();
		case 4:
			// fragment = new PreferencesFragment();
		default:
			fragment = new TodayFragment();
		}

		// args.putInt(TodayFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navtitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		CharSequence mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public void switchFragment() {
		// TODO Auto-generated method stub
		if (hasTwoCameras) {

			current = CameraFrag.newInstance(false);
			std = current;
			getFragmentManager().beginTransaction()
					.replace(R.id.content_frame, current).commit();
			CameraFrag.flip = true;
		}

		else {
			current = CameraFrag.newInstance(false);

			getFragmentManager().beginTransaction()
					.replace(R.id.content_frame, current).commit();
		}

	}

	@Override
	public boolean isSingleShotMode() {
		// TODO Auto-generated method stub
		return (singleShot);
	}

	@Override
	public void setSingleShotMode(boolean mode) {
		// TODO Auto-generated method stub
		singleShot = mode;

	}

	@Override
	public void flip() {
		// TODO Auto-generated method stub

		if (std == null) {
			std = CameraFrag.newInstance(false);
			current = std;
			ffc = null;
		}

		else if (ffc == null) {
			ffc = CameraFrag.newInstance(true);
			current = ffc;
			std = null;
		}

		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, current).commit();
	}
}
