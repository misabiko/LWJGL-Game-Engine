package com.misabiko.LWJGLGameEngine.Core;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.GameObjects.Axis;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Platform;
import com.misabiko.LWJGLGameEngine.Physic.JBulletHandler;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";

	private static DiscreteDynamicsWorld dynamicsWorld;
	
	private ArrayList<GameObject> objs = new ArrayList<GameObject>();
//	private Camera camera;
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
		dynamicsWorld = JBulletHandler.init(dynamicsWorld);
		
		init();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			input();
			
			dynamicsWorld.stepSimulation(1/60f, 10);
			
			for (GameObject obj : objs) {
				obj.update();
				OpenGLHandler.render(obj);
			}
			
			Display.sync(60);
			Display.update();
		}
		
		OpenGLHandler.cleanUp(objs);
		JBulletHandler.cleanUp();
		cleanUp();
	}
	
	private void init() {
		Platform ground = new Platform(-3f, -2f, -2f, 8f,0.5f,4f);
		objs.add(ground);
		dynamicsWorld.addRigidBody(ground.rb);
		
		cuby = new Cuby();
		objs.add(cuby);
		dynamicsWorld.addRigidBody(cuby.rb);
		
		objs.add(new Axis(0, 0, 0, 10f, 0, 0, Color.RED));
		objs.add(new Axis(0, 0, 0, 0f, 10f, 0f, Color.GREEN));
		objs.add(new Axis(0, 0, 0, 0f, 0f, -10f, Color.BLUE));
		
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			if (!F5isHeld) {
				if (Camera.freeMovement) {
					Camera.angleX = cuby.angleX;
					Camera.angleY = cuby.angleY;
				}
				Camera.freeMovement = !Camera.freeMovement;
			}
			F5isHeld = true;
		}else {
			F5isHeld = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cuby.angleY = Camera.angleY;
			
			cuby.vel.x += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			cuby.angleY = Camera.angleY;
			
			cuby.vel.x -= cuby.speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cuby.jump();
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Vector3f.sub(cuby.pos, new Vector3f(0,Camera.speed,0), cuby.pos);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cuby.angleY = Camera.angleY;
			
			cuby.vel.z += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			cuby.angleY = Camera.angleY;
			
			cuby.vel.z -= cuby.speed;
		}
		
	}
	
	public void cleanUp() {
		dynamicsWorld.destroy();
	}
}