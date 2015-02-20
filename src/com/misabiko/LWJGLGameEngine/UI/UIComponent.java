package com.misabiko.LWJGLGameEngine.UI;

import java.util.ArrayList;

public abstract class UIComponent {
	public ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public void render() {
		renderSelf();
		
		for (UIComponent component : components)
			component.render();
	}
	
	protected abstract void renderSelf();
}
