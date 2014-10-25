package com.misabiko.LWJGLGameEngine.Resources.Textures;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {
	public int texId = 0;
	
	public Texture(String filename, int textureUnit) {
		int texWidth = 0;
		int texHeight = 0;
		ByteBuffer buffer = null;
		
		try {
			InputStream input = new FileInputStream("src/com/misabiko/LWJGLGameEngine/Resources/Textures/"+filename);
			
			PNGDecoder decoder = new PNGDecoder(input);
			texWidth = decoder.getWidth();
			texHeight = decoder.getHeight();
			
			buffer = ByteBuffer.allocateDirect(4*texWidth*texHeight);
			decoder.decode(buffer, texWidth*4, Format.RGBA);
			
			buffer.flip();
			
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		texId = glGenTextures();
		
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, texId);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, texWidth, texHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
	}
}
