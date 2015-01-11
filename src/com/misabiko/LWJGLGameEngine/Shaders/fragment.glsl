#version 330 core

uniform mat4 modelMatrix;
uniform mat3 normalMatrix;

uniform vec3 lightPosition;
uniform vec3 lightIntensities;

in VertexData {
	vec4 position;
	vec4 color;
	vec3 normal;
} data;

layout(location = 0) out vec4 out_Color;

void main(void) {
//	Rotate normal using adapted modelMatrix
	vec3 normal = normalize(normalMatrix * data.normal);
	
	vec3 fragPosition = vec3(modelMatrix * data.position);
	
	vec3 surfaceToLight = lightPosition - fragPosition;
	
	float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	brightness = clamp(brightness, 0, 1);
	
	vec4 surfaceColor = data.color;
	out_Color = vec4(brightness * lightIntensities * surfaceColor.rgb, surfaceColor.a);
}