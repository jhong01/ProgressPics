package com.hsi.progresspics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class PictureActivity extends Activity{
	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private static String filename;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		 mCamera = getCameraInstance();
	        mCameraPreview = new CameraPreview(this, mCamera);
	        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mCameraPreview);

	        Button captureButton = (Button) findViewById(R.id.button_capture);
	        captureButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                mCamera.takePicture(null, null, mPicture);
	            }
	        });
	        
	}
	private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }
	PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            /*File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }*/
            try {
               // FileOutputStream fos = new FileOutputStream(pictureFile);
            	FileOutputStream fos = openFileOutput("Test", Context.MODE_PRIVATE);
                fos.write(data);
                fos.close();
                
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","Test");
            setResult(RESULT_OK,returnIntent);     
            finish();
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                "ProgressPics");
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
        filename = mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg";
        mediaFile = new File(filename);
        
        return mediaFile;
    }
	

}
