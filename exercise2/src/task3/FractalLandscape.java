package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.AbstractSimpleShape;

/* Konzept:
 * 1. Berechne die Array-Grösse (2^n + 1, n Natürliche Zahl) und 
 * 2. 
 */

public class FractalLandscape extends AbstractSimpleShape{
	private int size;
	private float heights[][];
	private float granularity;
	private ArrayList<Float> v;
	private ArrayList<Integer> i;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[size][size];
		this.granularity = granularity;
		
		setUpCorners(cornerHeight);
	}

	private void setUpCorners(float cornerHeight) {
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		
		heights[0][0] = cornerHeight;
		heights[size-1][0] = cornerHeight;
		heights[0][size-1] = cornerHeight;
		heights[size-1][size-1] = cornerHeight;
		
		int index1 = addVertex(v,new Point3f(0,0,heights[0][0]));
		int index2 = addVertex(v,new Point3f(0,size-1,heights[0][size-1]));
		int index3 = addVertex(v,new Point3f(size-1,0,heights[size-1][0]));
		
		addTriangle(indices,index1, index2, index3);
		
		this.vertices = v;
		this.indices = indices;		
	}
	
	private float getHeight(int i, int j){
		return heights[i][j];
	}
	
	private void setColors(){
		
		ArrayList<Float> c = new ArrayList<Float>();
		
		for(int i=0;i<vertices.size()/3;i++){
			Color3f col = new Color3f((i/4)%2,(i/4)%2,(i/4)%2);
			addColor(c,col);
		}
		this.colors = c;
	}
	
	private void diamondStep(int x, int z, int size){
		
	}
	
	private void squareStep(){
		
	}

	@Override
	protected float x(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float y(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float z(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}
}
