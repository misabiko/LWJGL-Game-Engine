package com.misabiko.PROGame.Core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "PROGame";
	private int vaoId;
	
	public Core() {
		initGL();
		
		init();
		
		while (!Display.isCloseRequested()) {
			update();
			
			Display.sync(60);
			Display.update();
		}
	}
	
	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle(TITLE);
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glClearColor(0.0f,0.0f,0.0f,0.0f);
		
		glViewport(0, 0, WIDTH, HEIGHT);
		
		vaoId = glGenVertexArrays();
	}
	
	private void init() {
		Cube cube = new Cube(-0.5f,-0.5f,0f,1f,1f,1f);
		cube.addVertices(vaoId);
		
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	private void update() {
		glClear(GL_COLOR_BUFFER_BIT);
		
//		Binding the VAO (with the VBOs in it)
		glBindVertexArray(vaoId);
		
//		Select the index 0 of the VAO (the Cube)
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}
