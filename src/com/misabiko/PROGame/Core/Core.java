package com.misabiko.PROGame.Core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "PROGame";
	private int vaoId = 0;
	private int vboId = 0;
	private int vboiId = 0;
	private Cube cube;
	
	public Core() {
		initGL();
		init();
		
		while (!Display.isCloseRequested()) {
			update(cube);
			
			Display.sync(60);
			Display.update();
		}
		cleanUp(cube);
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
	}
	
	private void init() {
		cube = new Cube(-0.5f,-0.5f,0.5f,1f,1f,1f);
		addVertices(cube);
	}
	
	public void addVertices(Cube c) {
			
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			vboId = glGenBuffers();
			
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
				glBufferData(GL_ARRAY_BUFFER,c.verticesBuffer,GL_STATIC_DRAW);
				
				glVertexPointer(3,GL_FLOAT,0,0);
				
			glBindBuffer(GL_ARRAY_BUFFER,0);
			
			vboiId = glGenBuffers();
				
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, c.indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			
		glBindVertexArray(0);
	}
	
	private void update(Cube c) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glBindVertexArray(vaoId);
		
			glEnableClientState(GL_VERTEX_ARRAY);
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
			glVertexPointer(3,GL_FLOAT,0,0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				
				glDrawElements(GL_TRIANGLES, c.indicesCount, GL_UNSIGNED_BYTE, 0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			glBindBuffer(GL_ARRAY_BUFFER,0);
			glDisableClientState(GL_VERTEX_ARRAY);
		glBindVertexArray(0);
	}
	
	private void cleanUp(Cube c) {
		glDisableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboId);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboiId);
		Display.destroy();
	}
}
