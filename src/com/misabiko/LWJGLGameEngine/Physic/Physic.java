package com.misabiko.LWJGLGameEngine.Physic;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public class Physic {
	private static float ySpeedCap = -1f;
	private static float speedCap = 0.1f;
	private static float gravity = 0.05f;
	private static float friction = 0.0005f;
	
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
		
		if (m.xVel > speedCap) {
			m.xVel = speedCap;
		}else if (m.xVel < -speedCap) {
			m.xVel = -speedCap;
		}
		
		if (m.zVel > speedCap) {
			m.zVel = speedCap;
		}else if (m.zVel < -speedCap) {
			m.zVel = -speedCap;
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
