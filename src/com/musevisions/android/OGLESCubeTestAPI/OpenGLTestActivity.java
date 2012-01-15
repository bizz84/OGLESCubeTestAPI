/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Modified source from Touch Rotate example in Android SDK.
 * This implementation adds inertia to cube motion by intercepting
 * the onFling event and posting additional renders until the inertia
 * falls below a minimum threshold.
 * 
 */
package com.musevisions.android.OGLESCubeTestAPI;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 *
 * Shows:
 * + How to redraw in response to user input.
 */
public class OpenGLTestActivity extends Activity {
	
    protected static final String DEBUG_TAG = "Event";
	
    private void makeText(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(DEBUG_TAG, "->onCreate");

        boolean es20Supported = detectOpenGLES20(); 
        if (!es20Supported) {
        	makeText("Running ES 1.0 as ES 2.0 not supported!");
        	
        	mTitleES = (String)getTitle() + " ES1.0";
        }
        else
        {
        	mTitleES = (String) getTitle() + " ES2.0";   	
        }
       	setTitle(mTitleES);
        
        // Create our Preview view and set it as the content of our
        // Activity
        mGLSurfaceView = new TouchSurfaceView(this, es20Supported, mTitleES);
        

        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);
        

	}
    
    private boolean detectOpenGLES20() {
        ActivityManager am =
            (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x20000);
    }


    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
        
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

    private GLSurfaceView mGLSurfaceView;
	private String mTitleES;
   
}


/**
 * Implement a simple rotation control.
 *
 */
class TouchSurfaceView extends GLSurfaceView {

    public TouchSurfaceView(Context context, boolean es20Supported, String titleES) {
        super(context);
        
        Log.v(DEBUG_TAG, "->TouchSurfaceView");
        
        owner = (Activity)context;
        mTitleES = titleES;
        
        if (es20Supported) {
	        setEGLContextClientVersion(2);
	
	        mRenderer = new CubeRendererES20();
        }
        else {
        	mRenderer = new CubeRendererES10();
        }
        	
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        
    	mGD = new GestureDetector(getContext(),
                new SimpleOnGestureListener() {

    		@Override
    		public boolean onScroll(MotionEvent e1, MotionEvent e2,
    		    float distanceX, float distanceY) {

    			Log.v(DEBUG_TAG, "onScroll(" + distanceX + ", " + distanceY + ")"); 
	    		// beware, it can scroll to infinity
	    		scrollBy((int)distanceX, (int)distanceY);
	    		return true;
    		}
    		
    		@Override
    		public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
    			
    			Log.v(DEBUG_TAG, "onFling(" + vX + ", " + vY + ")"); 

    			fling(vX, vY);
    			
    			return true;
    		}
    		
    		@Override
    		public boolean onDown(MotionEvent e) {
	    		if(!isFinished() ) { // is flinging
	    			forceFinished(true); // to stop flinging on touch
	    		}
	    		return true; // else won't work
    		}
    	});
    }

    @Override public boolean onTrackballEvent(MotionEvent e) {
    	mRenderer.updateAngles(
    			e.getX() * TRACKBALL_SCALE_FACTOR,
    			e.getY() * TRACKBALL_SCALE_FACTOR);
        requestRender();
        return true;
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            mRenderer.updateAngles(
            		dx * TOUCH_SCALE_FACTOR,
            		dy * TOUCH_SCALE_FACTOR);
            requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
       
        
        mGD.onTouchEvent(e);
        return true;
    }
    

	public void fling(float vx, float vy) {
		// Store velocity and current time
		mVelX = vx;
		mVelY = vy;
		mPrevTime = System.nanoTime();

		post(new Runnable() {  
           // @Override  
            public void run() {  
                onAnimateStep();  
            }  
        });  
	
	}
	private float updateSpeed(float speed, float acc) {
	    
		float updated = speed * (speed + acc) < 0.0f ? 0.0f : speed + acc;
		return Math.abs(updated) * INERTIA_MUL < MIN_MOVE ? 0.0f : updated;
	}
	private void onAnimateStep() {  
	    
	    if (mVelX != 0.0 || mVelY != 0.0) {
	    	
		    requestRender();

		    long curTime = System.nanoTime();

		    float xAcc = -mVelX * (float)(curTime - mPrevTime) / 1.0E9f;
		    float yAcc = -mVelY * (float)(curTime - mPrevTime) / 1.0E9f;
		    

			Log.v(DEBUG_TAG, "Step [xV: " + mVelX + ", yV: " + mVelY + ", xAcc: " + xAcc + ", yAcc: " + yAcc + "]");
		    
		    mVelX = updateSpeed(mVelX, xAcc);
		    mVelY = updateSpeed(mVelY, yAcc);

			mRenderer.updateAngles(
					mVelX * INERTIA_MUL,
					mVelY * INERTIA_MUL);

		    
		    mPrevTime = curTime;
		    
		    if ((updateCount++ % 50) == 0 || mVelX == 0.0f && mVelY == 0.0f)
		    	updateTitle(mVelX, mVelY);
		    
	        post(new Runnable() {  
	            // @Override  
	             public void run() {  
	                 onAnimateStep();  
	             }  
	         });
	    }

	}  
	
	public boolean isFinished() {
		return mVelX == 0.0f && mVelY == 0.0f;
	}
	public void forceFinished(boolean finished) {
		mVelX = 0.0f;
		mVelY = 0.0f;
	}
   

	private void updateTitle(float dx, float dy) {
		
		owner.setTitle(mTitleES + " [" + dx + ", " + dy + "]");
	}
    
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float INERTIA_MUL = 0.0001f;
    private final float MIN_MOVE = 0.001f;
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    private CubeRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private float mVelX;
    private float mVelY;
    private long mPrevTime;
	private GestureDetector mGD;
	
	private String mTitleES;
	private Activity owner;
	private int updateCount;
	

    protected static final String DEBUG_TAG = "Event";


}


