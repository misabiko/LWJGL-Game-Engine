package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public class Mesh {
	public Matrix4f modelMatrix;
	
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public int primitiveType = GL_TRIANGLES;
	public Texture texture;
	public boolean isTextured;
	
	protected static Color defaultColor = Color.RED;
	private static Texture defaultTexture = new Texture(System.getProperty("user.dir")+"/src/com/misabiko/LWJGLGameEngine/Resources/Textures/","ash_uvgrid01.png", GL13.GL_TEXTURE0);
	
	protected Mesh	(Vertex[] vertices, int primType) {
		modelMatrix = new Matrix4f();
		
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*Vertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
		indicesCount = vertices.length;
		primitiveType = primType;
		texture = defaultTexture;
		isTextured = false;
	}
	
	protected Mesh	(Vertex[] vertices, byte[] indices) {
		this(vertices, GL_TRIANGLES);
		
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
	
	public Mesh	(Vector3f[] vertArray, Vector3f[] normArray, Vector2f[] stArray, int indiceCount, ArrayList<Material> mtls) {
		indicesCount = indiceCount;
		modelMatrix = new Matrix4f();
		
		Vertex[] vertices = new Vertex[indiceCount];
		for (int i = 0; i < indiceCount; i++) {
			vertices[i] = new Vertex(
					vertArray[i].x,
					vertArray[i].y,
					vertArray[i].z,

					normArray[i].x,
					normArray[i].y,
					normArray[i].z,

					mtls.get(0).ambientColor.x,
					mtls.get(0).ambientColor.y,
					mtls.get(0).ambientColor.z,
					1f,

					mtls.get(0).diffuseColor.x,
					mtls.get(0).diffuseColor.y,
					mtls.get(0).diffuseColor.z,
					1f,

					mtls.get(0).specularColor.x,
					mtls.get(0).specularColor.y,
					mtls.get(0).specularColor.z,
					1f,

					stArray[i].x,
					stArray[i].y
					);
		}
		
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*Vertex.elementCount);
		
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		
		verticesBuffer.flip();
		primitiveType = GL_TRIANGLES;
		texture = mtls.get(0).diffuseTextureMap;
		isTextured = true;
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
