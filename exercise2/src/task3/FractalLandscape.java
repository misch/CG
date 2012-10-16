package task3;

import java.util.ArrayList;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import ex1.AbstractSimpleShape;

// TODO: 
//	add more (and reasonable) colors
//	change interval of random value for each step

public class FractalLandscape extends AbstractSimpleShape{
	private int size, cycles;
	private float heights[][];
	
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.cycles = size;
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[this.size][this.size];
			
		heights[0][0] = -20;
		heights[this.size-1][0]=-20;
		heights[0][this.size-1]=-20;
		heights[this.size-1][this.size-1]=100;

		int width = this.size-1;
		float randomScale = this.size;
//		float randomScale = 0;
		for (int c=0; c<=cycles; c++){
//			randomScale = 3/(c+1);
			randomScale /=2;
			diamondStep(width, randomScale);
//			randomScale/=2;
			squareStep(width, randomScale);
			width /=2;
		}
		
		composeVertexData();
	}

	private void diamondStep(int width, float randomScale){
		int distance = width/2;
		for (int i=0; i<this.size-1; i+=width){
			for (int j=0; j<this.size-1; j+= width){
								
				int iMiddle = i + distance;
				int jMiddle = j+distance;
				
				setDiamondHeight(iMiddle, jMiddle, distance, randomScale);
			}
		}
	}
	
	private void squareStep(int width, float randomScale){
		int distance = width/2;
		
		for (int i = distance; i+distance < this.size; i+=width){
			for (int j = distance; j+distance<this.size; j+=width){
				setSquareHeight(i,j-distance,distance, randomScale);
				setSquareHeight(i,j+distance,distance, randomScale);
				setSquareHeight(i-distance,j,distance, randomScale);
				setSquareHeight(i+distance,j,distance, randomScale);
			}
		}
	}
	
	private void setDiamondHeight(int iDiamond, int jDiamond, int distance, float randomScale) {
		
		float heightDiamond = (heights[iDiamond-distance][jDiamond-distance]+
			heights[iDiamond-distance][jDiamond+distance]+
			heights[iDiamond+distance][jDiamond-distance]+
			heights[iDiamond+distance][jDiamond+distance])/4;
		
			heights[iDiamond][jDiamond]=heightDiamond + (((float) Math.random()-0.5f)*randomScale);
	}
	
	private void setSquareHeight(int iSquare, int jSquare, int distance, float randomScale){
		
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
		heights[iSquare][jSquare]=avg + (((float)Math.random()-0.5f) * randomScale);
	}

	private void composeVertexData(){
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

	private Color3f getCol(float f) {
		Color3f col;
		
		col = new Color3f(1,1,1);
		
		if(f <= 70)
			col = new Color3f(0,1,0);
		
		if (f> 70)
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
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return new Vector3f(0,1,0);
		}
	}

	@Override
	protected float x(float u, float v) {return 0;}
	protected float y(float u, float v) {return 0;}
	protected float z(float u, float v) {return 0;}
}
