package com.misabiko.LWJGLGameEngine.GameObjects;

import java.awt.Color;

import com.misabiko.LWJGLGameEngine.Meshes.Line;

public class Axis extends GameObject{
	public org.lwjgl.util.vector.Vector3f pos;

	public Axis(float x, float y, float z, float x2, float y2, float z2) {
		super(x, y, z, new Line(x,y,z,x2,y2,z2));
	}

	public Axis(float x, float y, float z, float x2, float y2, float z2, Color color) {
		super(x, y, z, new Line(x, y, z, x2, y2, z2, color));
	}

	public void update() {
		mesh.update(new org.lwjgl.util.vector.Vector3f(pos.x,pos.y,pos.z),angleX,angleY);
	}
}
