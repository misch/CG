#version 150
// Default vertex shader
#define MAX_LIGHTS 2
// Uniform variables, set in main program
uniform mat4 projection; 
uniform mat4 modelview;
uniform float light_strength[MAX_LIGHTS];
uniform vec4 light_position[MAX_LIGHTS];
uniform float reflection_coeff;

// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;

// Output variables for fragment shader
//out float ndotl;
out float diffuse_light;
out vec2 frag_texcoord;

void main()
{		
	// Compute light direction 
	// (L[i] = (light_position_i - position)/|light_position_i-position|)
	vec4 lightDirection[MAX_LIGHTS];
	for (int i = 0; i<MAX_LIGHTS; i++){
		lightDirection[i] = normalize(light_position[i]-position[i]);
	}
	
	// Compute radiance
	// (c[i] = light_strength_i/|light_position_i-position|^2)
	float radiance[MAX_LIGHTS];
	for (int i = 0; i<MAX_LIGHTS; i++){
		radiance[i] = light_position[i]/dot((light_position[i]-position),(light_position[i]-position));
	}
	
	// Compute diffuse light
	// diffuse_light = sum_i(c[i]*reflection_coeff*max(dot(modelview * vec4(normal,0), L[i]),0))
	diffuse_light = 0;
	for (int i = 0; i<MAX_LIGHTS; i++){
		diffuse_light += radiance[i]*reflection_coeff*max(dot(modelview*vec4(normal,0),lightDirection[i],0));
	}
	
	// Pass texture coordinates to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
}