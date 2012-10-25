package ex1;
import java.util.ArrayList;



public class Cube extends AbstractSimpleShape{
	public Cube(){
		setVertices();
		setColors();
		setIndices();
	}
	
	protected void setIndices(){
		// The triangles (three vertex indices for each triangle)
		
		int[] indices = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22};	// bottom face

		ArrayList<Integer> ind = new ArrayList<Integer>();
		
		for (int i = 0;i<indices.length;i++)
			ind.add(indices[i]);
		
		this.indices = ind;
	}
	protected void setVertices(){
		// The vertex positions of the cube
		float[] vertices = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
		         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
			  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
				 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
				 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
				-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face
		
		ArrayList<Float> v = new ArrayList<Float>();
		
		for (int i = 0;i<vertices.length;i++)
			v.add(vertices[i]);
		
		this.vertices = v;
	}
	
	protected void setColors(){
		// The vertex colors
		float[] colors = {1,0,1, 1,0,1, 1,0,0, 1,0,0,
				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1};
		
		ArrayList<Float> col = new ArrayList<Float>();
		
		for (int i = 0;i<colors.length;i++)
			col.add(colors[i]);
		
		this.colors = col;
	}
	
	protected float x(float u, float v){
		return 0;
	}
	
	protected float y(float u, float v){
		return 0;
	}
	
	protected float z(float u, float v){
		return 0;
	}
}
