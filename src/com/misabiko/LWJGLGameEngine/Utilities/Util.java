package com.misabiko.LWJGLGameEngine.Utilities;

import org.lwjgl.util.vector.Matrix3f;
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
	
	public static double vector2fToAngle(Vector2f vec, boolean radian) {
		double angle = Math.atan(vec.y/vec.x);
		if (vec.x < 0 && vec.y >= 0)
			angle = angle >= 0 ? Math.PI - angle : Math.PI + angle;
		else if (vec.x < 0 && vec.y < 0)
			angle += Math.PI;
		else if (vec.x >= 0 && vec.y < 0)
			angle += Math.PI*2;
		return radian ? angle : Math.toDegrees(angle);
	}
	
	public static Vector3f mulMatrix4fVector3f(Matrix4f mat, Vector3f vec) {
		vec.set((vec.x*mat.m00)+(vec.y*mat.m10)+(vec.z*mat.m20),
				(vec.x*mat.m01)+(vec.y*mat.m11)+(vec.z*mat.m21),
				(vec.x*mat.m02)+(vec.y*mat.m12)+(vec.z*mat.m22));
		
		return vec;
	}
	
	public static Matrix3f mat4ToMat3(Matrix4f mat4) {
		Matrix3f mat3 = new Matrix3f();
		
		mat3.m00 = mat4.m00;
		mat3.m10 = mat4.m10;
		mat3.m20 = mat4.m20;
		
		mat3.m01 = mat4.m01;
		mat3.m11 = mat4.m11;
		mat3.m21 = mat4.m21;
		
		mat3.m02 = mat4.m02;
		mat3.m12 = mat4.m12;
		mat3.m22 = mat4.m22;

		
		return mat3;
	}
	
	public static float lengthBetween2Points(float x, float y, float x2, float y2) {	//Basically a simple pythagorium
		return (float) Math.sqrt(((y2-y)*(y2-y))+((x2-x)*(x2-x)));
	}
	
	public static float getYForAnXOnALine(float x, float x1, float y1, float x2, float y2) {
		float a = (y2-y1)/(x2-x1);
		float b = y1-(a*x1);
		
		return (a*x)+b;
	}
	
	public static float getXForAnYOnALine(float y, float x1, float y1, float x2, float y2) {
		float a = (y2-y1)/(x2-x1);
		float b = y1-(a*x1);
		
		return (y-b)/a;
	}
}
