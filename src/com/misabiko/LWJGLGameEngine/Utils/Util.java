package com.misabiko.LWJGLGameEngine.Utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Util {
	public static Vector3f angleToVector3f(float angleX, float angleY) {
		Vector3f angle = new Vector3f();
		
//		Turning on Y axis
		angle.z = (float) Math.cos(angleY);
		angle.x = (float) Math.sin(angleY);
		
//		Turning on X axis
		angle.y = (float) -Math.sin(angleX);
		
		return angle;
	}
	
	
	
	public static void mulMatrix4fVector3f(Matrix4f mat, Vector3f vec) {
		vec.setX((vec.x*mat.m00)+(vec.x*mat.m01)+(vec.x*mat.m02));
		vec.setY((vec.y*mat.m10)+(vec.y*mat.m11)+(vec.y*mat.m12));
		vec.setZ((vec.z*mat.m20)+(vec.z*mat.m21)+(vec.z*mat.m22));
	}
}
