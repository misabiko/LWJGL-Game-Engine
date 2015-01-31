package com.misabiko.LWJGLGameEngine.Rendering.Lights;

import java.util.ArrayList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class Light {
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
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
		
		lights.add(this);
	}
	
	public Light(float x, float y, float z, float w) {
		this(new Vector4f(x,y,z,w), new Vector3f(1f,1f,1f), 0.005f, 0.5f, 180f, new Vector3f(0f,-1f,0f));
	}
}
