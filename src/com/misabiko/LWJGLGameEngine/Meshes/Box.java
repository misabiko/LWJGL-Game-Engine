package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;

public class Box extends Mesh{
	
	public Box(float x, float y, float z, float w, float h, float d, Color[] colors) {
		super(x,y,z,w,h,d, new TexturedVertex[] {
//				Vertices are set in a counterclockwise manner starting from bottom-left
//				Front Face
				new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[0], 	0f, 1f),	//0
				new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[0], 	1f, 1f),	//1
				new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[0], 	1f, 0f),	//2
				new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[0], 	0f, 0f),	//3
//				Back Face
				new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[1], 	1f, 1f),	//4
				new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[1], 	0f, 1f),	//5
				new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[1], 	0f, 0f),	//6
				new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[1], 	1f, 0f),	//7
//				Left Face
				new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[2], 	0f, 1f),	//8
				new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[2], 	1f, 1f),	//9
				new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[2], 	1f, 0f),	//10
				new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[2], 	0f, 0f),	//11
//				Right Face
				new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[3], 	0f, 1f),	//12
				new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[3], 	1f, 1f),	//13
				new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[3], 	1f, 0f),	//14
				new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[3], 	0f, 0f),	//15
//				Top Face
				new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[4], 	0f, 0f),	//16
				new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[4], 	1f, 0f),	//17
				new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[4], 	1f, 1f),	//18
				new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[4], 	0f, 1f),	//19
//				Bottom Face
				new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[5], 	0f, 1f),	//20
				new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[5], 	1f, 1f),	//21
				new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[5], 	1f, 0f),	//22
				new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[5], 	0f, 0f)		//23
			}, new byte[] {
//					Sets the order in which the vertices should be used to produce triangles
//					Front Face
					0,1,3,
					1,2,3,
//					Back Face
					4,7,5,
					5,7,6,
//					Left Face
					8,9,11,
					9,10,11,
//					Right Face
					12,13,15,
					13,14,15,
//					Top Face
					16,19,17,
					19,18,17,
//					Bottom Face
					20,21,23,
					21,22,23
				});
	}
	
	public Box(float x, float y, float z, float w, float h, float d, float r, float g, float b, float a) {
		this(x,y,z,w,h,d, new Color[] {
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a)
			}
		);
	}
	
	public Box(float x, float y, float z, float w, float h, float d, Color color) {
		this(x,y,z,w,h,d, new Color[] {
				color,
				color,
				color,
				color,
				color,
				color
			}
		);
	}
	
	public Box(float x, float y, float z, float w, float h, float d) {
		this(x,y,z,w,h,d, new Color[] {
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor
			}
		);
	}
}
