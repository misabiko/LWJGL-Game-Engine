package com.misabiko.LWJGLGameEngine.Core;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public abstract class Mesh {
	public Matrix4f modelMatrix;
	
	public Vector3f pos;
	public Vector3f vel = new Vector3f();
	
	public float angleX, angleY, angleZ = 0;
	
	public boolean isTextured = true;
	
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public Texture texture = defaultTexture;

	protected static Texture defaultTexture = new Texture("ash_uvgrid01.png", GL_TEXTURE0); 
	protected static Color defaultColor = Color.WHITE;
	
	public Mesh	(float x, float y, float z, float w, float h, float d, Vertex[] vertices, byte[] indices) {
		modelMatrix = new Matrix4f();
		
		pos = new Vector3f(x,y,z);
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*TexturedVertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
		
		
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
}
