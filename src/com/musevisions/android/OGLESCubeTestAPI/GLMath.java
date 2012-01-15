package com.musevisions.android.OGLESCubeTestAPI;

public class GLMath {

	static public float degreesToRadians(float deg) {
    	return deg * (float)Math.PI / 180.0f;
    }

	
	static public class mat4 {
		
		/** Array used to store matrix values */
		float m[] = new float[16];
		
		/** Constructor */
		public mat4()
		{
			matrixIdentity();
		}
		/** Accessor method for glUniform setter */
		public final float[] f() { return m; }
		
		/** Wrappers for private mutable methods */
		static public mat4 identity()
		{
			return new mat4();
		}
		
		static public mat4 translation(float x, float y, float z) {
			return new mat4().matrixTranslate(x, y, z);
		}
		
		static public mat4 scale(float sx, float sy, float sz) {
			return new mat4().matrixScale(sx, sy, sz);
		}
		static public mat4 rotateX(float degrees) {
			return new mat4().matrixRotateX(degrees);
		}
		static public mat4 rotateY(float degrees) {
			return new mat4().matrixRotateY(degrees);
		}
		static public mat4 rotateZ(float degrees) {
			return new mat4().matrixRotateZ(degrees);
		}
		
		static public mat4 projection(float near, float far, float angle, float aspect) {
			return new mat4().matrixProjection(near, far, angle, aspect);
		}
		
		/* Private interface */
		private mat4(float init[]) {
			for (int i = 0; i < 16; i++) {
				m[i] = init[i];
			}
		}
		
		private mat4 matrixIdentity()
		{
		    m[0] = m[5] = m[10] = m[15] = 1.0f;
		    m[1] = m[2] = m[3] = m[4] = 0.0f;
		    m[6] = m[7] = m[8] = m[9] = 0.0f;
		    m[11] = m[12] = m[13] = m[14] = 0.0f;
		    return this;
		}
		
		private mat4 matrixTranslate(float x, float y, float z)
		{
		    matrixIdentity();
		 
		    // Translate slots.
		    m[12] = x;
		    m[13] = y;
		    m[14] = z;
		    return this;
		}
		
		private mat4 matrixScale(float sx, float sy, float sz)
		{
		    matrixIdentity();
		 
		    // Scale slots.
		    m[0] = sx;
		    m[5] = sy;
		    m[10] = sz;
		    return this;
		}
		
		private mat4 matrixRotateX(float degrees)
		{
		    float radians = degreesToRadians(degrees);
		 
		    matrixIdentity();
		 
		    // Rotate X formula.
		    m[5] = (float)Math.cos(radians);
		    m[6] = -(float)Math.sin(radians);
		    m[9] = -m[6];
		    m[10] = m[5];
		    return this;
		}
		
		private mat4 matrixRotateY(float degrees)
		{
		    float radians = degreesToRadians(degrees);
		 
		    matrixIdentity();
		 
		    // Rotate Y formula.
		    m[0] = (float)Math.cos(radians);
		    m[2] = (float)Math.sin(radians);
		    m[8] = -m[2];
		    m[10] = m[0];
		    return this;
		}
		private mat4 matrixRotateZ(float degrees)
		{
		    float radians = degreesToRadians(degrees);
		 
		    matrixIdentity();
		 
		    // Rotate Z formula.
		    m[0] = (float)Math.cos(radians);
		    m[1] = (float)Math.sin(radians);
		    m[4] = -m[1];
		    m[5] = m[0];
		    return this;
		}
		
