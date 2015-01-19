package com.misabiko.LWJGLGameEngine.Rendering;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
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

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Rendering.Lights.Light;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Rendering.Shaders.Program;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public abstract class OpenGLHandler {
	
	private static Program program;
	private static Matrix4f projectionMatrix;
	private static FloatBuffer matrix4fBuffer, matrix3fBuffer;
	private static int vaoId = 0;
	
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
		
		glClearColor(0f,0f,0f,1f);
		
		glViewport(0, 0, width, height);
		
		Mouse.setGrabbed(true);
		
		glEnable(GL_DEPTH_TEST);
		
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
		
		matrix4fBuffer = BufferUtils.createFloatBuffer(16);
		matrix3fBuffer = BufferUtils.createFloatBuffer(9);
	}
	
	public static void initBuffers() {
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			for (GameObject obj : GameObject.objs) {
				obj.mesh.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER,obj.mesh.vboId);
				
					glBufferData(GL_ARRAY_BUFFER,obj.mesh.verticesBuffer,GL_STATIC_DRAW);
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
				
				if (obj.mesh.indicesBuffer != null) {
					obj.mesh.vboiId = glGenBuffers();
						
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, obj.mesh.vboiId);
						glBufferData(GL_ELEMENT_ARRAY_BUFFER, obj.mesh.indicesBuffer, GL_STATIC_DRAW);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
				}
			}
			
		glBindVertexArray(0);
		
	}
	
	public static void render(GameObject obj) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, obj.mesh.texture.texId);
		
		glUseProgram(program.id);
			
			projectionMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(program.id, "projectionMatrix"), false, matrix4fBuffer);
			
			Camera.viewMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(program.id, "viewMatrix"), false, matrix4fBuffer);
			
			obj.mesh.modelMatrix.store(matrix4fBuffer);
			matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(program.id, "modelMatrix"), false, matrix4fBuffer);
			
			Util.mat4ToMat3(obj.mesh.modelMatrix).invert().transpose().store(matrix3fBuffer);
			matrix3fBuffer.flip();
			glUniformMatrix3(glGetUniformLocation(program.id, "normalMatrix"), false, matrix3fBuffer);
			
			glUniform1f(glGetUniformLocation(program.id, "numLights"),	Light.lights.size());
			for (Light light : Light.lights) {
				int i = Light.lights.indexOf(light);
				glUniform4f(glGetUniformLocation(program.id, "lights["+i+"].position"),				light.position.x,			light.position.y,		light.position.z,		light.position.w);
				glUniform3f(glGetUniformLocation(program.id, "lights["+i+"].intensities"),			light.intensities.x,		light.intensities.y,	light.intensities.z);
				glUniform3f(glGetUniformLocation(program.id, "lights["+i+"].coneDirection"),		light.coneDirection.x,		light.coneDirection.y,	light.coneDirection.z);
				glUniform1f(glGetUniformLocation(program.id, "lights["+i+"].ambientCoefficient"),	light.ambientCoefficient);
				glUniform1f(glGetUniformLocation(program.id, "lights["+i+"].attenuation"),			light.attenuation);
				glUniform1f(glGetUniformLocation(program.id, "lights["+i+"].coneAngle"),			light.coneAngle);
			}

			glUniform3f(glGetUniformLocation(program.id, "cameraPosition"), Camera.viewMatrix.m03, Camera.viewMatrix.m13, Camera.viewMatrix.m23);
			
			glUniform1i(glGetUniformLocation(program.id, "materialTex"), GL_TEXTURE_2D);
			glUniform1f(glGetUniformLocation(program.id, "materialShininess"), obj.mesh.material.specularExponent);
			
			if (obj.mesh.isTextured)
				glUniform1f(glGetUniformLocation(program.id, "isTextured"), 1f);
			else
				glUniform1f(glGetUniformLocation(program.id, "isTextured"), 0f);
			
			glBindVertexArray(vaoId);
				glEnableVertexAttribArray(0);	//position
				glEnableVertexAttribArray(1);	//normal
				glEnableVertexAttribArray(2);	//aColor
				glEnableVertexAttribArray(3);	//dColor
				glEnableVertexAttribArray(4);	//sColor
				glEnableVertexAttribArray(5);	//texCoords
				
					glBindBuffer(GL_ARRAY_BUFFER,obj.mesh.vboId);
					
						glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, 0);
						glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.normOffset);
						glVertexAttribPointer(2, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.aColorOffset);
						glVertexAttribPointer(3, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.dColorOffset);
						glVertexAttribPointer(4, 4, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.sColorOffset);
						glVertexAttribPointer(5, 2, GL_FLOAT, false, Vertex.bytesPerFloat*Vertex.elementCount, Vertex.texCoordsOffset);
						
						if (obj.mesh.indicesBuffer != null)
							glDrawElements(GL_TRIANGLES, obj.mesh.indicesBuffer);
						else
							glDrawArrays(GL_TRIANGLES, 0, obj.mesh.indicesCount);
						
					glBindBuffer(GL_ARRAY_BUFFER,0);
				
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
				glDisableVertexAttribArray(2);
				glDisableVertexAttribArray(3);
				glDisableVertexAttribArray(4);
				glDisableVertexAttribArray(5);
			glBindVertexArray(0);
	
		glUseProgram(0);
//		}
	}
	
	public static void cleanUp() {
		glUseProgram(0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(3);
		
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
