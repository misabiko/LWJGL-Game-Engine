package com.misabiko.LWJGLGameEngine.Core;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.Axis;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Platform;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";
	
	private ArrayList<GameObject> objs = new ArrayList<GameObject>();
	private Camera camera;
	private Cuby cuby;
	
	private boolean F5isHeld, EscIsHeld = false;
	
//	Current task
//		TODO Collision System
//			Making a cylinder shaped detection space
//			if 2 points are closer than r1+r2, then there is collision
	
//	Short term todos
//		Prevent 360 cam spins
	
//	Long term todos
//		TODO make a light shader/engine ( or at least something to see the objes' borders )
//		TODO learn to manage the projection matrix
//		TODO Custom (Blender or MagicaVoxel) models?
//		TODO well, you know, game stuff
	
	public Core() {
		OpenGLHandler.init(TITLE, WIDTH, HEIGHT);
		
		init();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			input();
			
			for (GameObject obj : objs) {
				update(obj);
				OpenGLHandler.render(obj, camera);
			}
			
			Display.sync(60);
			Display.update();
		}
		
		OpenGLHandler.cleanUp(objs);
	}
	
	private void init() {
		cuby = new Cuby();
		objs.add(cuby);
		
		objs.add(new Platform(-3f, -2f, -2f, 8f,0.5f,4f));
		
		objs.add(new Axis(0, 0, 0, 10f, 0, 0, Color.RED));
		objs.add(new Axis(0, 0, 0, 0f, 10f, 0f, Color.GREEN));
		objs.add(new Axis(0, 0, 0, 0f, 0f, -10f, Color.BLUE));
		
		camera = new Camera(-1f, -1.5f, -1f);
		
		OpenGLHandler.initVBOs(objs);
	}
	
	private void input() {
		
//		Dat clean input method :O
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (!EscIsHeld) {
				Mouse.setGrabbed(!Mouse.isGrabbed());
			}
			EscIsHeld = true;
		}else {
			EscIsHeld = false;
		}
		
		if (Mouse.isGrabbed()) {
			camera.angleX += ((float) Mouse.getDY()/500);
			camera.angleY -= ((float) Mouse.getDX()/500);
			
			if (camera.angleX > Math.PI*2) {
				camera.angleX = camera.angleX - (float) (Math.PI*2);
			}else if (camera.angleX < -(Math.PI*2)) {
				camera.angleX = camera.angleX + (float) (Math.PI*2);
			}
			if (camera.angleY > Math.PI*2) {
				camera.angleY = camera.angleY - (float) (Math.PI*2);
			}else if (camera.angleY < -(Math.PI*2)) {
				camera.angleY = camera.angleY + (float) (Math.PI*2);
			}
		
		}else {
			Mouse.getDX();
			Mouse.getDY();
		}
		
		camera.zoom -= ((float) Mouse.getDWheel()/1000);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			if (!F5isHeld) {
				if (camera.freeMovement) {
					camera.angleX = cuby.angleX;
					camera.angleY = cuby.angleY;
				}
				camera.freeMovement = !camera.freeMovement;
			}
			F5isHeld = true;
		}else {
			F5isHeld = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cuby.angleY = camera.angleY;
			
			cuby.xzVel.x += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			cuby.angleY = camera.angleY;
			
			cuby.xzVel.x -= cuby.speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cuby.jump();
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Vector3f.sub(cuby.pos, new Vector3f(0,camera.speed,0), cuby.pos);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cuby.angleY = camera.angleY;
			
			cuby.xzVel.y += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			cuby.angleY = camera.angleY;
			
			cuby.xzVel.y -= cuby.speed;
		}
		
	}
	
	private void update(GameObject obj) {
		if (obj instanceof Cuby)	//Will change cuby for "movables"
			((Cuby) obj).update(objs);
		else
			obj.update();
		
		camera.update(cuby);
	}
}