package com.misabiko.LWJGLGameEngine.Rendering.Lights;


import java.util.ArrayList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL20;

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;

public class Light {
	private static ArrayList<Light> lights = new ArrayList<Light>();
	
	private Vector4f position;
	private Vector3f intensities, coneDirection;
	private float ambientCoefficient, attenuation, coneAngle;

	public Light(Vector4f pos, Vector3f color, float amb, float att, float cAngle, Vector3f cDirection) {
		position = pos;
		intensities = color;
		ambientCoefficient = amb;
		attenuation = att;
		coneAngle = cAngle;
		coneDirection = cDirection;
		
		lights.add(this);
		int programId = OpenGLHandler.program.id;
		GL20.glUseProgram(programId);
		
		GL20.glUniform1f(GL20.glGetUniformLocation(programId, "numLights"),	Light.lights.size());
			int i = Light.lights.indexOf(this);
			GL20.glUniform4f(GL20.glGetUniformLocation(programId, "lights["+i+"].position"),				position.x,				position.y,			position.z,			position.w);
			GL20.glUniform3f(GL20.glGetUniformLocation(programId, "lights["+i+"].intensities"),			intensities.x,			intensities.y,		intensities.z);
			GL20.glUniform3f(GL20.glGetUniformLocation(programId, "lights["+i+"].coneDirection"),			coneDirection.x,		coneDirection.y,	coneDirection.z);
			GL20.glUniform1f(GL20.glGetUniformLocation(programId, "lights["+i+"].ambientCoefficient"),	ambientCoefficient);
			GL20.glUniform1f(GL20.glGetUniformLocation(programId, "lights["+i+"].attenuation"),			attenuation);
			GL20.glUniform1f(GL20.glGetUniformLocation(programId, "lights["+i+"].coneAngle"),				coneAngle);
			
		GL20.glUseProgram(0);
	}
	
	public Light(float x, float y, float z, float w) {
		this(new Vector4f(x,y,z,w), new Vector3f(1f,1f,1f), 0.005f, 0.5f, 180f, new Vector3f(0f,-1f,0f));
	}
}
