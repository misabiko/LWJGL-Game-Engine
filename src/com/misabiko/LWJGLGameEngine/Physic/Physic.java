package com.misabiko.LWJGLGameEngine.Physic;

import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public class Physic {
	private static float ySpeedCap = -0.2f;
	private static float speedCap = 0.1f;
	private static float gravity = 0.05f;
	private static float friction = 0.08f;
	
	private static void friction(Mesh m) {
		if (m.xzVel.length() >= (friction/20)) {
			m.xzVel.scale(1f-friction);
		}else if (m.xzVel.length() <= (-friction/20)) {
			m.xzVel.scale(1f-friction);
		}else {
			m.xzVel.scale(0);
		}
		
		if (m.xzVel.length() > speedCap) {
			m.xzVel.scale(speedCap/m.xzVel.length());
		}else if (m.xzVel.length() < -speedCap) {
			m.xzVel.scale(-speedCap/m.xzVel.length());
		}
	}
	
	private static void collisionCheck(Mesh m, Mesh m2) {
//		if (m.xzVel.x)
	}
	
	private static void gravity(Mesh m) {
		if (m.yVel > ySpeedCap)
			m.yVel -= gravity;
		else
			m.yVel = ySpeedCap;
	}
	
	public static void update(Mesh m, ArrayList<Mesh> meshes) {
		for (Mesh mesh : meshes) {
			collisionCheck(m,mesh);
		}
		
		friction(m);
//		if (!m.isOnGround)
//			gravity(m);
	}
}