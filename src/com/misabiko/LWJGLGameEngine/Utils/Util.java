package com.misabiko.LWJGLGameEngine.Utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Util {
	public static Vector3f angleToVector3f(float angleX, float angleY, float length) {
		Vector3f angle = new Vector3f();
		
//		Turning on Y axis
		angle.z = (float) Math.cos(angleY)*length;
		angle.x = (float) Math.sin(angleY)*length;
		
//		Turning on X axis
		angle.y = (float) -Math.sin(angleX)*length;
		
		return angle;
	}
	
	public static Vector3f angleToVector3f(float angleX, float angleY) {
		return angleToVector3f(angleX,angleY,1f);
	}
	
	public static Vector2f angleToVector2f(float angle, float length) {
		Vector2f vec = new Vector2f();
		
//		Turning on Y axis
		vec.x = (float) Math.cos(angle)*length;
		vec.y = (float) Math.sin(angle)*length;
		
		return vec;
	}
	
	public static Vector2f angleToVector2f(float angle) {
		return angleToVector2f(angle,1f);
	}
	
	public static Vector3f mulMatrix4fVector3f(Matrix4f mat, Vector3f vec) {
		vec.set((vec.x*mat.m00)+(vec.y*mat.m10)+(vec.z*mat.m20),
				(vec.x*mat.m01)+(vec.y*mat.m11)+(vec.z*mat.m21),
				(vec.x*mat.m02)+(vec.y*mat.m12)+(vec.z*mat.m22));
		
		return vec;
	}	
}
