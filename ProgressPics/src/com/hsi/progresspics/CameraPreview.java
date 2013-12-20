package com.hsi.progresspics;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	private boolean isPreviewRunning = false;
	
	// Constructor that obtains context and camera
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.mCamera = camera;
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		//this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		/*try {
			//mCamera.setDisplayOrientation(90);
		    mCamera.setPreviewDisplay(surfaceHolder);
		    mCamera.startPreview();
		} catch (IOException e) {
		    // left blank for now
		}*/
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		mCamera.stopPreview();
		mCamera.release();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
	    int width, int height) {
	// start preview with new settings
		/*try {
		    mCamera.setPreviewDisplay(surfaceHolder);
		    mCamera.startPreview();
		} catch (Exception e) {
		    // intentionally left blank for a test
		}*/
		 if (isPreviewRunning)
	        {
	            mCamera.stopPreview();
	        }

        Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(height, width);                           
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(width, height);                           
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(height, width);               
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        //mCamera.setParameters(parameters);
        previewCamera();                      
	}
	public void previewCamera()
	{        
	    try 
	    {           
	        mCamera.setPreviewDisplay(mSurfaceHolder);          
	        mCamera.startPreview();
	        isPreviewRunning = true;
	    }
	    catch(Exception e)
	    {
	        //Log.d(Context.APP_CLASS, "Cannot start preview", e);    
	    }
	}
}