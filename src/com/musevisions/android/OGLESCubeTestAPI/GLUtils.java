package com.musevisions.android.OGLESCubeTestAPI;

import android.opengl.GLES20;
import android.util.Log;

public class GLUtils {

    private static final String GL_TAG = "GLES20";

	public static void checkGLError(String msg) {
        int e = GLES20.glGetError();
        if (e != GLES20.GL_NO_ERROR) {
            Log.d(GL_TAG, "GLES20 ERROR: " + msg + " " + e);
            Log.d(GL_TAG, errString(e));
        }
    }
    
    public static String errString(int ec) {
        switch (ec) {
        case GLES20.GL_NO_ERROR:
            return "No error has been recorded.";
        case GLES20.GL_INVALID_ENUM:
            return "An unacceptable value is specified for an enumerated argument.";
        case GLES20.GL_INVALID_VALUE:
            return "A numeric argument is out of range.";
        case GLES20.GL_INVALID_OPERATION:
            return "The specified operation is not allowed in the current state.";
        case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
            return "The command is trying to render to or read from the framebuffer" +
            " while the currently bound framebuffer is not framebuffer complete (i.e." +
            " the return value from glCheckFramebufferStatus is not" +
            " GL_FRAMEBUFFER_COMPLETE).";
        case GLES20.GL_OUT_OF_MEMORY:
            return "There is not enough memory left to execute the command." +
                    " The state of the GL is undefined, except for the state" +
                    " of the error flags, after this error is recorded.";
        default :
            return "UNKNOW ERROR";
        }
    }
}
