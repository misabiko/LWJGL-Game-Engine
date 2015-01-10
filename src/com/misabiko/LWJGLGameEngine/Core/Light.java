package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position;
	private Vector3f intensities;
	
	public Light(float x, float y, float z, float r, float g, float b) {
		position = new Vector3f(x, y, z);
		intensities = new Vector3f(r, g, b);
	}
	
	public Light(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		intensities = new Vector3f(1f, 1f, 1f);
	}
	
	public Matrix3f getMatrix() {
		Matrix3f mat = new Matrix3f();
		mat.setZero();
		
		mat.m00 = position.x;
		mat.m01 = position.y;
		mat.m02 = position.z;

		mat.m10 = intensities.x;
		mat.m11 = intensities.y;
		mat.m12 = intensities.z;
		
		return mat;
	}
}
