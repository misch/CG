#version 150
// Default vertex shader
#define MAX_LIGHTS 2
// Uniform variables, set in main program
uniform mat4 projection; 
uniform mat4 modelview;
uniform mat4 camera;
uniform float source_radiance[MAX_LIGHTS];
uniform vec3 light_position[MAX_LIGHTS];
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
	diffuse_light = 0;
	for (int i = 0; i<MAX_LIGHTS; i++){
		vec3 light_direction = (camera*vec4(light_position[i],1)-modelview*position).xyz;
		float radiance = source_radiance[i]/dot(light_direction,light_direction);
		diffuse_light += radiance * reflection_coeff * max(0,dot((modelview*vec4(normal,0)).xyz, normalize(light_direction)));
	}
	
	
	// Pass texture coordinates to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
}