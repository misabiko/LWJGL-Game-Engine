package com.misabiko.LWJGLGameEngine.Core;

import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.misabiko.LWJGLGameEngine.GameObjects.Cuby;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Platform;
import com.misabiko.LWJGLGameEngine.GameObjects.Sky;
import com.misabiko.LWJGLGameEngine.Physic.JBulletHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Light;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Sun;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.World.World;

import static org.lwjgl.opengl.GL11.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";

	public static DiscreteDynamicsWorld dw;
	
	public static World world;
	public static Cuby cuby;
	public static Sky skybox;
	
//	Current task
//		TODO Multiple normals per vertex? binding normals to indices maybe?
	
	
//	Short term todos
//		TODO Simple dummy npc
//		TODO Attacks
//		TODO UI
	
//	Long term todos
//		TODO sparse voxel octree thing
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
	
	private void init() {
		
		OpenGLHandler.init(TITLE, WIDTH, HEIGHT);
		dw = JBulletHandler.init(dw);
		
		world = new World(2);

//		new Platform(0f, 0f, 0f, 10f, 2f, 10f);
		skybox = new Sky();
		
		try {
			cuby = new Cuby();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dw.addCollisionObject(cuby.co);
		dw.addAction(cuby.controller);
		
		new Light(new Vector4f(0f, 15f, 0f, 1f), new Vector3f(1f, 1f, 1f), 0.2f, 0.3f, 360f, new Vector3f(0f,-1f,0f));
		new Sun(new Vector3f(100f,100f,100f));
		
		OpenGLHandler.initVAOs();
	}
	
	private void run() {
		int fps = 0;
		long currentTime = System.currentTimeMillis();
		long lastTime = currentTime;
		long delta = 0;
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			Controls.update();
			
			dw.stepSimulation(1/60f, 3);
			
			for (GameObject obj : GameObject.shouldUpdate)
				obj.update();
			
			for (GameObject entity : GameObject.entities)
				entity.update();
			
			GameObject.shouldUpdate.clear();
			
			for (GameObject obj : Camera.shouldRender()) {
				if (obj.mesh.isTransparent) {
					GL11.glDepthMask(false);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					
					OpenGLHandler.render(obj.mesh);
			
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDepthMask(true);
				}else
					OpenGLHandler.render(obj.mesh);
			}
			
			for (Mesh mesh : world.getMeshes(false)) {
				if (mesh.isTransparent) {
					GL11.glDepthMask(false);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					
					OpenGLHandler.render(mesh);
			
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDepthMask(true);
				}else
					OpenGLHandler.render(mesh);
			}
			
			if (currentTime - lastTime > 1000) {
				Display.setTitle(TITLE+" - FPS: "+fps+" - Entities: "+Camera.shouldRender().size()+" - Position: "+cuby.getPosition().toString());
				fps = 0;
				lastTime = System.currentTimeMillis();
			}
			
			fps++;
			
			Display.update();
			Display.sync(60);
			
			currentTime = System.currentTimeMillis();
		}
	}
	
	public void cleanUp() {
		OpenGLHandler.cleanUp();
		JBulletHandler.cleanUp();
	
		dw.destroy();
	}
}