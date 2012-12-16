#version 150
// Default vertex shader
#define MAX_LIGHTS 8
// Uniform variables, set in main program
uniform mat4 projection; 
uniform mat4 modelview;
uniform sampler2D myTexture;

varying float pattern;

// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;
// in vec3 tangent;

// Output variables for fragment shader
out vec2 frag_texcoord;
out vec3 frag_normal;
out vec4 frag_position;
out vec3 frag_tangent;
out vec3 frag_bi_tangent;

void main()
{		
	// Pass texture coordinates, normals and vertex positions to fragment shader, OpenGL automatically
	// interpolates them to each pixel (in a perpectively correct manner)
	vec3 tangent = vec3(1,0,0);
	vec3 bi_tangent = vec3(0,1,0);
	
	frag_tangent = (modelview * vec4(tangent)).xyz;
	frag_bi_tangent = (modelview * vec4(bi_tangent)).xyz;
	
	frag_normal = normal;
	frag_texcoord = texcoord;
	frag_position = position;
	frag_bump = vec4(0.3,0.3,0.3)*texture(myTexture, texcoord);

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
	//pattern=fract(4.0*(gl_Position.y+gl_Position.x+gl_Position.z));
}