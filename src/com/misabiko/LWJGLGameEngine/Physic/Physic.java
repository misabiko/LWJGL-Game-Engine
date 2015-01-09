package com.misabiko.LWJGLGameEngine.Physic;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;

public class Physic {
	private static float speedCap = 0.1f;
	private static float friction = 0.25f;
	public static float gravity = -0.1f;
	
	private static void friction(GameObject obj) {
		if (obj.vel.length() >= (friction/200)) {
			obj.vel.scale(1f-friction);
		}else if (obj.vel.length() <= (-friction/200)) {
			obj.vel.scale(1f-friction);
		}else {
			obj.vel.scale(0);
		}
		
		if (obj.vel.length() > speedCap) {
			obj.vel.scale(speedCap/obj.vel.length());
		}else if (obj.vel.length() < -speedCap) {
			obj.vel.scale(-speedCap/obj.vel.length());
		}
	}
	
	public static void update(GameObject obj) {
		friction(obj);
	}
}