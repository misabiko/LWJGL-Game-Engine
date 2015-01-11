package com.misabiko.LWJGLGameEngine.Shaders;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Program {
	
	public int id;
	public int[] shaderIds;
	
	public Program(int[] shaderIds) {
		this.shaderIds = shaderIds;
		
		id = glCreateProgram();
		for (int i : shaderIds) {
			glAttachShader(id, i);
		}
		
//		glBindAttribLocation(id, 0, "in_Position");
//		glBindAttribLocation(id, 1, "in_Color");
//		glBindAttribLocation(id, 2, "in_TextureCoord");
//		glBindAttribLocation(id, 3, "in_Normal");
		
		glBindFragDataLocation(id, 0, "out_Color");
		
		System.out.println(glGetProgramInfoLog(id, 10000));
		for (int i : shaderIds) {
			System.out.println(glGetShaderInfoLog(i, 10000));
		}
		
		glLinkProgram(id);
		glValidateProgram(id);
	}
	
	public void cleanUp() {
		for (int i : shaderIds) {
			glDetachShader(id,i);
		}
		glDeleteProgram(id);
	}
}
