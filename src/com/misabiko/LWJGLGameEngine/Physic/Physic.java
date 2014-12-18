package com.misabiko.LWJGLGameEngine.Physic;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Physic.Hitboxes.Hitbox;

public class Physic {
	
//	private static void friction(GameObject obj) {
//		if (obj.xzVel.length() >= (friction/20)) {
//			obj.xzVel.scale(1f-friction);
//		}else if (obj.xzVel.length() <= (-friction/20)) {
//			obj.xzVel.scale(1f-friction);
//		}else {
//			obj.xzVel.scale(0);
//		}
//		
//		if (obj.xzVel.length() > speedCap) {
//			obj.xzVel.scale(speedCap/obj.xzVel.length());
//		}else if (obj.xzVel.length() < -speedCap) {
//			obj.xzVel.scale(-speedCap/obj.xzVel.length());
//		}
//	}
//	
//	private static void gravity(GameObject obj) {
//		if (obj.yVel > ySpeedCap)
//			obj.yVel -= gravity;
//		else
//			obj.yVel = ySpeedCap;
//	}
	
	public static boolean isColliding(Hitbox hb1, Hitbox hb2) {

		for (Vector3f vec : hb1.getSP()) {
			System.out.println(vec.toString());
		}

		System.out.println();
		
		for (Vector3f vec : hb2.getSP()) {
			System.out.println(vec.toString());
		}
//		
		for (Vector3f sp : hb1.getSP()) {
			if (hb2.isPointInside(sp)) {
				System.out.println("boop1");
				return true;
			}
		}
		
		for (Vector3f sp : hb2.getSP()) {
			if (hb1.isPointInside(sp)) {
				System.out.println("foo");
				return true;
			}
		}
		
		return false;
	}
	
	public static void update(GameObject obj) {
//		friction(obj);
//		for (Hitbox hb : Hitbox.Hitboxes) {
//			if (hb != obj.hitbox) {
//				if (isColliding(obj.hitbox, hb)) {
//					System.out.println("Collision");
//				}
//			}
//		}
	}
}