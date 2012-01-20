package com.musevisions.android.OGLESCubeTestAPI;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.musevisions.android.OGLESCubeTestAPI.GLUtils;
import com.musevisions.android.OGLESCubeTestAPI.GLMath.mat4;

/**
 * Render a cube.
 */
class CubeRendererES20 implements GLSurfaceView.Renderer, CubeRenderer {
	
    protected static final String DEBUG_TAG = "Event";

    
    private final String vertexShaderCode = 
        "uniform mat4 MVPMatrix; \n" +
        "attribute vec4 vPosition; \n" +
        "attribute vec3 vColor; \n" +
        "varying vec4 color;  \n" +
        "void main(){              \n" +
        " color = vec4(vColor.x, vColor.y, vColor.z, 1.0); \n" +
        " gl_Position = MVPMatrix * vPosition; \n" +
        "}                         \n";
    
    private final String fragmentShaderCode = 
        "precision mediump float;  \n" +
        "varying vec4 color;  \n" +
        "void main(){              \n" +
        " gl_FragColor = color; \n" +
        "}                         \n";
    
    
    private int mProgram;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPHandle;
    
    public CubeRendererES20() {
        mCube = new CubeES20();
        
        Log.v(DEBUG_TAG, "->CubeRenderer");
        

    }

    private int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        GLUtils.checkGLError("glCreateShader");
        
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLUtils.checkGLError("glShaderSource");
        GLES20.glCompileShader(shader);
        GLUtils.checkGLError("glCompileShader");

        return shader;
    }
    
    public void onDrawFrame(GL10 unused) {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

    	GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    	//GLES20.glCullFace(GLES20.GL_BACK);

        /* Update MVP matrix */

        mat4 mvp = mProj
        	.mul(mat4.translation(0.0f, 0.0f, -5.0f))
        	.mul(mat4.rotateY(-mAngleX))
        	.mul(mat4.rotateX(mAngleY));
        	
      
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        /* Set model view projection matrix */
        GLES20.glUniformMatrix4fv(muMVPHandle, 1, false, mvp.f(), 0);
       
        /*
         * Now we're ready to draw some 3D objects
         */
        mCube.draw(maPositionHandle, maColorHandle);
    }   
 

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	
    	GLES20.glViewport(0, 0, width, height);    	
    	GLUtils.checkGLError("glViewport");

        /*
         * Set our projection matrix. This doesn't have to be done
         * each time we draw, but usually a new projection needs to
         * be set when the viewport is resized.
         */
    	mProj = mat4.projection(0.1f, 1000.0f, 45.0f, (float)width / (float)height); 
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	
    	Log.v(DEBUG_TAG, "->onSurfaceCreated");
    	
    	int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    	GLUtils.checkGLError("Vertex Shader");
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    	GLUtils.checkGLError("Fragment Shader");
        
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
    	GLUtils.checkGLError("Create Program");
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
    	GLUtils.checkGLError("Attach Vertex");
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
    	GLUtils.checkGLError("Attach Fragment");
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL program executables
    	GLUtils.checkGLError("Link Program");
        
        // get handles
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
    	GLUtils.checkGLError("glGetAttribLocation");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
    	GLUtils.checkGLError("glGetAttribLocation");

    	muMVPHandle = GLES20.glGetUniformLocation(mProgram, "MVPMatrix");
    	GLUtils.checkGLError("glGetUniformLocation");
    	
    }
    
    private CubeES20 mCube;
    private float mAngleX;
    private float mAngleY;
    private mat4 mProj;

	@Override
	public void updateAngles(float angleDX, float angleDY) {
		// TODO Auto-generated method stub
		mAngleX += angleDX;
		mAngleY += angleDY;
	}
}
