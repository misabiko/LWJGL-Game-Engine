package com.misabiko.LWJGLGameEngine.Shaders;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class Program {
	
	public int id, projectionMatrixLocation, viewMatrixLocation, modelMatrixLocation = 0;
	public int[] shaderIds;
	
	public Program(int[] shaderIds) {
		this.shaderIds = shaderIds;
		
		id = glCreateProgram();
		for (int i : shaderIds) {
			glAttachShader(id, i);
		}
		
		glBindAttribLocation(id, 0, "in_Position");
		glBindAttribLocation(id, 1, "in_Color");
		glBindAttribLocation(id, 2, "in_TextureCoord");
		
		glLinkProgram(id);
		glValidateProgram(id);
		
		projectionMatrixLocation = glGetUniformLocation(id, "projectionMatrix");
		viewMatrixLocation = glGetUniformLocation(id, "viewMatrix");
		modelMatrixLocation = glGetUniformLocation(id, "modelMatrix");
	}
	
	public void cleanUp() {
		for (int i : shaderIds) {
			glDetachShader(id,i);
		}
		glDeleteProgram(id);
	}
}
