package com.misabiko.LWJGLGameEngine.Core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Meshes.Cuby;
import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Meshes.TexturedVertex;
import com.misabiko.LWJGLGameEngine.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Shaders.Program;
import com.misabiko.LWJGLGameEngine.Utils.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";
	
	private ArrayList<Mesh> Meshes = new ArrayList<Mesh>();
	private Camera camera;
	private Cuby cuby;
	
	private boolean F5isHeld, EscIsHeld = false;
	
//	Current task
//	TODO make a line class
	
//	Short term todos
//	TODO in mesh class, join xVel and zVel into a vector and manipulate length instead
//	TODO Collision System
	
//	Long term todos
//	TODO make a light shader/engine ( or at least something to see the meshes' borders )
//	TODO learn to manage the projection matrix
//	TODO Custom (Blender or MagicaVoxel) models?
//	TODO well, you know, game stuff
	
	public Core() {
		OpenGLHandler.init(TITLE, WIDTH, HEIGHT);
		
		init();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			input();
			
			for (Mesh mesh : Meshes) {
				update(mesh);
				OpenGLHandler.render(mesh, camera);
			}
			
			Display.sync(60);
			Display.update();
		}
		
		OpenGLHandler.cleanUp(Meshes);
	}
	
	private void init() {
		cuby = new Cuby();
		Meshes.add(cuby);
		
		Meshes.add(new Box(-3f, -2f, -2f, 8f,0.5f,4f));
		
		camera = new Camera(-1f, -1.5f, -1f);
		
		OpenGLHandler.initVBOs(Meshes);
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
					camera.angleZ = cuby.angleZ;
				}
				camera.freeMovement = !camera.freeMovement;
			}
			F5isHeld = true;
		}else {
			F5isHeld = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cuby.angleY = camera.angleY;
			
			cuby.xVel += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			cuby.angleY = camera.angleY;
			
			cuby.xVel -= cuby.speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cuby.jump();
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Vector3f.sub(cuby.pos, new Vector3f(0,camera.speed,0), cuby.pos);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cuby.angleY = camera.angleY;
			
			cuby.zVel += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			cuby.angleY = camera.angleY;
			
			cuby.zVel -= cuby.speed;
		}
		
	}
	
	private void update(Mesh mesh) {
		
		mesh.update();
		camera.update(cuby);
		
	}
}