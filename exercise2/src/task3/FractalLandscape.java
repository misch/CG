package task3;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.VertexData;

import ex1.AbstractSimpleShape;

public class FractalLandscape extends AbstractSimpleShape{
	private int size, cycles;
	private float heights[][];
//	private float granularity;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.cycles = size;
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[this.size][this.size];
//		this.granularity = granularity;
			
		heights[0][0] = 0;
		heights[this.size-1][0]=0;
		heights[0][this.size-1]=0;
		heights[this.size-1][this.size-1]=cornerHeight;

		for (int c=0; c<=cycles; c++){
			diamondStep((int)(this.size/(Math.pow(2, c))));
			squareStep((int)(this.size/(Math.pow(2, c))));
		}
		
//		diamondStep(0,0,this.size-1,this.size-1);
		
		addVerticesAndTrianglesandColors();
	}

	private void diamondStep(int width){
		int distance = width/2;
		for (int i=0; i<this.size-1; i+=width){
			for (int j=0; j<this.size-1; j+= width){
								
				int iMiddle = i + distance;
				int jMiddle = j+distance;
				
				setDiamondHeight(iMiddle, jMiddle, distance);
			}
		}
	}
	
	private void squareStep(int width){
		int distance = width/2;
		
//		for (int i = 0; i<this.size; i+= width-1){
//			for (int j = 0; j<this.size; j+= width-1){
//				setSquareHeight(i, j+distance, distance);
//				setSquareHeight(i+distance, j, distance);
//			}
//		}
		
		for (int i = 0; i+distance<this.size; i++){
			int first = 0;
			if (i%2==0)
				first = 1;
			
			for(int j = first; j+distance<this.size; j+=2){
				setSquareHeight(i, j+distance, distance);
				setSquareHeight(i+distance,j, distance);
			}
		}
		
//		for (int i = 0; i<=cycles;i++){
//			setSquareHeight(i, i+distance, distance);
//			setSquareHeight(i+distance, i, distance);
//		}
		
//		for (int i = distance; i <: cycles; i++){
//			setSquareHeight(i, i-distance, distance);
//			setSquareHeight(i+distance, )
//		}
	}
	
	private void setDiamondHeight(int iDiamond, int jDiamond, int distance) {
		
		float heightDiamond = (heights[iDiamond-distance][jDiamond-distance]+
			heights[iDiamond-distance][jDiamond+distance]+
			heights[iDiamond+distance][jDiamond-distance]+
			heights[iDiamond+distance][jDiamond+distance])/4;
		
			heights[iDiamond][jDiamond]=heightDiamond + (float)Math.random();
	}
	
	private void setSquareHeight(int iSquare, int jSquare, int distance){
		
		int dividers = 0;
		float sum = 0;
		
		// finde Nachbar(en)		
		if (iSquare-distance >= 0){
			// finde linken Nachbar
			float heightLinkerNachbar = heights[iSquare-distance][jSquare];
			dividers++;
			sum += heightLinkerNachbar;
		}
		
		if (iSquare+distance < this.size){
			// finde rechten Nachbar
			float heightRechterNachbar = heights[iSquare+distance][jSquare];
			dividers++;
			sum += heightRechterNachbar;
		}
		
		if (jSquare-distance >= 0){
			// finde oberen Nachba
			float heightObererNachbar = heights[iSquare][jSquare-distance];
			dividers ++;
			sum += heightObererNachbar;
		}
		
		if (jSquare+distance < this.size){
			// finde unteren Nachbar
			float heightUntererNachbar = heights[iSquare][jSquare+distance];
			dividers++;
			sum+=  heightUntererNachbar;
		}
		
		// berechne Durchschnitt...
		float avg = sum/dividers;
		
		//...und setze Höhenwert
		heights[iSquare][jSquare]=avg + (float)Math.random();
	}

	private void addVerticesAndTrianglesandColors(){
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> ind = new ArrayList<Integer>();
		ArrayList<Float> c = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		
		for(int i = 0; i<this.size-1; i++){
			for(int j = 0; j<this.size-1;j++){
				int indTopLeft = addVertex(v, new Point3f(i,heights[i][j],j));
				addColor(c,getCol(heights[i][j]));
				addNormal(n, computeNormal(i,j));
				
				int indBottomLeft = addVertex(v, new Point3f(i,heights[i][j+1],j+1));
				addColor(c,getCol(heights[i][j+1]));
				addNormal(n, computeNormal(i,j+1));
				
				int indBottomRight = addVertex(v, new Point3f(i+1,heights[i+1][j+1], j+1));
				addColor(c,getCol(heights[i+1][j+1]));
				addNormal(n, computeNormal(i+1,j+1));
				
				int indTopRight = addVertex(v, new Point3f(i+1, heights[i+1][j],j));
				addColor(c,getCol(heights[i+1][j]));
				addNormal(n, computeNormal(i+1,j));
				
				addTriangle(ind, indTopLeft, indBottomLeft, indTopRight);
				addTriangle(ind, indTopRight, indBottomRight, indBottomLeft);
			}
		}
		this.vertices = v;
		this.indices = ind;
		this.colors = c;
		this.normals = n;
	}
	
	private void addNormal(ArrayList<Float> n, Vector3f normal) {
		n.add(normal.x);
		n.add(normal.y);
		n.add(normal.z);
	}

	private Color3f getCol(float f) {
		Color3f col;
		
		col = new Color3f(1,1,1);
		
		if(f <= 0.1)
			col = new Color3f(0,1,0);
		
		if (f>= 0.1)
			col = new Color3f(0,1,.5f);
		
		if( f>=0.5)
			col = new Color3f(0,1,1);
		
		if(f>=0.8)
			col = new Color3f(1,1,1);

		return col;
	}
	
	private Vector3f computeNormal(int i, int j) {
		try {
			Vector3f v = new Vector3f(i, heights[i][j], j);
			Vector3f down = new Vector3f(i + 1, heights[i][j + 1], j + 1);
			Vector3f right = new Vector3f(i + 1, heights[i + 1][j],j);
			down.sub(v);
			right.sub(v);
			Vector3f cross = new Vector3f();
			cross.cross(down, right);
			return cross;
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Vector3f(0,1,0);
		}
	}

	@Override
	protected float x(float u, float v) {return 0;}
	protected float y(float u, float v) {return 0;}
	protected float z(float u, float v) {return 0;}
}
