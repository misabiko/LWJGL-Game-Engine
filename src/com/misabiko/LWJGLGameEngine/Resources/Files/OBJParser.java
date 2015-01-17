package com.misabiko.LWJGLGameEngine.Resources.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Material;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public abstract class OBJParser {
	public static Mesh parse(String parentPath, String fileName) throws FileNotFoundException, IOException {
		Mesh mesh;
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(parentPath+fileName+".obj")));
		String line;
		
		ArrayList<Material> materials = new ArrayList<Material>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
		ArrayList<String> mtlNames = new ArrayList<String>();
		
		ArrayList<String> vertIndices = new ArrayList<String>();
		ArrayList<String> normIndices = new ArrayList<String>();
		ArrayList<String> stIndices = new ArrayList<String>();
		
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("mtllib "))
				materials = MTLParser.parse(parentPath, line.split(" ")[1]);
			else if (line.startsWith("vn ")) {
				normals.add(new Vector3f((float)Float.valueOf(line.split(" ")[1]), (float)Float.valueOf(line.split(" ")[2]), (float)Float.valueOf(line.split(" ")[3])));
			}else if (line.startsWith("vt ")) {
				texCoords.add(new Vector2f((float)Float.valueOf(line.split(" ")[1]),1f-(float)Float.valueOf(line.split(" ")[2])));
			}else if (line.startsWith("v ")) {
				vertices.add(new Vector3f((float)Float.valueOf(line.split(" ")[1]), (float)Float.valueOf(line.split(" ")[2]), (float)Float.valueOf(line.split(" ")[3])));
			}else if (line.startsWith("usemtl "))
				mtlNames.add(line.split(" ")[1]);
			else if (line.startsWith("f ")) {
				vertIndices.add(line.split(" ")[1].split("/")[0]);
				vertIndices.add(line.split(" ")[2].split("/")[0]);
				vertIndices.add(line.split(" ")[3].split("/")[0]);
				
				vertIndices.add(line.split(" ")[1].split("/")[0]);
				vertIndices.add(line.split(" ")[3].split("/")[0]);
				vertIndices.add(line.split(" ")[4].split("/")[0]);
				
				stIndices.add(line.split(" ")[1].split("/")[1]);
				stIndices.add(line.split(" ")[2].split("/")[1]);
				stIndices.add(line.split(" ")[3].split("/")[1]);
				
				stIndices.add(line.split(" ")[1].split("/")[1]);
				stIndices.add(line.split(" ")[3].split("/")[1]);
				stIndices.add(line.split(" ")[4].split("/")[1]);
				
				normIndices.add(line.split(" ")[1].split("/")[2]);
				normIndices.add(line.split(" ")[2].split("/")[2]);
				normIndices.add(line.split(" ")[3].split("/")[2]);
				
				normIndices.add(line.split(" ")[1].split("/")[2]);
				normIndices.add(line.split(" ")[3].split("/")[2]);
				normIndices.add(line.split(" ")[4].split("/")[2]);
			}
		}
		reader.close();
		
		float xMin = vertices.get(0).x;
		float xMax = vertices.get(0).x;
		float yMin = vertices.get(0).y;
		float yMax = vertices.get(0).y;
		float zMin = vertices.get(0).z;
		float zMax = vertices.get(0).z;
		
		for (Vector3f vert : vertices) {
			if (vert.x < xMin)
				xMin = vert.x;
			if (vert.x > xMax)
				xMax = vert.x;
			if (vert.y < yMin)
				yMin = vert.y;
			if (vert.y > yMax)
				yMax = vert.y;
			if (vert.z < zMin)
				zMin = vert.z;
			if (vert.z > zMax)
				zMax = vert.z;
		}
		
		Vector3f size = new Vector3f(xMax-xMin, yMax-yMin, zMax-zMin);
		Vector3f center = new Vector3f(xMin+(size.x/2), yMin+(size.y/2), zMin+(size.z/2));

		Vector3f offset = new Vector3f(0,0,0);
		Vector3f.sub(offset, center, offset);
		Vector3f.add(center, offset, center);
		
		Vector3f[] vertArray = new Vector3f[vertIndices.size()];
		for (int i = 0; i < vertIndices.size(); i++) {
			vertArray[i] = Vector3f.add(vertices.get(Integer.valueOf(vertIndices.get(i))-1), offset, new Vector3f());
		}
		Vector3f[] normArray = new Vector3f[normIndices.size()];
		for (int i = 0; i < normIndices.size(); i++) {
			normArray[i] = normals.get(Integer.valueOf(normIndices.get(i))-1);
		}
		Vector2f[] stArray = new Vector2f[stIndices.size()];
		for (int i = 0; i < stIndices.size(); i++) {
			stArray[i] = texCoords.get(Integer.valueOf(stIndices.get(i))-1);
		}
		
		mesh = new Mesh(vertArray, normArray, stArray, vertArray.length, materials, size, center);
		return mesh;
	}
}
