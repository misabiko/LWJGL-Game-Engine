package com.misabiko.LWJGLGameEngine.Core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	private int vaoId, vboId, vbocId, vboiId, vertShaderId, fragShaderId, programId = 0;
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
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle(TITLE);
			Display.create(pixelFormat,contextAttribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		glClearColor(0.0f,0.0f,0.0f,0.0f);
		
		glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	private void init() {
		cube = new Cube(-0.5f,-0.5f,0.5f,1f,1f,1f);

		vertShaderId = loadShader("vertex.glsl",GL_VERTEX_SHADER);
		fragShaderId = loadShader("fragment.glsl",GL_FRAGMENT_SHADER);
		
		programId = glCreateProgram();
		glAttachShader(programId, vertShaderId);
		glAttachShader(programId, fragShaderId);
		
		glBindAttribLocation(programId, 0, "in_Position");
		glBindAttribLocation(programId, 1, "in_Color");
		
		glLinkProgram(programId);
		glValidateProgram(programId);
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			vboId = glGenBuffers();
			
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
				glBufferData(GL_ARRAY_BUFFER,cube.verticesBuffer,GL_STATIC_DRAW);
				
				glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
				
			vbocId = glGenBuffers();
			
			glBindBuffer(GL_ARRAY_BUFFER, vbocId);
				glBufferData(GL_ARRAY_BUFFER, cube.colorsBuffer, GL_STATIC_DRAW);
				
				glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER,0);
			
			vboiId = glGenBuffers();
				
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, cube.indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			
		glBindVertexArray(0);
	}
	
	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
//		TODO Look further into BufferedReader and such
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/com/misabiko/LWJGLGameEngine/Shaders/"+filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Couldn't read the file");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		return shaderID;
	}
	
	private void update(Cube c) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUseProgram(programId);
		
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
			glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				
				glDrawElements(GL_TRIANGLES, c.indicesCount, GL_UNSIGNED_BYTE, 0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			glBindBuffer(GL_ARRAY_BUFFER,0);
			
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		glUseProgram(0);
	}
	
	private void cleanUp(Cube c) {
		glUseProgram(0);
		glDetachShader(programId,vertShaderId);
		glDetachShader(programId,fragShaderId);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glDeleteBuffers(vboId);
		glDeleteBuffers(vbocId);
		glDeleteBuffers(vboiId);
		glDeleteProgram(programId);
		
		Display.destroy();
	}
}
