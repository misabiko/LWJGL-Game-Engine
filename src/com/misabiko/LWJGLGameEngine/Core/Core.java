package com.misabiko.LWJGLGameEngine.Core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Utils.Util;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Core {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "LWJGL Game Engine";
	private Matrix4f projectionMatrix;
	private int vaoId = 0;
	private Program colProgram, texProgram;
	private FloatBuffer matrixBuffer;
	private int[] texIds = new int[2];
	private Box cuby;
	private ArrayList<Mesh> Meshes = new ArrayList<Mesh>();;
	private Camera camera;
	
	private boolean F5isHeld = false;
	
//	Short term todos
//	TODO what is the x angle for the vector (1,0,0)
//	TODO make a texture class, to easily manage textures (duh)
//	TODO put textures into the mesh objects
//	TODO make a line class
//	TODO Maybe move the stuff messing with opengl into another class (crowded core class is crowded)
	
//	Long term todos
//	TODO make a light shader/engine ( or at least something to see the meshes' borders )
//	TODO learn to manage the projection matrix because that shizza is but' ugly
//	TODO Physic Engine (Collision, gravity, etc)
//	TODO Custom (Blender) models?
//	TODO sound manager
//	TODO well, you know, game stuff
	
	public Core() {
		initGL();
		init();
		initShaders();
		initTextures();
		initMatrices();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			input();
			
			for (Mesh mesh : Meshes) {
				update(mesh);
				
				render(mesh);
				
			}
			
			
			Display.sync(60);
			Display.update();
		}
		
		cleanUp();
	}
	
	private void initGL() {
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle(TITLE);
			Display.create(pixelFormat,contextAttribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		glClearColor(0.0f,0.5f,1f,1f);
		
		glViewport(0, 0, WIDTH, HEIGHT);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	private void initShaders() {
		int vertShaderId = loadShader("vertex.glsl", GL_VERTEX_SHADER);
		int fragShaderId = loadShader("fragment.glsl", GL_FRAGMENT_SHADER);
		int texFragShaderId = loadShader("texfragment.glsl", GL_FRAGMENT_SHADER);
		
		colProgram = new Program(new int[] {vertShaderId, fragShaderId});
		texProgram = new Program(new int[] {vertShaderId, texFragShaderId});
	}
	
	private void initTextures() {
		texIds[0] = loadTexture("ash_uvgrid01.png", GL_TEXTURE0);
		texIds[1] = loadTexture("ash_uvgrid08.png", GL_TEXTURE0);
	}
	
	private void initMatrices() {
		projectionMatrix = new Matrix4f();
		float fov = 150f;
		float aspectRatio = (float) WIDTH / (float) HEIGHT;
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
	
	private void init() {
		cuby = new Box(0, 0, 0, 0.5f,0.5f,0.5f, 0, 1f, 1f, 0.5f);
		Meshes.add(cuby);
		
//		Meshes.add(new Box(0, 0, 0,1f,1f,1f));
		
		Meshes.add(new Box(-3f, -2f, -2f, 8f,0.5f,4f));
		
		camera = new Camera(-1f, -1.5f, -1f);
		
		
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
	
	private int loadShader(String filename, int type) {
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
	
	private int loadTexture(String filename, int textureUnit) {
		int texWidth = 0;
		int texHeight = 0;
		ByteBuffer buffer = null;
		
		try {
			InputStream input = new FileInputStream("src/com/misabiko/LWJGLGameEngine/Resources/Textures/"+filename);
			
			PNGDecoder decoder = new PNGDecoder(input);
			texWidth = decoder.getWidth();
			texHeight = decoder.getHeight();
			
			buffer = ByteBuffer.allocateDirect(4*texWidth*texHeight);
			decoder.decode(buffer, texWidth*4, Format.RGBA);
			
			buffer.flip();
			
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int texId = glGenTextures();
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, texId);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, texWidth, texHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
		
		return texId;
	}
	
	private void input() {
		
//		Dat clean input method :O
		
		if (Mouse.isButtonDown(0)) {
			camera.angleX -= ((float) Mouse.getDY()/100);
			camera.angleY += ((float) Mouse.getDX()/100);
			
			if (camera.angleX > Math.PI*2) {
				camera.angleX = camera.angleX - (float) (Math.PI*2);
			}else if (camera.angleX < -(Math.PI*2)) {
				camera.angleX = camera.angleX + (float) (Math.PI*2);
			}
			if (camera.angleY > Math.PI*2) {
				camera.angleY = camera.angleY - (float) (Math.PI*2);
			}else if (camera.angleY < -(Math.PI*2)) {
				camera.angleY = camera.angleY + (float) (Math.PI*2);
			}
			
			if (!camera.freeMovement) {
				cuby.angleY = camera.angleY;
			}
		}else {
			Mouse.getDX();
			Mouse.getDY();
		}
		
		camera.zoom -= ((float) Mouse.getDWheel()/1000);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			if (!F5isHeld) {
				if (camera.freeMovement) {
					camera.angleX = cuby.angleX;
					camera.angleY = cuby.angleY;
					camera.angleZ = cuby.angleZ;
				}
				camera.freeMovement = !camera.freeMovement;
			}
			F5isHeld = true;
		}else {
			F5isHeld = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Vector3f vel = Util.angleToVector3f(cuby.angleX, cuby.angleY+(float)(Math.PI/2));
			System.out.println(vel.toString());
			vel.scale(camera.speed);
			
			Vector3f.add(cuby.pos, vel, cuby.pos);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			Vector3f vel = Util.angleToVector3f(cuby.angleX, cuby.angleY-(float)(Math.PI/2));
			System.out.println(vel.toString());
			vel.scale(camera.speed);
			
			Vector3f.add(cuby.pos, vel, cuby.pos);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Vector3f vel = Util.angleToVector3f(cuby.angleX-(float)(Math.PI/2), cuby.angleY);
			System.out.println(vel.toString());
			vel.scale(camera.speed);
			
			Vector3f.add(cuby.pos, vel, cuby.pos);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Vector3f vel = Util.angleToVector3f(cuby.angleX+(float)(Math.PI/2), cuby.angleY);
			System.out.println(vel.toString());
			vel.scale(camera.speed);
			
			Vector3f.add(cuby.pos, vel, cuby.pos);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Vector3f vel = Util.angleToVector3f(cuby.angleX, cuby.angleY);
			vel.scale(camera.speed);
			
			Vector3f.sub(cuby.pos, vel, cuby.pos);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			Vector3f vel = Util.angleToVector3f(cuby.angleX, cuby.angleY);
			vel.scale(camera.speed);
			
			Vector3f.add(cuby.pos, vel, cuby.pos);
		}
		
	}
	
	private void update(Mesh mesh) {
		Matrix4f.setIdentity(camera.viewMatrix);
		Matrix4f.setIdentity(cuby.modelMatrix);
		
		Matrix4f.translate(cuby.pos, cuby.modelMatrix, cuby.modelMatrix);
		
		Matrix4f.rotate(cuby.angleY, new Vector3f(0,1f,0), cuby.modelMatrix, cuby.modelMatrix);
		Matrix4f.rotate(cuby.angleX, new Vector3f(1f,0,0), cuby.modelMatrix, cuby.modelMatrix);
		
		Matrix4f.translate(new Vector3f(0,0,-camera.zoom), camera.viewMatrix, camera.viewMatrix);
		
		Matrix4f.rotate(-camera.angleX, new Vector3f(1f,0,0), camera.viewMatrix, camera.viewMatrix);
		Matrix4f.rotate(-camera.angleY, new Vector3f(0,1f,0), camera.viewMatrix, camera.viewMatrix);
		
		Matrix4f.translate(cuby.pos.negate(new Vector3f()), camera.viewMatrix, camera.viewMatrix);
		
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
		
	}
	
	private void render(Mesh mesh) {
		if (mesh.isTextured) {
			glUseProgram(texProgram.id);
			
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texIds[0]);
				
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
	
	private void cleanUp() {
		glUseProgram(0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		for (Mesh mesh : Meshes) {
			glDeleteBuffers(mesh.vboId);
			glDeleteBuffers(mesh.vboiId);
		}
		
		glDeleteTextures(texIds[0]);
		glDeleteTextures(texIds[1]);
		
		colProgram.cleanUp();
		texProgram.cleanUp();
		
		Display.destroy();
	}
}
