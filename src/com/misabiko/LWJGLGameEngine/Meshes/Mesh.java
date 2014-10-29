package com.misabiko.LWJGLGameEngine.Meshes;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public abstract class Mesh {
	public Matrix4f modelMatrix;
	
	public Vector3f pos;
	public Vector3f Yvel = new Vector3f(0,0,0);
	
	public float angleX, angleY, angleZ, xRotVel, yVel = 0;
	
	public boolean isTextured = true;
	public boolean isOnGround = false;
	
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public Texture texture = defaultTexture;

	public static Texture defaultTexture = new Texture("ash_uvgrid01.png", GL_TEXTURE0); 
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
	
	public void update() {
		Vector3f.sub(pos, Xvel, pos);
		Vector3f.sub(pos, Yvel, pos);
		
		Matrix4f.setIdentity(modelMatrix);
		
		Matrix4f.translate(pos, modelMatrix, modelMatrix);
		
		Matrix4f.rotate(angleY, new Vector3f(0,1f,0), modelMatrix, modelMatrix);
		Matrix4f.rotate(angleX, new Vector3f(1f,0,0), modelMatrix, modelMatrix);
	}
}
