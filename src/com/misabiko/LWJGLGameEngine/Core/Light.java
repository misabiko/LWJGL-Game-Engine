package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Light {
	public Vector4f position;
	public Vector3f intensities, coneDirection;
	public float ambientCoefficient, attenuation, coneAngle;

	public Light(Vector4f pos, Vector3f color, float amb, float att, float cAngle, Vector3f cDirection) {
		position = pos;
		intensities = color;
		ambientCoefficient = amb;
		attenuation = att;
		coneAngle = cAngle;
		coneDirection = cDirection;
	}
	
	public Light(float x, float y, float z, float w) {
		this(new Vector4f(x,y,z,w), new Vector3f(1f,1f,1f), 0.005f, 0.5f, 180f, new Vector3f(0f,-1f,0f));
	}
}
