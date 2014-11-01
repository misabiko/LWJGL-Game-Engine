package com.misabiko.LWJGLGameEngine.Meshes;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Core.Core;

public class Cuby extends Box{
	
	public Cuby() {
		super(0, 0, 0, 0.5f,0.5f,0.5f);
	}
	
	public void update() {
		Matrix4f.setIdentity(modelMatrix);
		
		Matrix4f.translate(pos, modelMatrix, modelMatrix);
		
		Matrix4f.rotate(angleY, new Vector3f(0,1f,0), modelMatrix, modelMatrix);
		Matrix4f.rotate(angleX, new Vector3f(1f,0,0), modelMatrix, modelMatrix);
		
		
		glUseProgram(Core.texProgram.id);
		
			Core.projectionMatrix.store(Core.matrixBuffer);
			Core.matrixBuffer.flip();
			glUniformMatrix4(Core.texProgram.projectionMatrixLocation, false, Core.matrixBuffer);
			
			Core.camera.viewMatrix.store(Core.matrixBuffer);
			Core.matrixBuffer.flip();
			glUniformMatrix4(Core.texProgram.viewMatrixLocation, false, Core.matrixBuffer);
			
			modelMatrix.store(Core.matrixBuffer);
			Core.matrixBuffer.flip();
			glUniformMatrix4(Core.texProgram.modelMatrixLocation, false, Core.matrixBuffer);
			
		
		glUseProgram(0);
	}
}
