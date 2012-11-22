package task1;

import jrtr.Shape;
import jrtr.VertexData;

public class NormalCube {

	private VertexData vertexData;
	// Make a simple geometric object: a cube
	public NormalCube(){
		// The vertex positions of the cube
		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
		         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
			  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
				 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
				 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
				-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face

		// The vertex colors
		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1};
	
		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1,
		         	-1,0,0, -1,0,0, -1,0,0, -1,0,0,
			  	    0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1, 
				    1,0,0, 1,0,0, 1,0,0, 1,0,0,
				    0,1,0, 0,1,0, 0,1,0, 0,1,0, 
				    0,-1,0, 0,-1,0, 0,-1,0,  0,-1,0};  
		
		float uv[] = {0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1};
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		vertexData = new VertexData(24);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
		
		// The triangles (three vertex indices for each triangle)
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22};	// bottom face
	
		vertexData.addIndices(indices);
	}
	
	public Shape getShape(){
		return new Shape(vertexData);
	}
}