		public mat4 mul(final mat4 m2)
		{
			float result[] = new float[16];
		    // Fisrt Column
		    result[0] = m[0]*m2.m[0] + m[4]*m2.m[1] + m[8]*m2.m[2] + m[12]*m2.m[3];
		    result[1] = m[1]*m2.m[0] + m[5]*m2.m[1] + m[9]*m2.m[2] + m[13]*m2.m[3];
		    result[2] = m[2]*m2.m[0] + m[6]*m2.m[1] + m[10]*m2.m[2] + m[14]*m2.m[3];
		    result[3] = m[3]*m2.m[0] + m[7]*m2.m[1] + m[11]*m2.m[2] + m[15]*m2.m[3];
		 
		    // Second Column
		    result[4] = m[0]*m2.m[4] + m[4]*m2.m[5] + m[8]*m2.m[6] + m[12]*m2.m[7];
		    result[5] = m[1]*m2.m[4] + m[5]*m2.m[5] + m[9]*m2.m[6] + m[13]*m2.m[7];
		    result[6] = m[2]*m2.m[4] + m[6]*m2.m[5] + m[10]*m2.m[6] + m[14]*m2.m[7];
		    result[7] = m[3]*m2.m[4] + m[7]*m2.m[5] + m[11]*m2.m[6] + m[15]*m2.m[7];
		 
		    // Third Column
		    result[8] = m[0]*m2.m[8] + m[4]*m2.m[9] + m[8]*m2.m[10] + m[12]*m2.m[11];
		    result[9] = m[1]*m2.m[8] + m[5]*m2.m[9] + m[9]*m2.m[10] + m[13]*m2.m[11];
		    result[10] = m[2]*m2.m[8] + m[6]*m2.m[9] + m[10]*m2.m[10] + m[14]*m2.m[11];
		    result[11] = m[3]*m2.m[8] + m[7]*m2.m[9] + m[11]*m2.m[10] + m[15]*m2.m[11];
		 
		    // Fourth Column
		    result[12] = m[0]*m2.m[12] + m[4]*m2.m[13] + m[8]*m2.m[14] + m[12]*m2.m[15];
		    result[13] = m[1]*m2.m[12] + m[5]*m2.m[13] + m[9]*m2.m[14] + m[13]*m2.m[15];
		    result[14] = m[2]*m2.m[12] + m[6]*m2.m[13] + m[10]*m2.m[14] + m[14]*m2.m[15];
		    result[15] = m[3]*m2.m[12] + m[7]*m2.m[13] + m[11]*m2.m[14] + m[15]*m2.m[15];
		    return new mat4(result);
		}
		
		private mat4 matrixProjection(float near, float far, float angle, float aspect) {
		    
		    // These paramaters are about lens properties.
		    // The "near" and "far" create the Depth of Field.
		    // The "angleOfView", as the name suggests, is the angle of view.
		    // The "aspectRation" is the cool thing about this matrix. OpenGL doesn't
		    // has any information about the screen you are rendering for. So the
		    // results could seem stretched. But this variable puts the thing into the
		    // right path. The aspect ration is your device screen (or desired area) width divided
		    // by its height. This will give you a number < 1.0 the the area has more vertical
		    // space and a number > 1.0 is the area has more horizontal space.
		    // Aspect Ration of 1.0 represents a square area.
		    //float near = 0.001, far = 100.0;
		    //float angleOfView = 45.0;
		    //float aspectRation = 0.75;
	    	

		 
		    // Some calculus before the formula.
		    float size = (float) (near * Math.tan(degreesToRadians(angle) / 2.0));
		    float left = -size, right = size, bottom = -size / aspect, top = size / aspect;
		 
		    // First Column
		    m[0] = 2 * near / (right - left);
		    m[1] = 0.0f;
		    m[2] = 0.0f;
		    m[3] = 0.0f;
		 
		    // Second Column
		    m[4] = 0.0f;
		    m[5] = 2 * near / (top - bottom);
		    m[6] = 0.0f;
		    m[7] = 0.0f;
		 
		    // Third Column
		    m[8] = (right + left) / (right - left);
		    m[9] = (top + bottom) / (top - bottom);
		    m[10] = -(far + near) / (far - near);
		    m[11] = -1;
		 
		    // Fourth Column
		    m[12] = 0.0f;
		    m[13] = 0.0f;
		    m[14] = -(2 * far * near) / (far - near);
		    m[15] = 0.0f;
		    
		    return this;
	    }
	}
	

	   
}
