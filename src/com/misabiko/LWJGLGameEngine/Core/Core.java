package com.misabiko.LWJGLGameEngine.Core;

import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.misabiko.LWJGLGameEngine.GameObjects.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.DetectionArea;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Sky;
import com.misabiko.LWJGLGameEngine.Physic.JBulletHandler;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Light;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Sun;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";

	public static DiscreteDynamicsWorld dw;
	
	public static Cuby cuby;
	public static Sky skybox;
	
//	Current task
//		TODO UI
	
//	Short term todos
//		TODO Simple dummy npc
//		TODO Attacks
	
//	Long term todos
//		TODO Smooth rotating for Cuby, Cube world style
//		TODO Implement separate textures per-face on mesh
//		TODO Reimplement lines and axis (axii? axises? axi? axes?)
//		TODO learn to manage the projection matrix
//		TODO Transition LWJGL 2 to 3
	
	public Core() {
		init();
//		run();
		originalRun();
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
				
				dw.stepSimulation(1/60f, 3);
				
				for (GameObject obj : GameObject.objs) {
					if (!obj.mesh.isTransparent) {
						obj.update();
						OpenGLHandler.render(obj);
					}
				}
				
				GL11.glDepthMask(false);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				
				for (GameObject obj : GameObject.objs) {
					if (obj.mesh.isTransparent) {
						obj.update();
						OpenGLHandler.render(obj);
					}
				}
				
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
				
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
	
	private void originalRun() {
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			Controls.update();
			
			dw.stepSimulation(1/60f, 3);
			
			for (GameObject obj : GameObject.objs) {
				if (!obj.mesh.isTransparent) {
					obj.update();
					OpenGLHandler.render(obj);
				}
			}
			
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			for (GameObject obj : GameObject.objs) {
				if (obj.mesh.isTransparent) {
					obj.update();
					OpenGLHandler.render(obj);
				}
			}
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			
			Display.sync(60);
			Display.update();
		}
		cleanUp();
	}
	
	private void init() {
		OpenGLHandler.init(TITLE, WIDTH, HEIGHT);
		dw = JBulletHandler.init(dw);
		
		skybox = new Sky();
		
		Block ground = new Block(0f, 0f, 0f, 50f,5f,50f);
		dw.addRigidBody(ground.rb);
		
//		Platform lightBlock = new Platform(0f, 3f, 0f, 1f,1f,1f);
//		objs.add(lightBlock);
//		dw.addRigidBody(lightBlock.rb);
//		
		Block block = new Block(10f, 3f, -8f, 1f, 1f, 1f);
		dw.addRigidBody(block.rb);
		
		Block block2 = new Block(0f, 3f, -2f, 1f, 1f, 1f);
		dw.addRigidBody(block2.rb);
		
		DetectionArea da = new DetectionArea(5f, 5f, 5f, 3f,3f,3f);
		dw.addCollisionObject(da.go);
		
		try {
			cuby = new Cuby();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dw.addCollisionObject(cuby.go);
		dw.addAction(cuby.controller);
		
		Light.lights.add(new Light(new Vector4f(0f,0f,0f,1f), new Vector3f(1f,1f,1f), 0.005f, 0.5f, 45f, new Vector3f(0f,-1f,0f)));
		Light.lights.add(new Sun(new Vector3f(100f,100f,100f)));
//		Light.lights.add(new Sun(new Vector3f(-100f,100f,100f)));
//		Light.lights.add(new Sun(new Vector3f(100f,100f,-100f)));
//		Light.lights.add(new Sun(new Vector3f(-100f,100f,-100f)));
		
		OpenGLHandler.initBuffers();
	}
	
	public void cleanUp() {
		OpenGLHandler.cleanUp();
		JBulletHandler.cleanUp();
	
		dw.destroy();
	}
}