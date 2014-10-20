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
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

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
	private static final String TITLE = "PROGame";
	private Matrix4f projectionMatrix;
	private int vaoId, vertShaderId, fragShaderId, programId,
		projectionMatrixLocation, viewMatrixLocation, modelMatrixLocation = 0;
	private FloatBuffer matrixBuffer;
	private int[] texIds = new int[2];
	private ArrayList<Cube> cubes = new ArrayList<Cube>();;
	private Camera camera;
	
	public Core() {
		initGL();
		init();
		initShaders();
		initTextures();
		initMatrices();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			input();
			
			for (Cube cube : cubes) {
				update(cube);
				render(cube);
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
	}
	
	private void initShaders() {
		vertShaderId = loadShader("vertex.glsl",GL_VERTEX_SHADER);
		fragShaderId = loadShader("fragment.glsl",GL_FRAGMENT_SHADER);
		
		programId = glCreateProgram();
		glAttachShader(programId, vertShaderId);
		glAttachShader(programId, fragShaderId);
		
		glBindAttribLocation(programId, 0, "in_Position");
		glBindAttribLocation(programId, 1, "in_Color");
		glBindAttribLocation(programId, 2, "in_TextureCoord");
		
		glLinkProgram(programId);
		glValidateProgram(programId);
		
		projectionMatrixLocation = glGetUniformLocation(programId, "projectionMatrix");
		viewMatrixLocation = glGetUniformLocation(programId, "viewMatrix");
		modelMatrixLocation = glGetUniformLocation(programId, "modelMatrix");
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
		cubes.add(new Cube(-0.5f,-0.5f,-1f,1f,1f,1f));
		cubes.add(new Cube(0.5f, 0.5f, -1f, 1f,1f,1f));
		
		camera = new Camera(0f,0f,-1f);
		
//		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			for (Cube cube : cubes) {
				cube.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER,cube.vboId);
				
					glBufferData(GL_ARRAY_BUFFER,cube.verticesBuffer,GL_STATIC_DRAW);
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
				
				cube.vboiId = glGenBuffers();
					
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cube.vboiId);
					glBufferData(GL_ELEMENT_ARRAY_BUFFER, cube.indicesBuffer, GL_STATIC_DRAW);
				
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			cubes.get(1).vel.x = -camera.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_L)){
			cubes.get(1).vel.x = camera.speed;
		}else {
			cubes.get(1).vel.x = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.vel.z = camera.speed/2;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			camera.vel.z = -camera.speed/2;
		}else {
			camera.vel.z = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.vel.x = camera.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			camera.vel.x = -camera.speed;
		}else {
			camera.vel.x = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			camera.vel.y = -camera.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			camera.vel.y = camera.speed;
		}else {
			camera.vel.y = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.angleY = camera.speed*2;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.angleY = -camera.speed*2;
		}else {
			camera.angleY = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.angleX = camera.speed*2;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.angleX = -camera.speed*2;
		}else {
			camera.angleX = 0;
		}
		
	}
	
	private void update(Cube cube) {
		Matrix4f.translate(cube.vel, cube.modelMatrix, cube.modelMatrix);
		
		Matrix4f.translate(camera.vel, camera.viewMatrix, camera.viewMatrix);
		
		Matrix4f.rotate(camera.angleY, new Vector3f(0f,1f,0f), camera.viewMatrix, camera.viewMatrix);
		Matrix4f.rotate(camera.angleX, new Vector3f(1f,0f,0f), camera.viewMatrix, camera.viewMatrix);
		
		glUseProgram(programId);
		
			projectionMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(projectionMatrixLocation, false, matrixBuffer);
			
			camera.viewMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(viewMatrixLocation, false, matrixBuffer);
			
			cube.modelMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);
			
		
		glUseProgram(0);
		
		
	}
	
	private void render(Cube cube) {
		
		glUseProgram(programId);
		
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texIds[cubes.indexOf(cube)]);
			
			glBindVertexArray(vaoId);
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(2);
			
				glBindBuffer(GL_ARRAY_BUFFER,cube.vboId);
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cube.vboiId);
				
					glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
					glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
					glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
					
					glDrawElements(GL_TRIANGLES, cube.indicesCount, GL_UNSIGNED_BYTE, 0);
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
				glBindBuffer(GL_ARRAY_BUFFER,0);
			
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
			glBindVertexArray(0);
		
		glUseProgram(0);
	}
	
	private void cleanUp() {
		glUseProgram(0);
		glDetachShader(programId,vertShaderId);
		glDetachShader(programId,fragShaderId);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		for (Cube cube : cubes) {
			glDeleteBuffers(cube.vboId);
			glDeleteBuffers(cube.vboiId);
		}
		
		glDeleteProgram(programId);
		glDeleteTextures(texIds[0]);
		glDeleteTextures(texIds[1]);
		
		Display.destroy();
	}
}
