package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import javax.vecmath.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public class Material {
	public String name;
	public Vector3f ambientColor, diffuseColor, specularColor;
	public int illuminationModel;
	public Texture diffuseTextureMap;
	
	public Material(String name) {
		this.name = name;
	}
}
