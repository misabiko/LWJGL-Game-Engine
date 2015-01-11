package com.misabiko.LWJGLGameEngine.Meshes;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public abstract class Mesh {
	public Matrix4f modelMatrix;
	
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public int primitiveType = GL_TRIANGLES;
	
	public Texture texture = defaultTexture;

	public static Texture defaultTexture = new Texture("ash_uvgrid01.png", GL_TEXTURE0); 
	protected static Color defaultColor = Color.ORANGE;
	
	public Mesh	(Vertex[] vertices, int primType) {
		modelMatrix = new Matrix4f();
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*Vertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
		indicesCount = vertices.length;
		primitiveType = primType;
	}
	
	public Mesh	(Vertex[] vertices, byte[] indices) {
		this(vertices, GL_TRIANGLES);
		
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
	
	public void update(Vector3f pos, float angleX, float angleY) {
		Matrix4f.setIdentity(modelMatrix);
		
		Matrix4f.translate(pos, modelMatrix, modelMatrix);
		
		Matrix4f.rotate(angleY, new Vector3f(0,1f,0), modelMatrix, modelMatrix);
		Matrix4f.rotate(angleX, new Vector3f(1f,0,0), modelMatrix, modelMatrix);
	}
	
	public void update(Matrix4f mat) {
		modelMatrix = mat;
	}
}
