package com.misabiko.LWJGLGameEngine.Rendering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Rendering.Shaders.Program;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public abstract class OpenGLHandler {
	
	public static int VAO = 0;
	public static Program program, program2D;
	public static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	public static FloatBuffer matrix3fBuffer = BufferUtils.createFloatBuffer(9);
	public static Matrix4f projectionMatrix;
	
	public static void init(String title, int width, int height) {
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(3,3).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title);
			Display.create(pixelFormat,contextAttribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		GL11.glClearColor(0f,0f,0f,1f);
		
		GL11.glViewport(0, 0, width, height);
		
		Mouse.setGrabbed(true);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		initProgram();
		initMatrices(width,height);
	}
	
	private static void initProgram() {
		int vertShaderId = loadShader("vertex.glsl", GL20.GL_VERTEX_SHADER);
		int fragShaderId = loadShader("fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		
		program = new Program(new int[] {vertShaderId, fragShaderId});

		int vert2dShaderId = loadShader("2dVertex.glsl", GL20.GL_VERTEX_SHADER);
		int frag2dShaderId = loadShader("2dFragment.glsl", GL20.GL_FRAGMENT_SHADER);
		
		program2D = new Program(new int[] {vert2dShaderId, frag2dShaderId});
		
		GL20.glUseProgram(program2D.id);
			GL20.glUniform1i(GL20.glGetUniformLocation(program2D.id, "textureUnit"), GL11.GL_TEXTURE_2D);
		GL20.glUseProgram(0);
	}
	
	private static int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
//		TODO Look further into BufferedReader and such
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/com/misabiko/LWJGLGameEngine/Rendering/Shaders/"+filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Couldn't read the file");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		return shaderID;
	}
	
	private static void initMatrices(int width, int height) {
		projectionMatrix = new Matrix4f();
		float fov = 360f;
		float aspectRatio = (float) width / (float) height;
		float nearPlane = 0.1f;
		float farPlane = 100f;
		
		float yScale = (float) Math.atan(Math.toRadians(fov/2f));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;
		
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		projectionMatrix.m33 = 0;
		
			
			projectionMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
		GL20.glUseProgram(program.id);
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(program.id, "projectionMatrix"), false, matrix4fBuffer);
		GL20.glUseProgram(program2D.id);
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(program2D.id, "projectionMatrix"), false, matrix4fBuffer);
		GL20.glUseProgram(0);
	}
	
	public static void initVAOs() {
		VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
			
			for (GameObject obj : GameObject.objs) {
				obj.mesh.vboId = GL15.glGenBuffers();
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,obj.mesh.vboId);
				
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER,obj.mesh.verticesBuffer,GL15.GL_STATIC_DRAW);
				
					if (obj.mesh.indicesBuffer != null) {
						obj.mesh.vboiId = GL15.glGenBuffers();
						
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, obj.mesh.vboiId);
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, obj.mesh.indicesBuffer, GL15.GL_STATIC_DRAW);
					
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0);
				}
				
				GL20.glEnableVertexAttribArray(0);	//position
				GL20.glEnableVertexAttribArray(1);	//normal
				GL20.glEnableVertexAttribArray(2);	//aColor
				GL20.glEnableVertexAttribArray(3);	//dColor
				GL20.glEnableVertexAttribArray(4);	//sColor
				GL20.glEnableVertexAttribArray(5);	//texCoords
					
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
			}
			
		GL30.glBindVertexArray(0);
		
	}
	
	public static void render(Mesh mesh) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.texture.texId);
		
		GL20.glUseProgram(program.id);
			
			mesh.modelMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(program.id, "modelMatrix"), false, matrix4fBuffer);
			
			Util.mat4ToMat3(mesh.modelMatrix).invert().transpose().store(matrix3fBuffer);
			matrix3fBuffer.flip();
			GL20.glUniformMatrix3(GL20.glGetUniformLocation(program.id, "normalMatrix"), false, matrix3fBuffer);
			
			Vector3f camPos = Camera.getPosition();
			GL20.glUniform3f(GL20.glGetUniformLocation(program.id, "cameraPosition"), camPos.x, camPos.y, camPos.z);
			
			GL20.glUniform1i(GL20.glGetUniformLocation(program.id, "materialTex"), GL11.GL_TEXTURE_2D);
			GL20.glUniform1f(GL20.glGetUniformLocation(program.id, "materialShininess"), mesh.material.specularExponent);
			
			GL20.glUniform1f(GL20.glGetUniformLocation(program.id, "isTextured"), mesh.isTextured ? 1f : 0f);
			
			GL20.glUniform1f(GL20.glGetUniformLocation(program.id, "ignoreLightning"), mesh.ignoreLightning ? 1f : 0f);
			
			GL30.glBindVertexArray(VAO);
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.vboId);
					
					GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, 0);
					GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.normOffset);
					GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.aColorOffset);
					GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.dColorOffset);
					GL20.glVertexAttribPointer(4, 4, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.sColorOffset);
					GL20.glVertexAttribPointer(5, 2, GL11.GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.texCoordsOffset);
						
					if (mesh.indicesBuffer != null)
						GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.indicesBuffer);
					else
						GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.indicesCount);
						
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
				
			GL30.glBindVertexArray(0);
	
		GL20.glUseProgram(0);
	}
	
	public static void cleanUp() {
		GL20.glUseProgram(0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		for (GameObject obj : GameObject.objs) {
			GL15.glDeleteBuffers(obj.mesh.vboId);
			GL15.glDeleteBuffers(obj.mesh.vboiId);
			GL11.glDeleteTextures(obj.mesh.texture.texId);
		}
		
		program.cleanUp();
		
		Display.destroy();
	}
}
