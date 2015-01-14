package com.misabiko.LWJGLGameEngine.Core;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;

public class Controls {
	private static boolean Esc = false;
	private static float MouseSensibility = 1/500f;
	private static float MouseWheelSensibility = 1/4000f;
	private static Vector3f preVel = new Vector3f(0f,0f,0f);
	
	private static final Vector3f frontVel 	= new Vector3f(0f,0f,-1f);
	private static final Vector3f backVel	= new Vector3f(0f,0f,1f);
	private static final Vector3f leftVel	= new Vector3f(-1f,0f,0f);
	private static final Vector3f rightVel	= new Vector3f(1f,0f,0f);
	
	public static void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (!Esc) {
				Mouse.setGrabbed(!Mouse.isGrabbed());
			}
			Esc = true;
		}else {
			Esc = false;
		}
		
		if (Mouse.isGrabbed()) {
			Camera.angleX += ((float) Mouse.getDY()*MouseSensibility);
			Camera.angleY -= ((float) Mouse.getDX()*MouseSensibility);
			
			if (Camera.angleX > Math.PI*2) {
				Camera.angleX = Camera.angleX - (float) (Math.PI*2);
			}else if (Camera.angleX < -(Math.PI*2)) {
				Camera.angleX = Camera.angleX + (float) (Math.PI*2);
			}
			if (Camera.angleY > Math.PI*2) {
				Camera.angleY = Camera.angleY - (float) (Math.PI*2);
			}else if (Camera.angleY < -(Math.PI*2)) {
				Camera.angleY = Camera.angleY + (float) (Math.PI*2);
			}
		
		}else {
			Mouse.getDX();
			Mouse.getDY();
		}
		
		Camera.zoomVel -= ((float) Mouse.getDWheel()*MouseWheelSensibility);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Core.cuby.angleY = Camera.angleY;
			
			preVel.add(frontVel);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			Core.cuby.angleY = Camera.angleY;
			
			preVel.add(leftVel);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			Core.cuby.angleY = Camera.angleY;
			
			preVel.add(backVel);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Core.cuby.angleY = Camera.angleY;
			
			preVel.add(rightVel);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Core.cuby.controller.jump();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Physic.speedCap = 0.04f;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			Physic.speedCap = 0.2f;
		}else {
			Physic.speedCap = 0.1f;
		}
		
		if (preVel.length() != 0f) {
			preVel.normalize();
			preVel.scale(Core.cuby.speed);
			Core.cuby.vel.add(preVel);
			preVel.set(0f,0f,0f);
		}
	}

}
