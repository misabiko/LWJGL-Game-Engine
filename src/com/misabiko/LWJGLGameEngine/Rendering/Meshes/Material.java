package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import javax.vecmath.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public class Material {
	public String name;
	public Vector3f ambientColor = new Vector3f(1f,1f,1f);
	public Vector3f diffuseColor = new Vector3f(1f,1f,1f);
	public Vector3f specularColor = new Vector3f(1f,1f,1f);
	public float specularExponent = 80f;
	public int illuminationModel;
	public Texture diffuseTextureMap;
	
	public Material(String name) {
		this.name = name;
	}
}
