#version 150
// Default vertex shader
#define MAX_LIGHTS 8
// Uniform variables, set in main program
uniform mat4 projection; 
uniform mat4 modelview;
uniform float light_strength[MAX_LIGHTS];
uniform vec3 light_position[MAX_LIGHTS];
uniform float reflection_coeff

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
	// TODO
	
	// Compute radiance
	// (c[i] = light_strength_i/|light_position_i-position|^2)
	// TODO 
	
	// Compute diffuse light
	// diffuse_light = sum_i(c[i]*reflection_coeff*max(dot(modelview * vec4(normal,0), L[i]),0))
	// TODO
	
	// Pass texture coordinates to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
}