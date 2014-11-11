package com.misabiko.LWJGLGameEngine.Meshes;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Physic.Hitbox;
import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public abstract class Mesh {
	public Matrix4f modelMatrix;
	
	public Vector3f pos;
	public Vector2f xzVel = new Vector2f(0,0);
	public float angleX, angleY, angleZ, yVel = 0;
	
	public Hitbox hitbox;
	
	public boolean isTextured;
	public boolean isOnGround = false;
	
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public int primitiveType = GL_TRIANGLES;
	
	public Texture texture = defaultTexture;

	public static Texture defaultTexture = new Texture("ash_uvgrid01.png", GL_TEXTURE0); 
	protected static Color defaultColor = Color.WHITE;
	
	public Mesh	(float x, float y, float z, Vertex[] vertices, int primType, Hitbox hb) {
		modelMatrix = new Matrix4f();
		
		pos = new Vector3f(x,y,z);
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*TexturedVertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
		indicesCount = vertices.length;
		primitiveType = primType;
		isTextured = false;
		
		hitbox = hb;
	}
	
	public Mesh	(float x, float y, float z, Vertex[] vertices, byte[] indices, Hitbox hb) {
		this(x,y,z,vertices, GL_TRIANGLES, hb);
		
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		isTextured = true;
	}
	
	public void update() {
		Vector3f vel = new Vector3f(xzVel.x, yVel, -xzVel.y);
		
		Matrix4f rot = new Matrix4f();
		Matrix4f.rotate(angleX, new Vector3f(1,0,0), rot, rot);
		Matrix4f.rotate(angleY, new Vector3f(0,1,0), rot, rot);
		Matrix4f.rotate(angleZ, new Vector3f(0,0,1), rot, rot);
		vel = Util.mulMatrix4fVector3f(rot, vel);
		
		Vector3f.add(pos, vel, pos);
		
		Matrix4f.setIdentity(modelMatrix);
		
		Matrix4f.translate(pos, modelMatrix, modelMatrix);
		
		Matrix4f.rotate(angleY, new Vector3f(0,1f,0), modelMatrix, modelMatrix);
		Matrix4f.rotate(angleX, new Vector3f(1f,0,0), modelMatrix, modelMatrix);
	}
}
