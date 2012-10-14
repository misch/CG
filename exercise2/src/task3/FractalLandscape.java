package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Shape;

import ex1.AbstractSimpleShape;

public class FractalLandscape extends AbstractSimpleShape{
	private int size;
//	private Selectable heights[][];
	private float heights[][];
	private float granularity;
//	private int iTopLeft, iTopRight, iBottomLeft, iBottomRight;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[this.size][this.size];
		this.granularity = granularity;
			
		heights[0][0] = 0;
		heights[this.size-1][0]=0;
		heights[0][this.size-1]=0;
		heights[this.size-1][this.size-1]=cornerHeight;

		diamondStep(0,0,this.size-1,this.size-1);
		
		addVerticesAndTriangles();
		setColors();
	}

	private void squareStep(int iComputedDiamondPoint,int jComputedDiamondPoint, int distance){
	
		setSquareHeight(iComputedDiamondPoint-distance, jComputedDiamondPoint, distance);
		setSquareHeight(iComputedDiamondPoint, jComputedDiamondPoint-distance, distance);
		setSquareHeight(iComputedDiamondPoint+distance, jComputedDiamondPoint, distance);
		setSquareHeight(iComputedDiamondPoint, jComputedDiamondPoint+distance, distance);
		
		diamondStep(iComputedDiamondPoint, jComputedDiamondPoint-distance, iComputedDiamondPoint+distance, jComputedDiamondPoint);
		diamondStep(iComputedDiamondPoint, jComputedDiamondPoint, iComputedDiamondPoint+distance, jComputedDiamondPoint+distance);
		diamondStep(iComputedDiamondPoint-distance, jComputedDiamondPoint, iComputedDiamondPoint, jComputedDiamondPoint+distance);
		diamondStep(iComputedDiamondPoint-distance, jComputedDiamondPoint-distance, iComputedDiamondPoint, jComputedDiamondPoint);	
	}
	
	private void diamondStep(int iTopLeftOfSquare, int jTopLeftOfSquare, int iBottomRightOfSquare, int jBottomRightOfSquare){
		
		// finde restliche Punkte des gegebenen Quadrats
		int iBottomLeftOfSquare = iTopLeftOfSquare;
		int jBottomLeftOfSquare = jBottomRightOfSquare;
		
		int  iTopRightOfSquare = iBottomRightOfSquare;
		int jTopRightOfSquare = jTopLeftOfSquare;
		
		// finde Mittelpunkt
		int iMiddle = (iTopLeftOfSquare+iBottomRightOfSquare)/2;
		int jMiddle = (jTopLeftOfSquare+jBottomRightOfSquare)/2;
		
		int distance = Math.abs(iMiddle - iTopLeftOfSquare);
		
		if(distance < 1){
			return;
		}
		
		setDiamondHeight(iMiddle, jMiddle, distance);
		squareStep(iMiddle, jMiddle, distance);
	}
	
	private void setDiamondHeight(int iDiamond, int jDiamond, int distance) {
		
		float heightDiamond = (heights[iDiamond-distance][jDiamond-distance]+
			heights[iDiamond-distance][jDiamond+distance]+
			heights[iDiamond+distance][jDiamond-distance]+
			heights[iDiamond+distance][jDiamond+distance])/4;
			heights[iDiamond][jDiamond]=heightDiamond;
	}
	
	private void setSquareHeight(int iSquare, int jSquare, int distance){
		
		int dividers = 0;
		float sum = 0;
		
		// finde Nachbar(en)
				
		if (iSquare>0){
			// finde linken Nachbar
			float heightLinkerNachbar = heights[iSquare-distance][jSquare];
			dividers++;
			sum += heightLinkerNachbar;
		}
		
		if (iSquare < this.size-1){
			// finde rechten Nachbar
			float heightRechterNachbar = heights[iSquare+distance][jSquare];
			dividers++;
			sum += heightRechterNachbar;
		}
		
		if (jSquare > 0){
			// finde oberen Nachba
			float heightObererNachbar = heights[iSquare][jSquare-distance];
			dividers ++;
			sum += heightObererNachbar;
		}
		
		if (jSquare < this.size-1){
			// finde unteren Nachbar
			float heightUntererNachbar = heights[iSquare][jSquare+distance];
			dividers++;
			sum+=  heightUntererNachbar;
		}
		
		// berechne Durchschnitt...
		float avg = sum/dividers;
		
		//...und setze Höhenwert
		heights[iSquare][jSquare]=avg;
	}

	private void addVerticesAndTriangles(){
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> ind = new ArrayList<Integer>();
		
		for(int i = 0; i<this.size-1; i++){
			for(int j = 0; j<this.size-1;j++){
				int indTopLeft = addVertex(v, new Point3f(i,heights[i][j],j));
				int indBottomLeft = addVertex(v, new Point3f(i,heights[i][j+1],j+1));
				int indBottomRight = addVertex(v, new Point3f(i+1,heights[i+1][j+1], j+1));
				int indTopRight = addVertex(v, new Point3f(i+1, heights[i+1][j],j));
				
				addTriangle(ind, indTopLeft, indBottomLeft, indTopRight);
				addTriangle(ind, indTopRight, indBottomRight, indBottomLeft);
			}
		}
		this.vertices = v;
		this.indices = ind;
	}
	
	private class Selectable{
		private boolean selected = false;
		private float val;
		
		public boolean getSelected(){
			return selected;
		}
		
		public float val(){
			return val;
		}
		
		public void setVal(float val){
			this.val = val;
			this.selected = true;
		}
	}
	
	protected void setColors(){
		
		ArrayList<Float> c = new ArrayList<Float>();
		
		for(int i=0;i<vertices.size()/3;i++){
			Color3f col = new Color3f(0,1,0);
			addColor(c,col);
		}
		this.colors = c;
	}
	
	private float getHeight(int i, int j){
		return heights[i][j];
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
