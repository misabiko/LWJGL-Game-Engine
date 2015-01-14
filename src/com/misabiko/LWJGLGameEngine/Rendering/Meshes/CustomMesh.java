package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public class CustomMesh extends Mesh {
	private ArrayList<Material> materials;
	private ArrayList<Face> faces;
	
	
	public CustomMesh(TexturedVertex[] vertices, byte[] indices, ArrayList<Face> f, ArrayList<Material> mtl) {
		super(vertices, indices);
		
		faces = f;
		materials = mtl;
	}

}
