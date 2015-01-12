package com.misabiko.LWJGLGameEngine.Core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Platform;
import com.misabiko.LWJGLGameEngine.Physic.JBulletHandler;
import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;
import com.misabiko.LWJGLGameEngine.Rendering.Light;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";

	private static DiscreteDynamicsWorld dynamicsWorld;
	
	private ArrayList<GameObject> objs = new ArrayList<GameObject>();
//	private Camera camera;
	private Cuby cuby;
	public static ArrayList<Light> lights = new ArrayList<Light>();
	private boolean EscIsHeld = false;
	
//	Current task
//		Make "Rendering" package and add OpenGLHandler and Meshes in it
	
//	Short term todos
//		Prevent 360 cam spins
//		TODO make a control class
	
//	Long term todos
//		TODO Implement separate textures per-face on mesh
//		TODO Remove those .* imports, I don't like those
//		TODO Reimplement lines and axis (axii? axises? axi?)
//		TODO learn to manage the projection matrix
//		TODO Custom (Blender or MagicaVoxel) models?
//		TODO well, you know, game stuff
//		TODO Transition LWJGL 2 to 3
	
	public Core() {
		init();
		
		run();
		
		cleanUp();
	}
	
	private void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

//		int renders = 0;
//		int updates = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
//				updates++;
				
				glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
				input();
				
				dynamicsWorld.stepSimulation(1/60f, 3);
				
				for (GameObject obj : objs) {
					obj.update();
					OpenGLHandler.render(obj);
				}
				
//				cuby.update();
//				OpenGLHandler.render(cuby);
				
				delta -= 1;
				shouldRender = true;
			}

			if (shouldRender) {
//				renders++;
				Display.update();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
//				System.out.println(renders + ", " + updates);
//				renders = 0;
//				updates = 0;
			}

		}
	}
	
	private void init() {
		OpenGLHandler.init(TITLE, WIDTH, HEIGHT);
		dynamicsWorld = JBulletHandler.init(dynamicsWorld);
		
		Platform ground = new Platform(-3f, -2f, -2f, 16f,0.5f,16f);
		objs.add(ground);
		dynamicsWorld.addRigidBody(ground.rb);
		
//		Platform lightBlock = new Platform(0f, 3f, 0f, 1f,1f,1f);
//		objs.add(lightBlock);
//		dynamicsWorld.addRigidBody(lightBlock.rb);
		
		Platform block = new Platform(-3f, -1.5f, -2f, 1f, 1.5f, 3f);
		objs.add(block);
		dynamicsWorld.addRigidBody(block.rb);
		
		Platform block2 = new Platform(0f, -1.5f, -2f, 1f, 1.5f, 3f);
		objs.add(block2);
		dynamicsWorld.addRigidBody(block2.rb);
		
		cuby = new Cuby();
		objs.add(cuby);
		dynamicsWorld.addCollisionObject(cuby.go);
		dynamicsWorld.addAction(cuby.controller);
		
		lights.add(new Light(-6f,-1.5f,-1f, 1f));
		lights.add(new Light(10f,100f,10f, 0f));
		
//		objs.add(new Axis(0, 0, 0, 10f, 0, 0, Color.RED));
//		objs.add(new Axis(0, 0, 0, 0f, 10f, 0f, Color.GREEN));
//		objs.add(new Axis(0, 0, 0, 0f, 0f, -10f, Color.BLUE));
		
		OpenGLHandler.initBuffers(objs);
	}
	
	private void input() {
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
			cuby.angleY = Camera.angleY;
			
			cuby.vel.x += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			cuby.angleY = Camera.angleY;
			
			cuby.vel.x -= cuby.speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cuby.controller.jump();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Physic.speedCap = 0.04f;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			Physic.speedCap = 0.2f;
		}else {
			Physic.speedCap = 0.1f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cuby.angleY = Camera.angleY;
			
			cuby.vel.z -= cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			cuby.angleY = Camera.angleY;
			
			cuby.vel.z += cuby.speed;
		}
		
	}
	
	public void cleanUp() {
		OpenGLHandler.cleanUp(objs);
		JBulletHandler.cleanUp();
	
		dynamicsWorld.destroy();
	}
}