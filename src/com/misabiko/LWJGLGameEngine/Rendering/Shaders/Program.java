package com.misabiko.LWJGLGameEngine.Rendering.Shaders;

import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class Program {
	
	public int id;
	public int[] shaderIds;
	
	public Program(int[] shaderIds) {
		this.shaderIds = shaderIds;
		
		id = glCreateProgram();
		for (int i : shaderIds) {
			glAttachShader(id, i);
		}
		
		glBindFragDataLocation(id, 0, "out_Color");
		
		if (glGetProgramInfoLog(id, 5).length() > 0)
			System.out.println(glGetProgramInfoLog(id, 10000));
		for (int i : shaderIds) {
			if (glGetShaderInfoLog(i, 5).length() > 0)
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
