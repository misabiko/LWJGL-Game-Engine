package com.misabiko.LWJGLGameEngine.UI;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Resources.Textures.Fonts.Fonts;

public class Label extends UIComponent {
	public String string;
	
	public Label(String str) {
		string = str;
	}

	@Override
	protected void renderSelf() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Fonts.pixfont.texture.texId);
		
		GL20.glUseProgram(OpenGLHandler.program2D.id);
		
			GL30.glBindVertexArray(Fonts.pixfont.VAO);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Fonts.pixfont.IBO);
					for (char c : string.toCharArray()) {
//						GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, Fonts.pixfont.indiceCount, GL11.GL_UNSIGNED_INT, 0, Fonts.pixfont.charArray.indexOf(c)*4);
						GL11.glDrawElements(GL11.GL_TRIANGLES, Fonts.pixfont.indiceCount, GL11.GL_UNSIGNED_INT, 0);
					}
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			
		GL20.glUseProgram(0);
	}
	
}
