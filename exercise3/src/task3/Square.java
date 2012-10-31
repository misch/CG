package task3;

import java.util.ArrayList;

import ex1.AbstractSimpleShape;

public class Square extends AbstractSimpleShape{
	public Square(){
		setVertices();
		setIndices();
//		setColors();
		setTextures();
	}
	
	protected void setTextures(){
		float textures[] = {0,0, 1,0, 1,1, 0,1};
		
		ArrayList<Float> t = new ArrayList<Float>();
		
		for(int i=0; i<textures.length;i++){
			t.add(textures[i]);
		}
		this.texels = t;
	}

	protected void setVertices(){
		// The vertex positions of the cube
		float vertices[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1};
		
		ArrayList<Float> v = new ArrayList<Float>();
		
		for (int i = 0;i<vertices.length;i++)
			v.add(vertices[i]);
		
		this.vertices = v;
	}
	
	protected void setIndices(){
		// The triangles (three vertex indices for each triangle)
		
		int indices[] = {0,2,3, 0,1,2};

		ArrayList<Integer> ind = new ArrayList<Integer>();
		
		for (int i = 0;i<indices.length;i++)
			ind.add(indices[i]);
		
		this.indices = ind;
	}
	
//	protected void setColors(){
//		// The vertex colors
//		float[] colors = {1,1,0, 0,1,1, 1,0,1, 1,0,0};
//		
//		ArrayList<Float> col = new ArrayList<Float>();
//		
//		for (int i = 0;i<colors.length;i++)
//			col.add(colors[i]);
//		
//		this.colors = col;
//	}
	
	@Override
	protected float x(float u, float v) {return 0;}
	protected float y(float u, float v) {return 0;}
	protected float z(float u, float v) {return 0;}
}
