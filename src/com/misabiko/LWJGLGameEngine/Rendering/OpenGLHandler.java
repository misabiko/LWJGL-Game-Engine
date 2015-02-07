package com.misabiko.LWJGLGameEngine.Rendering;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Core.Core;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Light;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Rendering.Shaders.Program;
import com.misabiko.LWJGLGameEngine.Utilities.Util;
import com.misabiko.LWJGLGameEngine.World.Chunk;

public abstract class OpenGLHandler {
	
	public static int VAO = 0;
	public static Program program;
	public static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	public static FloatBuffer matrix3fBuffer = BufferUtils.createFloatBuffer(9);
	public static Matrix4f projectionMatrix;
	
	public static void init(String title, int width, int height) {
//		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(3,3).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title+" - FPS:");
			Display.create(pixelFormat,contextAttribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		glClearColor(0f,0f,0f,1f);
		
		glViewport(0, 0, width, height);
		
		Mouse.setGrabbed(true);
		
		glEnable(GL_DEPTH_TEST);
//		glEnable(GL_CULL_FACE);
//		 GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		initProgram();
		initMatrices(width,height);
	}
	private static void initProgram() {
		int vertShaderId = loadShader("vertex.glsl", GL_VERTEX_SHADER);
		int fragShaderId = loadShader("fragment.glsl", GL_FRAGMENT_SHADER);
		
		program = new Program(new int[] {vertShaderId, fragShaderId});
		
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
		
		glUseProgram(program.id);
			
			projectionMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(program.id, "projectionMatrix"), false, matrix4fBuffer);
			
		glUseProgram(0);
	}
	
	public static void initVAOs() {
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
			
			for (GameObject obj : GameObject.objs) {
				obj.mesh.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER,obj.mesh.vboId);
				
					glBufferData(GL_ARRAY_BUFFER,obj.mesh.verticesBuffer,GL_STATIC_DRAW);
				
				if (obj.mesh.indicesBuffer != null) {
					obj.mesh.vboiId = glGenBuffers();
						
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, obj.mesh.vboiId);
						glBufferData(GL_ELEMENT_ARRAY_BUFFER, obj.mesh.indicesBuffer, GL_STATIC_DRAW);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
				}
				
				glEnableVertexAttribArray(0);	//position
				glEnableVertexAttribArray(1);	//normal
				glEnableVertexAttribArray(2);	//aColor
				glEnableVertexAttribArray(3);	//dColor
				glEnableVertexAttribArray(4);	//sColor
				glEnableVertexAttribArray(5);	//texCoords
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
			}
			
			for (Mesh mesh : Core.world.getMeshes(false)) {
				mesh.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER, mesh.vboId);
				
					glBufferData(GL_ARRAY_BUFFER, mesh.verticesBuffer, GL15.GL_DYNAMIC_DRAW);
				
				if ( mesh.indicesBuffer != null) {
					 mesh.vboiId = glGenBuffers();
						
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,  mesh.vboiId);
						glBufferData(GL_ELEMENT_ARRAY_BUFFER,  mesh.indicesBuffer, GL15.GL_DYNAMIC_DRAW);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
				}
				
				glEnableVertexAttribArray(0);	//position
				glEnableVertexAttribArray(1);	//normal
				glEnableVertexAttribArray(2);	//aColor
				glEnableVertexAttribArray(3);	//dColor
				glEnableVertexAttribArray(4);	//sColor
				glEnableVertexAttribArray(5);	//texCoords
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
			}
				

//		glBindVertexArray(worldVAO);
//		
//			
//		
		glBindVertexArray(0);
		
	}
	
	public static void render(Mesh mesh) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, mesh.texture.texId);
		
		glUseProgram(program.id);
			
			mesh.modelMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(program.id, "modelMatrix"), false, matrix4fBuffer);
			
			Util.mat4ToMat3(mesh.modelMatrix).invert().transpose().store(matrix3fBuffer);
			matrix3fBuffer.flip();
			glUniformMatrix3(glGetUniformLocation(program.id, "normalMatrix"), false, matrix3fBuffer);
			
			Vector3f camPos = Camera.getPosition();
			glUniform3f(glGetUniformLocation(program.id, "cameraPosition"), camPos.x, camPos.y, camPos.z);
			
			glUniform1i(glGetUniformLocation(program.id, "materialTex"), GL_TEXTURE_2D);
			glUniform1f(glGetUniformLocation(program.id, "materialShininess"), mesh.material.specularExponent);
			
			glUniform1f(glGetUniformLocation(program.id, "isTextured"), mesh.isTextured ? 1f : 0f);
			
			glUniform1f(glGetUniformLocation(program.id, "ignoreLightning"), mesh.ignoreLightning ? 1f : 0f);
			
			glBindVertexArray(VAO);
				
					glBindBuffer(GL_ARRAY_BUFFER, mesh.vboId);
					
						glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, 0);
						glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.normOffset);
						glVertexAttribPointer(2, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.aColorOffset);
						glVertexAttribPointer(3, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.dColorOffset);
						glVertexAttribPointer(4, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.sColorOffset);
						glVertexAttribPointer(5, 2, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.texCoordsOffset);
						
						if (mesh.indicesBuffer != null)
							glDrawElements(GL_TRIANGLES, mesh.indicesBuffer);
						else
							glDrawArrays(GL_TRIANGLES, 0, mesh.indicesCount);
						
					glBindBuffer(GL_ARRAY_BUFFER,0);
				
			glBindVertexArray(0);
	
		glUseProgram(0);
	}
	public static void cleanUp() {
		glUseProgram(0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		for (GameObject obj : GameObject.objs) {
			glDeleteBuffers(obj.mesh.vboId);
			glDeleteBuffers(obj.mesh.vboiId);
			glDeleteTextures(obj.mesh.texture.texId);
		}
		
		program.cleanUp();
		
		Display.destroy();
	}
}
