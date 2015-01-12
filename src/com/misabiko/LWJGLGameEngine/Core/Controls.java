package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;

public class Controls {
	private static boolean EscIsHeld = false;
	
	public static void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (!EscIsHeld) {
				Mouse.setGrabbed(!Mouse.isGrabbed());
			}
			EscIsHeld = true;
		}else {
			EscIsHeld = false;
		}
		
		if (Mouse.isGrabbed()) {
			Camera.angleX += ((float) Mouse.getDY()/500);
			Camera.angleY -= ((float) Mouse.getDX()/500);
			
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
		
		Camera.zoom -= ((float) Mouse.getDWheel()/1000);
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Core.cuby.angleY = Camera.angleY;
			
			Core.cuby.vel.x += Core.cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			Core.cuby.angleY = Camera.angleY;
			
			Core.cuby.vel.x -= Core.cuby.speed;
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Core.cuby.angleY = Camera.angleY;
			
			Core.cuby.vel.z -= Core.cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			Core.cuby.angleY = Camera.angleY;
			
			Core.cuby.vel.z += Core.cuby.speed;
		}
		
	}

}
