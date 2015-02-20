#version 330 core

layout(location = 0) in vec2 in_Position;
layout(location = 1) in vec2 in_TexCoords;

out VertexData {
	vec2 texCoords;
} data;

void main(void) {
	data.texCoords = in_TexCoords;
    
	vec2 vertexPosition_homoneneousspace = in_Position - vec2(400,300); // [0..800][0..600] -> [-400..400][-300..300]
    vertexPosition_homoneneousspace /= vec2(400,300);
    gl_Position =  vec4(vertexPosition_homoneneousspace,0,1);
}