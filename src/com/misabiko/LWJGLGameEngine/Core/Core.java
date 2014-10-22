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
	private int vaoId = 0;
	private Program colProgram, texProgram;
	private FloatBuffer matrixBuffer;
	private int[] texIds = new int[2];
	private Box cuby;
	private ArrayList<Box> Boxes = new ArrayList<Box>();;
	private Camera camera;
	
//	TODO set viewMatrix rotation axis
//	TODO make viewMatrix translation independant from rotation
//	TODO put textures into the box objects
	
	public Core() {
		initGL();
		init();
		initShaders();
		initTextures();
		initMatrices();
		
		while (!Display.isCloseRequested()) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			input();
			
			for (Box Box : Boxes) {
				update(Box);
				
				render(Box);
				
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
		cuby = new Box(0, 0, 0, 0.5f,0.5f,0.5f);
		Boxes.add(cuby);
		
		Boxes.add(new Box(-1f, -1.5f, -1f,1f,1f,1f));
		
		Boxes.add(new Box(-3f, -2f, -2f, 8f,0.5f,4f));
		
		Boxes.get(0).textured = true;
		Boxes.get(1).textured = true;
		Boxes.get(2).textured = true;
		
		camera = new Camera(-1f, -1.5f, -1f);
		
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
			
			for (Box Box : Boxes) {
				Box.vboId = glGenBuffers();
				
				glBindBuffer(GL_ARRAY_BUFFER,Box.vboId);
				
					glBufferData(GL_ARRAY_BUFFER,Box.verticesBuffer,GL_STATIC_DRAW);
					
				glBindBuffer(GL_ARRAY_BUFFER,0);
				
				Box.vboiId = glGenBuffers();
					
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Box.vboiId);
					glBufferData(GL_ELEMENT_ARRAY_BUFFER, Box.indicesBuffer, GL_STATIC_DRAW);
				
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.angleX += camera.rotateSpeed;
		}
//		else {
//			if (camera.angleX > 0) {
//				camera.angleX -= camera.rotateSpeed;
//			}
//		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.angleX -= camera.rotateSpeed;
		}
//		else {
//			if (camera.angleX < 0) {
//				camera.angleX += camera.rotateSpeed;
//			}
//		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.angleY -= camera.rotateSpeed;
		}
//		else {
//			if (camera.angleY < 0) {
//				camera.angleY += camera.rotateSpeed;
//			}
//		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.angleY += camera.rotateSpeed;
		}
//		else {
//			if (camera.angleY > 0) {
//				camera.angleY -= camera.rotateSpeed;
//			}
//		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			cuby.pos.x -= cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			cuby.pos.x += cuby.speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cuby.pos.y += cuby.speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_R)){
			cuby.pos.y -= cuby.speed;
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cuby.pos.z -= cuby.speed/2;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			cuby.pos.z += cuby.speed/2;
		}
		
	}
	
	private void update(Box Box) {
		
		Matrix4f.setIdentity(camera.viewMatrix);
		Matrix4f.setIdentity(cuby.modelMatrix);
		
		Matrix4f.translate(cuby.pos, cuby.modelMatrix, cuby.modelMatrix);
		Matrix4f.translate(cuby.pos.negate(new Vector3f()), camera.viewMatrix, camera.viewMatrix);
		
		Matrix4f.rotate(camera.angleX, new Vector3f(-cuby.pos.x,0,0), camera.viewMatrix, camera.viewMatrix);
		Matrix4f.rotate(camera.angleY, new Vector3f(0,1f,0), camera.viewMatrix, camera.viewMatrix);
		
//		Matrix4f.translate(new Vector3f(0,0,-1f), camera.viewMatrix, camera.viewMatrix);
		
//		Matrix4f.translate(new Vector3f(0f,0f,-1f), camera.viewMatrix, camera.viewMatrix);
		
		if (Box.textured) {
			glUseProgram(texProgram.id);
			
				projectionMatrix.store(matrixBuffer);
				matrixBuffer.flip();
				glUniformMatrix4(texProgram.projectionMatrixLocation, false, matrixBuffer);
				
				camera.viewMatrix.store(matrixBuffer);
				matrixBuffer.flip();
				glUniformMatrix4(texProgram.viewMatrixLocation, false, matrixBuffer);
				
				Box.modelMatrix.store(matrixBuffer);
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
			
			Box.modelMatrix.store(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4(colProgram.modelMatrixLocation, false, matrixBuffer);
			
		
		glUseProgram(0);
		}
		
	}
	
	private void render(Box Box) {
		if (Box.textured) {
			glUseProgram(texProgram.id);
			
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texIds[0]);
				
				glBindVertexArray(vaoId);
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				glEnableVertexAttribArray(2);
				
					glBindBuffer(GL_ARRAY_BUFFER,Box.vboId);
					
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Box.vboiId);
					
						glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
						glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
						glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
						
						glDrawElements(GL_TRIANGLES, Box.indicesCount, GL_UNSIGNED_BYTE, 0);
					
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
			
				glBindBuffer(GL_ARRAY_BUFFER,Box.vboId);
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Box.vboiId);
				
					glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, 0);
					glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, Vertex.colorOffset);
					glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.bytesPerFloat*TexturedVertex.elementCount, TexturedVertex.stOffset);
					
					glDrawElements(GL_TRIANGLES, Box.indicesCount, GL_UNSIGNED_BYTE, 0);
					
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
		
		for (Box Box : Boxes) {
			glDeleteBuffers(Box.vboId);
			glDeleteBuffers(Box.vboiId);
		}
		
		glDeleteTextures(texIds[0]);
		glDeleteTextures(texIds[1]);
		
		colProgram.cleanUp();
		texProgram.cleanUp();
		
		Display.destroy();
	}
}
