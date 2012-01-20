/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.musevisions.android.OGLESCubeTestAPI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.opengl.GLES20;

/**
 * A vertex shaded cube.
 */
class CubeES20
{
	int mNumIndices;
	
    public CubeES20()
    {
        int one = 0x10000;
        int vertices[] = {
                -one, -one, -one,
                one, -one, -one,
                one,  one, -one,
                -one,  one, -one,
                -one, -one,  one,
                one, -one,  one,
                one,  one,  one,
                -one,  one,  one,
        };

        int colors[] = {
                0,    0,    0,  one,
                one,    0,    0,  one,
                one,  one,    0,  one,
                0,  one,    0,  one,
                0,    0,  one,  one,
                one,    0,  one,  one,
                one,  one,  one,  one,
                0,  one,  one,  one,
        };

        byte indices[] = {
                0, 4, 5,    0, 5, 1,
                1, 5, 6,    1, 6, 2,
                2, 6, 7,    2, 7, 3,
                3, 7, 4,    3, 4, 0,
                4, 7, 6,    4, 6, 5,
                3, 0, 1,    3, 1, 2
        };

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        
        mNumIndices = indices.length;
 
    }
   
    
    public void draw(int posHandle, int colorHandle)
    {
    	//GLES20.glFrontFace(GLES20.GL_CCW);
    	
    	/* Set attributes */
    	GLES20.glVertexAttribPointer(posHandle, 3, GLES20.GL_FIXED, false, 12, mVertexBuffer);
    	GLUtils.checkGLError("glVertexAttribPointer");
    	GLES20.glEnableVertexAttribArray(posHandle);
    	GLUtils.checkGLError("glEnableVertexAttribArray");
    	
    	GLES20.glVertexAttribPointer(colorHandle, 3, GLES20.GL_FIXED, false, 12, mColorBuffer);
    	GLUtils.checkGLError("glVertexAttribPointer");
    	GLES20.glEnableVertexAttribArray(colorHandle);
    	GLUtils.checkGLError("glEnableVertexAttribArray");

    	/* Draw */
    	GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);
    	GLUtils.checkGLError("glDrawElements");
    	
    	/* Unset attributes */
    	GLES20.glDisableVertexAttribArray(posHandle);
    	GLUtils.checkGLError("glDisableVertexAttribArray");
    	GLES20.glDisableVertexAttribArray(colorHandle);
    	GLUtils.checkGLError("glDisableVertexAttribArray");
    }

    private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;
    private ByteBuffer  mIndexBuffer;
}
