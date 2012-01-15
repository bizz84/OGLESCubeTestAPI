package com.musevisions.android.OGLESCubeTestAPI;

import android.opengl.GLSurfaceView;

public interface CubeRenderer extends GLSurfaceView.Renderer {

	public void updateAngles(float angleDX, float angleDY);
}
