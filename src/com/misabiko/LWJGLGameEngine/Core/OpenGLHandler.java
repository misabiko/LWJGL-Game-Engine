package com.misabiko.LWJGLGameEngine.Core;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Meshes.TexturedVertex;
import com.misabiko.LWJGLGameEngine.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Shaders.Program;

public abstract class OpenGLHandler {
	
	private static Program colProgram, texProgram;
	private static Matrix4f projectionMatrix;
	private static FloatBuffer matrixBuffer;
	private static int vaoId = 0;
	
	public static void init(String title, int width, int height) {
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title);
			Display.create(pixelFormat,contextAttribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		glClearColor(0.0f,0f,0f,1f);
		
		glViewport(0, 0, width, height);
		
		Mouse.setGrabbed(true);
		
		glEnable(GL_DEPTH_TEST);
		
		initShaders();
		initMatrices(width,height);
	}
	
	private static void initShaders() {
		int vertShaderId = loadShader("vertex.glsl", GL_VERTEX_SHADER);
		int fragShaderId = loadShader("fragment.glsl", GL_FRAGMENT_SHADER);
		int texFragShaderId = loadShader("texfragment.glsl", GL_FRAGMENT_SHADER);
		
		colProgram = new Program(new int[] {vertShaderId, fragShaderId});
		texProgram = new Program(new int[] {vertShaderId, texFragShaderId});
	}
	
	private static int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
//		TODO Look further into BufferedReader and such
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/com/misabiko/LWJGLGameEngine/Shaders/"+filename));
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
		
		shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		return shaderID;
	}
	
	private static void initMatrices(int width, int height) {
		projectionMatrix = new Matrix4f();
		float fov = 150f;
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
		
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	public static void initVBOs(ArrayList<Mesh> Meshes) {
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			for (Mesh mesh : Meshes) {
				mesh.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER,mesh.vboId);
				
					glBufferData(GL_ARRAY_BUFFER,mesh.verticesBuffer,GL_STATIC_DRAW);
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
				
				mesh.vboiId = glGenBuffers();
					
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vboiId);
					glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.indicesBuffer, GL_STATIC_DRAW);
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			}
			
		glBindVertexArray(0);
		
	}
	
	public static void render(Mesh mesh, Camera camera) {
		
		if (mesh.isTextured) {
			glUseProgram(texProgram.id);
			
				projectionMatrix.store(matrixBuffer);
				matrixBuffer.flip();
				glUniformMatrix4(texProgram.projectionMatrixLocation, false, matrixBuffer);
				
				camera.viewMatrix.store(matrixBuffer);
				matrixBuffer.flip();
				glUniformMatrix4(texProgram.viewMatrixLocation, false, matrixBuffer);
				
				mesh.modelMatrix.store(matrixBuffer);
				matrixBuffer.flip();
				glUniformMatrix4(texProgram.modelMatrixLocation, false, matrixBuffer);
				
			
			glUseProgram(0);
		} else {
			glUseProgram(colProgram.id);
			
			projectionMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(colProgram.projectionMatrixLocation, false, matrixBuffer);
			
			camera.viewMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(colProgram.viewMatrixLocation, false, matrixBuffer);
			
			mesh.modelMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(colProgram.modelMatrixLocation, false, matrixBuffer);
			
		
		glUseProgram(0);
		}
		
		if (mesh.isTextured) {
			glUseProgram(texProgram.id);
			
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, mesh.texture.texId);
				
				glBindVertexArray(vaoId);
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				glEnableVertexAttribArray(2);
				
					glBindBuffer(GL_ARRAY_BUFFER,mesh.vboId);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vboiId);
					
						glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
						glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
						glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
						
						glDrawElements(GL_TRIANGLES, mesh.indicesCount, GL_UNSIGNED_BYTE, 0);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
					glBindBuffer(GL_ARRAY_BUFFER,0);
				
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
				glDisableVertexAttribArray(2);
				glBindVertexArray(0);
			
			glUseProgram(0);
		}else {
			glUseProgram(colProgram.id);
			
				glBindVertexArray(vaoId);
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				glEnableVertexAttribArray(2);
				
					glBindBuffer(GL_ARRAY_BUFFER,mesh.vboId);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vboiId);
					
						glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
						glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
						glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
						
						glDrawElements(GL_TRIANGLES, mesh.indicesCount, GL_UNSIGNED_BYTE, 0);
						
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
					glBindBuffer(GL_ARRAY_BUFFER,0);
				
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
				glDisableVertexAttribArray(2);
				glBindVertexArray(0);
		
			glUseProgram(0);
		}
	}
	
	public static void cleanUp(ArrayList<Mesh> Meshes) {
		glUseProgram(0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		for (Mesh mesh : Meshes) {
			glDeleteBuffers(mesh.vboId);
			glDeleteBuffers(mesh.vboiId);
			
			if (mesh.texture.texId != Mesh.defaultTexture.texId)
				glDeleteTextures(mesh.texture.texId);
		}
		
		glDeleteTextures(Mesh.defaultTexture.texId);
		
		colProgram.cleanUp();
		texProgram.cleanUp();
		
		Display.destroy();
	}
}
