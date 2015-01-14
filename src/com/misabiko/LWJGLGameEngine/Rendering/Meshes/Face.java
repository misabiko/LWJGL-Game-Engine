package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Face {
	public String material;
	public Vector3f[] vertices, normals;
	public Vector2f[] texCoords;
	
	public Face(Vector3f[] vertices, Vector2f[] texCoords, Vector3f[] normals, String material) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.material = material;
	}
}
