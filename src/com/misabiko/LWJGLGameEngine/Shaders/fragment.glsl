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
	vec3 surfacePos = vec3(modelMatrix * data.position);
	vec4 surfaceColor = data.color;
	vec3 surfaceToLight = normalize(lightPosition - surfacePos);
	
	float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
	vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * lightIntensities;
	out_Color = vec4(diffuse , surfaceColor.a);
}