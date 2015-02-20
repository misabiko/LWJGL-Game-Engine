package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public class Mesh {
	public Matrix4f modelMatrix;
	
	public FloatBuffer verticesBuffer;
	public IntBuffer indicesBuffer;
	
	public int indicesCount, vboId, vboiId = 0;
	
	public Vertex[] vertices;
	public Vector3f size, center;
	
	public int primitiveType = GL11.GL_TRIANGLES;
	public Texture texture = defaultTexture;
	public boolean isTextured;
	public boolean ignoreLightning = false;
	public boolean isTransparent;
	
	public Material material = new Material("Default");
	
	protected static Vector4f defaultColor = new Vector4f(1f,1f,1f,1f);
	private static Texture defaultTexture = new Texture(System.getProperty("user.dir")+"/src/com/misabiko/LWJGLGameEngine/Resources/Textures/","ash_uvgrid01.png", GL13.GL_TEXTURE0);
	
	protected Mesh (Vertex[] vertices, int primType, Vector3f size, Vector3f center) {
		this.size = size;
		this.center = center;
		this.vertices = vertices;
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
		isTransparent = (vertices[0].dColor[3] != 1f);
	}
	
	protected Mesh (Vertex[] vertices, int[] indices, Vector3f size, Vector3f center) {
		this(vertices, GL11.GL_TRIANGLES, size, center);
		
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
	
	public Mesh	(Vector3f[] vertArray, Vector3f[] normArray, Vector2f[] stArray, int indicesCount, ArrayList<Material> mtls, Vector3f size, Vector3f center) {
		this.indicesCount = indicesCount;
		this.size = size;
		this.center = center;
		this.material = mtls.get(0);
		modelMatrix = new Matrix4f();
		
		vertices = new Vertex[indicesCount];
		for (int i = 0; i < indicesCount; i++) {
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
		primitiveType = GL11.GL_TRIANGLES;
		texture = mtls.get(0).diffuseTextureMap;
		isTextured = true;
		isTransparent = (vertices[0].dColor[3] != 1f);
	}
	
	public Vector3f getPosition() {
		return Matrix3f.transform(Util.mat4ToMat3(modelMatrix), center, new Vector3f());
	}
	
	public void changeDiffuseColor(Vector4f dColor) {
		GL30.glBindVertexArray(OpenGLHandler.VAO);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
				for (Vertex vertex : vertices) {
					dColor.get(vertex.dColor);
					verticesBuffer.put(vertex.getElements());
				}
				verticesBuffer.flip();
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
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
