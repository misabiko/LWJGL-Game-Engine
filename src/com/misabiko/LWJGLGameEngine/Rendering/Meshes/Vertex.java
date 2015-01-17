package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.awt.Color;

import javax.vecmath.Vector4f;

public class Vertex {
	public float[] position,aColor,dColor,sColor,normal,texCoords;
	public static final int posElementCount = 4;
	public static final int normElementCount = 3;
	public static final int aColorElementCount = 4;
	public static final int dColorElementCount = 4;
	public static final int sColorElementCount = 4;
	public static final int texCoordsElementCount = 2;
	
	public static final int elementCount = posElementCount+normElementCount+aColorElementCount+dColorElementCount+sColorElementCount+texCoordsElementCount;
	
	public static final int bytesPerFloat = 4;
	
	public static final int normOffset = bytesPerFloat*posElementCount;
	public static final int aColorOffset = normOffset+(bytesPerFloat*normElementCount);
	public static final int dColorOffset = aColorOffset+(bytesPerFloat*aColorElementCount);
	public static final int sColorOffset = dColorOffset+(bytesPerFloat*dColorElementCount);
	public static final int texCoordsOffset = sColorOffset+(bytesPerFloat*sColorElementCount);
	
	public Vertex(float x, float y, float z, float nX, float nY, float nZ, float aR, float aG, float aB, float aA, float dR, float dG, float dB, float dA, float sR, float sG, float sB, float sA, float s, float t) {
		position = new float[] {x,y,z,1f};
		normal = new float[] {nX,nY,nZ};
		aColor = new float[] {aR,aG,aB,aA};
		dColor = new float[] {dR,dG,dB,dA};
		sColor = new float[] {sR,sG,sB,sA};
		texCoords = new float[] {s,t};
	}
	
	public Vertex(float x, float y, float z, float nX, float nY, float nZ, Vector4f aColor, Vector4f dColor, Vector4f sColor, float s, float t) {
		this(
			x,y,z,
			nX,nY,nZ,
			aColor.x,aColor.y,aColor.z,aColor.w,
			dColor.x,dColor.y,dColor.z,dColor.w,
			sColor.x,sColor.y,sColor.z,sColor.w,
			s,t);
	}
	
//	public Vertex(Vector3f pos, Vector3f norm, Vector4f aColor, Vector4f dColor, Vector4f sColor, Vector2f st) {
//		pos.get(position);
//		norm.get(normal);
//		aColor.get(this.aColor);
//		dColor.get(this.dColor);
//		sColor.get(this.sColor);
//		st.get(texCoords);
//	}
	
	public float[] getElements() {
		return new float[] {
				position[0],
				position[1],
				position[2],
				position[3],
				
				normal[0],
				normal[1],
				normal[2],
				
				aColor[0],
				aColor[1],
				aColor[2],
				aColor[3],
				
				dColor[0],
				dColor[1],
				dColor[2],
				dColor[3],
				
				sColor[0],
				sColor[1],
				sColor[2],
				sColor[3],
				
				texCoords[0],
				texCoords[1]
		};
	}
}
