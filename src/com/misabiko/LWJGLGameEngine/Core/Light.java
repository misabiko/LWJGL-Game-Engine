package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Vector3f;

//import java.nio.FloatBuffer;

//import org.lwjgl.BufferUtils;
//import static org.lwjgl.opengl.GL15.glGenBuffers;
//import static org.lwjgl.opengl.GL15.glBindBuffer;
//import static org.lwjgl.opengl.GL15.glBufferData;
//import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
//import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;;

public class Light {
//	private FloatBuffer buffer;
//	public int bufferId = 0;
	public Vector3f position, intensities;
	
	public Light(float x, float y, float z, float r, float g, float b) {
		position = new Vector3f(x,y,z);
		intensities = new Vector3f(r,g,b);
//		buffer = BufferUtils.createFloatBuffer(6);
//		buffer.put(new float[] {x,y,z,r,g,b});
//		buffer.flip();
//		
//		bufferId = glGenBuffers();
//		
//		glBindBuffer(GL_UNIFORM_BUFFER, bufferId);
//			glBufferData(bufferId, buffer, GL_STATIC_DRAW);
//		glBindBuffer(GL_UNIFORM_BUFFER, 0);
	}
	
	public Light(float x, float y, float z) {
		this(x, y, z, 1f, 1f, 1f);
	}
}
