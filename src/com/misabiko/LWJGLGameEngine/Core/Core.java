package com.misabiko.LWJGLGameEngine.Core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
	private int vaoId, vboId, vboiId, vertShaderId, fragShaderId, programId, textureSelector,
		projectionMatrixLocation, viewMatrixLocation, modelMatrixLocation = 0;
	private FloatBuffer matrixBuffer;
	private int[] texIds = new int[2];
	private Cube cube;
	private Camera camera;
	
	public Core() {
		initGL();
		init();
		initShaders();
		initTextures();
		initMatrices();
		
		while (!Display.isCloseRequested()) {
			input();
			update();
			render();
			
			Display.sync(60);
			Display.update();
		}
		cleanUp(cube);
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
		
		glClearColor(0.0f,0.0f,0.0f,0.0f);
		
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
		float fov = 60f;
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
		
		Matrix4f.translate(camera.pos, camera.viewMatrix, camera.viewMatrix);
	}
	
	private void init() {
		cube = new Cube(-0.5f,-0.5f,-1f,1f,1f,1f);
		camera = new Camera();
		
		glEnable(GL_CULL_FACE);
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			vboId = glGenBuffers();
			
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
				glBufferData(GL_ARRAY_BUFFER,cube.verticesBuffer,GL_STREAM_DRAW);
				
				glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
				glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
				glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
			glBindBuffer(GL_ARRAY_BUFFER,0);
			
			vboiId = glGenBuffers();
				
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, cube.indicesBuffer, GL_STREAM_DRAW);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			
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
	
	private void update() {
		
		Matrix4f.scale(cube.scale, cube.modelMatrix, cube.modelMatrix);
		Matrix4f.translate(cube.pos, cube.modelMatrix, cube.modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(cube.angle.z), new Vector3f(0, 0, 1), cube.modelMatrix, cube.modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(cube.angle.y), new Vector3f(0, 1, 0), cube.modelMatrix, cube.modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(cube.angle.x), new Vector3f(1, 0, 0), cube.modelMatrix, cube.modelMatrix);
		
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
	
	private void input() {
		
		while(Keyboard.next()) {
			if(!Keyboard.getEventKeyState())
				continue;
			
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_1:
					textureSelector = 0;
					break;
				case Keyboard.KEY_2:
					textureSelector = 1;
					break;
			}
		}
			
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Matrix4f.translate(new Vector3f(0,0,0.005f), camera.viewMatrix, camera.viewMatrix);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			Matrix4f.translate(new Vector3f(0,0,-0.005f), camera.viewMatrix, camera.viewMatrix);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Matrix4f.translate(new Vector3f(0.01f,0,0), camera.viewMatrix, camera.viewMatrix);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			Matrix4f.translate(new Vector3f(-0.01f,0,0), camera.viewMatrix, camera.viewMatrix);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			Matrix4f.rotate((float)Math.toRadians(1), new Vector3f(0, 1, 0), camera.viewMatrix, camera.viewMatrix);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			Matrix4f.rotate((float)Math.toRadians(-1), new Vector3f(0, 1, 0), camera.viewMatrix, camera.viewMatrix);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Matrix4f.translate(new Vector3f(0,-0.01f,0), camera.viewMatrix, camera.viewMatrix);
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			Matrix4f.translate(new Vector3f(0,0.01f,0), camera.viewMatrix, camera.viewMatrix);
		}
		
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUseProgram(programId);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texIds[textureSelector]);
		
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
			glBindBuffer(GL_ARRAY_BUFFER,vboId);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
				
				glDrawElements(GL_TRIANGLES, cube.indicesCount, GL_UNSIGNED_BYTE, 0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
			glBindBuffer(GL_ARRAY_BUFFER,0);
			
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glUseProgram(0);
	}
	
	private void cleanUp(Cube c) {
		glUseProgram(0);
		glDetachShader(programId,vertShaderId);
		glDetachShader(programId,fragShaderId);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glDeleteBuffers(vboId);
		glDeleteBuffers(vboiId);
		glDeleteProgram(programId);
		glDeleteTextures(texIds[0]);
		glDeleteTextures(texIds[1]);
		
		Display.destroy();
	}
}
