package com.misabiko.LWJGLGameEngine.Physic;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public class Physic {
	private static float ySpeedCap = 1f;
	private static float gravity = 0.05f;
	private static float friction = 0.05f;
	
	private static void friction(Mesh m) {
		if (m.xzVel.length() > 0)
			m.xzVel.scale((m.xzVel.length()-friction)/m.xzVel.length());
		else
			m.xzVel.scale(0);
	}
	
	private static void gravity(Mesh m) {
		if (m.yVel > ySpeedCap)
			m.yVel -= gravity;
		else
			m.yVel = ySpeedCap;
	}
	
	public static void update(Mesh m) {
		friction(m);
		gravity(m);
	}
}
