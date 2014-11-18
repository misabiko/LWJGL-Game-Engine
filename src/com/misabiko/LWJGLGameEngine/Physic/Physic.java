package com.misabiko.LWJGLGameEngine.Physic;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

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
	
	private static void gravity(Mesh m) {
		if (m.yVel > ySpeedCap)
			m.yVel -= gravity;
		else
			m.yVel = ySpeedCap;
	}
	
	public static boolean isColliding(Mesh m1, Mesh m2) {
		for (Vector3f sp : m1.hitbox.getSP()) {
			if (m2.hitbox.isPointInside(sp))
				return true;
		}
		
		for (Vector3f sp : m2.hitbox.getSP()) {
			if (m1.hitbox.isPointInside(sp))
				return true;
		}
		
		return false;
	}
	
	public static void update(Mesh m, ArrayList<Mesh> meshes) {
		
		friction(m);
		if (m.hitbox != null) {
			for (Mesh mesh : meshes) {
				if (isColliding(m, mesh) && mesh.hitbox != null)
					System.out.println("Collision");
			}
		}
	}
}