package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import javax.vecmath.Vector4f;

import org.lwjgl.util.vector.Vector3f;

public class Skybox extends Mesh{
	private static final Vector4f topColor = new Vector4f(0f,0f,1f,1f);
	private static final Vector4f bottomColor = new Vector4f(0f,0.8f,1f,1f);
	
	public Skybox() {
		super(new Vertex[] {
//				Vertices are set in a counterclockwise manner starting from bottom-left
//				Front Face
				new Vertex(-45f,	-45f,	45f,		0f,		0f,		1f,		bottomColor,	bottomColor,	bottomColor,	0f, 1f),	//0
				new Vertex(45f,		-45f,	45f,		0f,		0f,		1f,		bottomColor,	bottomColor,	bottomColor,	1f, 1f),	//1
				new Vertex(45f,		45f,	45f,		0f,		0f,		1f,		topColor,		topColor,		topColor,		1f, 0f),	//2
				new Vertex(-45f,	45f,	45f,		0f,		0f,		1f,		topColor,		topColor,		topColor,		0f, 0f),	//3
//				Back Face
				new Vertex(-45f,	-45f,	-45f,		0f,		0f,		1f,		bottomColor,	bottomColor,	bottomColor,	1f, 1f),	//4
				new Vertex(45f,		-45f,	-45f,		0f,		0f,		1f,		bottomColor,	bottomColor,	bottomColor,	0f, 1f),	//5
				new Vertex(45f,		45f,	-45f,		0f,		0f,		1f,		topColor,		topColor,		topColor,		0f, 0f),	//6
				new Vertex(-45f,	45f,	-45f,		0f,		0f,		1f,		topColor,		topColor,		topColor,		1f, 0f),	//7
//				Left Face
				new Vertex(-45f,	-45f,	-45f,		1f,		0f,		0f,		bottomColor,	bottomColor,	bottomColor,	0f, 1f),	//8
				new Vertex(-45f,	-45f,	45f,		1f,		0f,		0f,		bottomColor,	bottomColor,	bottomColor,	1f, 1f),	//9
				new Vertex(-45f,	45f,	45f,		1f,		0f,		0f,		topColor,		topColor,		topColor,		1f, 0f),	//10
				new Vertex(-45f,	45f,	-45f,		1f,		0f,		0f,		topColor,		topColor,		topColor,		0f, 0f),	//11
//				Right Face
				new Vertex(45f,		-45f,	45f,		1f,		0f,		0f,		bottomColor,	bottomColor,	bottomColor,	1f, 1f),	//12
				new Vertex(45f,		-45f,	-45f,		1f,		0f,		0f,		bottomColor,	bottomColor,	bottomColor,	0f, 1f),	//13
				new Vertex(45f,		45f,	-45f,		1f,		0f,		0f,		topColor,		topColor,		topColor,		0f, 0f),	//14
				new Vertex(45f,		45f,	45f,		1f,		0f,		0f,		topColor,		topColor,		topColor,		1f, 0f),	//15
//				Top Face
				new Vertex(-45f,	45f,	-45f,		0f,		1f,		0f,		topColor,		topColor,		topColor,		0f, 1f),	//16
				new Vertex(45f,		45f,	-45f,		0f,		1f,		0f,		topColor,		topColor,		topColor,		1f, 1f),	//17
				new Vertex(45f,		45f,	45f,		0f,		1f,		0f,		topColor,		topColor,		topColor,		1f, 0f),	//18
				new Vertex(-45f,	45f,	45f,		0f,		1f,		0f,		topColor,		topColor,		topColor,		0f, 0f),	//19
//				Bottom Face
				new Vertex(-45f,	-45f,	-45f,		0f,		1f,		0f,		bottomColor,	bottomColor,	bottomColor,	1f, 1f),	//20
				new Vertex(45f,		-45f,	-45f,		0f,		1f,		0f,		bottomColor,	bottomColor,	bottomColor,	0f, 1f),	//21
				new Vertex(45f,		-45f,	45f,		0f,		1f,		0f,		bottomColor,	bottomColor,	bottomColor,	0f, 0f),	//22
				new Vertex(-45f,	-45f,	45f,		0f,		1f,		0f,		bottomColor,	bottomColor,	bottomColor,	1f, 0f)		//23
			}, new byte[] {
//					Sets the order in which the vertices should be used to produce triangles
//					Front Face
					3,1,0,
					3,2,1,
//					Back Face
					5,7,4,
					6,7,5,
//					Left Face
					11,9,8,
					11,10,9,
//					Right Face
					15,13,12,
					15,14,13,
//					Top Face
					17,19,16,
					17,18,19,
//					Bottom Face
					23,21,20,
					23,22,21
				}, new Vector3f(90f,90f,90f), new Vector3f(0,0,0));
		ignoreLightning = true;
	}
}
