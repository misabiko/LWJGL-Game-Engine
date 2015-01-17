package com.misabiko.LWJGLGameEngine.Resources.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL13;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Material;
import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public abstract class MTLParser {
	
	public static ArrayList<Material> parse(String parentPath, String fileName) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(parentPath+fileName)));
		String line;
		ArrayList<Material> materials = new ArrayList<Material>();
		int numMaterials = -1;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("newmtl ")) {
				materials.add(new Material(line.split(" ")[1]));
				numMaterials++;
			}
			if (line.startsWith("illum "))
				materials.get(numMaterials).illuminationModel = Integer.valueOf(line.split(" ")[1]);
			if (line.startsWith("Ka ")) {
				materials.get(numMaterials).ambientColor = new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("Kd ")) {
				materials.get(numMaterials).diffuseColor = new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("Ks ")) {
				materials.get(numMaterials).specularColor = new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("Ns ")) {
				materials.get(numMaterials).specularExponent = Float.valueOf(line.split(" ")[1]);
			}
			if (line.startsWith("map_kd ") || line.startsWith("map_Kd ")) {
				String extension = line.split(" ")[1].substring(line.split(" ")[1].length()-3, line.split(" ")[1].length());
				if (extension.equals("tga")) {
					ByteBuffer buffer = TGALoader.loadImage(new FileInputStream(parentPath+line.split(" ")[1]), true);
					materials.get(numMaterials).diffuseTextureMap = new Texture(buffer, TGALoader.getLastWidth(), TGALoader.getLastTexHeight(), GL13.GL_TEXTURE0);
				}else if (extension.equals("png"))
					materials.get(numMaterials).diffuseTextureMap = new Texture(parentPath, line.split(" ")[1], GL13.GL_TEXTURE0);
				else
					System.out.println("The heck is a ."+extension+" file?");
			}
		}
		reader.close();
		return materials;
	}
}
