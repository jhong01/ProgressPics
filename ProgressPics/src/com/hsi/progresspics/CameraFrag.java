package com.hsi.progresspics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.SimpleCameraHost;

public class CameraFrag extends CameraFragment implements
		OnSeekBarChangeListener {
	private static final String KEY_USE_FFC = "com.hsi.progresspics.USE_FFC";
	private MenuItem singleShotItem = null;
	private MenuItem autoFocusItem = null;
	private MenuItem takePictureItem = null;
	private MenuItem flipCameraAction = null;
	private boolean singleShotProcessing = false;
	private SeekBar zoom = null;
	Button flipper;
	static boolean flip;
	FlipView flipView;

	interface FlipView {
		void flip();
	}

	static CameraFrag newInstance(boolean useFFC) {
		CameraFrag f = new CameraFrag();
		Bundle args = new Bundle();

		args.putBoolean(KEY_USE_FFC, useFFC);
		f.setArguments(args);

		return (f);
	}

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		setHasOptionsMenu(true);
		setHost(new CameraHost(getActivity()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cameraView = super.onCreateView(inflater, container,
				savedInstanceState);
		View results = inflater.inflate(R.layout.fragment, container, false);

		((ViewGroup) results.findViewById(R.id.camera)).addView(cameraView);
		zoom = (SeekBar) results.findViewById(R.id.zoom);
		/*
		 * flipper = (Button) results.findViewById(R.id.flip);
		 * flipper.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub if (flip) { flipView.flip(true); } else {
		 * flipView.flip(false); } } });
		 */

		return (results);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			flipView = (FlipView) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FlipView");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.camera_menu, menu);

		takePictureItem = menu.findItem(R.id.camera);
		singleShotItem = menu.findItem(R.id.single_shot);
		singleShotItem.setChecked(getContract().isSingleShotMode());
		autoFocusItem = menu.findItem(R.id.autofocus);
		flipCameraAction = menu.findItem(R.id.flipCameraAction);
		if (flip) {
			flipCameraAction.setVisible(true);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.camera:
			if (singleShotItem.isChecked()) {
				singleShotProcessing = true;
				takePictureItem.setEnabled(false);
			}

			takePicture();

			return (true);

		case R.id.autofocus:
			autoFocus();

			return (true);

		case R.id.single_shot:
			item.setChecked(!item.isChecked());
			getContract().setSingleShotMode(item.isChecked());

			return (true);
		case R.id.flipCameraAction:
			flipView.flip();
		}

		return (super.onOptionsItemSelected(item));
	}

	boolean isSingleShotProcessing() {
		return (singleShotProcessing);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			zoom.setEnabled(false);
			zoomTo(zoom.getProgress()).onComplete(new Runnable() {
				@Override
				public void run() {
					zoom.setEnabled(true);
				}
			}).go();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// ignore
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// ignore
	}

	Contract getContract() {
		return ((Contract) getActivity());
	}

	interface Contract {
		boolean isSingleShotMode();

		void setSingleShotMode(boolean mode);
	}

	class CameraHost extends SimpleCameraHost {
		boolean supportsFaces = false;

		public CameraHost(Context _ctxt) {
			super(_ctxt);
		}

		@Override
		public boolean useFrontFacingCamera() {
			return (getArguments().getBoolean(KEY_USE_FFC));
		}

		@Override
		public boolean useSingleShotMode() {
			return (singleShotItem.isChecked());
		}

		@Override
		public void saveImage(byte[] image) {
			if (useSingleShotMode()) {
				singleShotProcessing = false;

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						takePictureItem.setEnabled(true);
					}
				});

				DisplayActivity.imageToShow = image;
				startActivity(new Intent(getActivity(), DisplayActivity.class));
			} else {
				super.saveImage(image);
			}
		}

		@Override
		public void autoFocusAvailable() {
			autoFocusItem.setEnabled(true);
			if (supportsFaces)
				startFaceDetection();
		}

		@Override
		public void autoFocusUnavailable() {
			stopFaceDetection();
			if (supportsFaces)
				autoFocusItem.setEnabled(false);
		}

		@Override
		public void onCameraFail(CameraHost.FailureReason reason) {
			super.onCameraFail(reason);

			Toast.makeText(getActivity(),
					"Sorry, but you cannot use the camera now!",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public Parameters adjustPreviewParameters(Parameters parameters) {
			if (parameters.getMaxZoom() == 0) {
				zoom.setEnabled(false);
			} else {
				zoom.setMax(parameters.getMaxZoom());
				zoom.setOnSeekBarChangeListener(CameraFrag.this);
			}

			if (parameters.getMaxNumDetectedFaces() > 0) {
				supportsFaces = true;
			} else {

			}

			return (super.adjustPreviewParameters(parameters));
		}

	}

}
