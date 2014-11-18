package com.misabiko.LWJGLGameEngine.Shaders;

import static org.lwjgl.opengl.GL20.*;

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
