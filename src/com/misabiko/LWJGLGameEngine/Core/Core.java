package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.opengl.Display;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Platform;
import com.misabiko.LWJGLGameEngine.Physic.JBulletHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Light;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";

	private static DiscreteDynamicsWorld dynamicsWorld;
	
	public static Cuby cuby;
	
//	Current task
//		TODO Custom (Blender or MagicaVoxel) models?
	
//	Short term todos
//		TODO Detection areas
//		TODO Simple dummy npc
//		TODO Attacks
//		TODO UI
	
//	Long term todos
//		TODO Smooth rotating for Cuby, Cube world style
//		TODO Implement separate textures per-face on mesh
//		TODO Reimplement lines and axis (axii? axises? axi? axes?)
//		TODO learn to manage the projection matrix
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
				Controls.update();
				
				dynamicsWorld.stepSimulation(1/60f, 3);
				
				for (GameObject obj : GameObject.objs) {
					obj.update();
					OpenGLHandler.render(obj);
				}
				
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
		dynamicsWorld.addRigidBody(ground.rb);
		
//		Platform lightBlock = new Platform(0f, 3f, 0f, 1f,1f,1f);
//		objs.add(lightBlock);
//		dynamicsWorld.addRigidBody(lightBlock.rb);
		
		Platform block = new Platform(-3f, -1.5f, -2f, 1f, 1.5f, 3f);
		dynamicsWorld.addRigidBody(block.rb);
		
		Platform block2 = new Platform(0f, -1.5f, -2f, 1f, 1.5f, 3f);
		dynamicsWorld.addRigidBody(block2.rb);
		
		cuby = new Cuby();
		dynamicsWorld.addCollisionObject(cuby.go);
		dynamicsWorld.addAction(cuby.controller);
		
		Light.lights.add(new Light(-6f,-1.5f,-1f, 1f));
		Light.lights.add(new Light(10f,100f,10f, 0f));
		
		OpenGLHandler.initBuffers();
	}
	
	public void cleanUp() {
		OpenGLHandler.cleanUp();
		JBulletHandler.cleanUp();
	
		dynamicsWorld.destroy();
	}
}