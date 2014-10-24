package com.misabiko.LWJGLGameEngine.Utils;

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
}
