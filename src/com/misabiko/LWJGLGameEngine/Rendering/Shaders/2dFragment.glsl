#version 330 core

uniform sampler2D textureUnit;

in VertexData {
	vec2 texCoords;
} data;

layout(location = 0) out vec4 out_Color;

void main(void) {
//	out_Color = texture(textureUnit, data.texCoords);
	out_Color = vec4(1, 1, 1, 1);
}