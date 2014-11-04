package com.misabiko.LWJGLGameEngine.Physic;

import org.lwjgl.util.vector.Vector2f;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class Physic {
	private static float ySpeedCap = -0.005f;
	private static float gravity = 0.05f;
	private static float friction = 0.001f;
	
	private static void friction(Mesh m) {
		if (m.xVel >= friction) {
			m.xVel -= friction;
		}else if (m.xVel <= -friction) {
			m.xVel += friction;
		}else {
			m.xVel = 0;
		}
		
		if (m.zVel >= friction) {
			m.zVel -= friction;
		}else if (m.zVel <= -friction) {
			m.zVel += friction;
		}else {
			m.zVel = 0;
		}
	}
	
	private static void gravity(Mesh m) {
		if (m.yVel > ySpeedCap)
			m.yVel -= gravity;
		else
			m.yVel = ySpeedCap;
	}
	
	public static void update(Mesh m) {
		friction(m);
//		gravity(m);
	}
}
