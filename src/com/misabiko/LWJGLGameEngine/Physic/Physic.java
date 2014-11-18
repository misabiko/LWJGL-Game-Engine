package com.misabiko.LWJGLGameEngine.Physic;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;

public class Physic {
	private static float ySpeedCap = -0.2f;
	private static float speedCap = 0.1f;
	private static float gravity = 0.05f;
	private static float friction = 0.08f;
	
	private static void friction(GameObject obj) {
		if (obj.xzVel.length() >= (friction/20)) {
			obj.xzVel.scale(1f-friction);
		}else if (obj.xzVel.length() <= (-friction/20)) {
			obj.xzVel.scale(1f-friction);
		}else {
			obj.xzVel.scale(0);
		}
		
		if (obj.xzVel.length() > speedCap) {
			obj.xzVel.scale(speedCap/obj.xzVel.length());
		}else if (obj.xzVel.length() < -speedCap) {
			obj.xzVel.scale(-speedCap/obj.xzVel.length());
		}
	}
	
	private static void gravity(GameObject obj) {
		if (obj.yVel > ySpeedCap)
			obj.yVel -= gravity;
		else
			obj.yVel = ySpeedCap;
	}
	
	public static boolean isColliding(GameObject obj1, GameObject obj2) {
		for (Vector3f sp : obj1.hitbox.getSP()) {
			if (obj1.hitbox.isPointInside(sp))
				return true;
		}
		
		for (Vector3f sp : obj1.hitbox.getSP()) {
			if (obj1.hitbox.isPointInside(sp))
				return true;
		}
		
		return false;
	}
	
	public static void update(GameObject obj, ArrayList<GameObject> objs) {
		friction(obj);
		if (obj.hitbox != null) {
			for (GameObject object : objs) {
				if (isColliding(obj, object) && object.hitbox != null)
					System.out.println("Collision");
			}
		}
	}
}