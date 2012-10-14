package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Shape;

import ex1.AbstractSimpleShape;

//public class FractalLandscape extends AbstractSimpleShape{
public class FractalLandscape extends AbstractSimpleShape{
	private int size;
	private Selectable heights[][];
	private float granularity;
	private int iTopLeft, iTopRight, iBottomLeft, iBottomRight;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new Selectable[this.size][this.size];
		this.granularity = granularity;
			
		heights[0][0] = new Selectable();
		heights[0][0].setVal(0);
		
		heights[this.size-1][0] = new Selectable();
		heights[this.size-1][0].setVal(0);
		
		heights[0][this.size-1] = new Selectable();
		heights[0][this.size-1].setVal(0);
		
		heights[this.size-1][this.size-1] = new Selectable();
		heights[this.size-1][this.size-1].setVal(cornerHeight);

//		squareStep(0,0,this.size-1, this.size-1, this.size-1);
//		diamondStep(0,0,this.size-1,this.size-1);
		
		diamondStep(0,0,this.size-1,this.size-1);
		
		addVerticesAndTriangles();
		setColors();
		
	}

//	private void squareStep(int iTopLeft, int jTopLeft, int iBottomRight, int jBottomRight, int länge){
//	private void squareStep(int i1, int j1, int i2, int j2){ // {i1,j1} und {i2,j2} sind zwei sich gegenüberliegende Ecken eines Diamonds
	private void squareStep(int iComputedDiamondPoint,int jComputedDiamondPoint, int distance){
		
//		int iSquare = (i2+i1)/2;
//		int jSquare = (j1+j2)/2;
		
		setSquareHeight(iComputedDiamondPoint-distance, jComputedDiamondPoint, distance);
		setSquareHeight(iComputedDiamondPoint, jComputedDiamondPoint-distance, distance);
		setSquareHeight(iComputedDiamondPoint+distance, jComputedDiamondPoint, distance);
		setSquareHeight(iComputedDiamondPoint, jComputedDiamondPoint+distance, distance);
		
		
		diamondStep(iComputedDiamondPoint, jComputedDiamondPoint-distance, iComputedDiamondPoint+distance, jComputedDiamondPoint);
		diamondStep(iComputedDiamondPoint, jComputedDiamondPoint, iComputedDiamondPoint+distance, jComputedDiamondPoint+distance);
		diamondStep(iComputedDiamondPoint-distance, jComputedDiamondPoint, iComputedDiamondPoint, jComputedDiamondPoint+distance);
		diamondStep(iComputedDiamondPoint-distance, jComputedDiamondPoint-distance, iComputedDiamondPoint, jComputedDiamondPoint);
		
//		setSquareHeight()
//		setSquareHeight(iTopOfDiamond,jTopOfDiamond+länge,iBottomOfDiamond+länge,jBottomOfDiamond);
//		setSquareHeight(iTopOfDiamond-länge,jTopOfDiamond+länge,iBottomOfDiamond, jBottomOfDiamond);
//		setSquareHeight(iTopOfDiamond-länge,jTopOfDiamond,iTopOfDiamond,jTopOfDiamond+länge);
		
//		diamondStep(iTopLeft, jTopLeft, iBottomRight, jBottomRight);
		
		// Abbruch-Bedingung:
//		if(länge<=1){
//			return;
//		}
		
//		int iBottomLeft = iTopLeft;
//		int jBottomLeft = jTopLeft+länge;
		
//		int iBottomRight = iTopLeft+länge;
//		int jBottomRight = jTopLeft+länge;
		
//		int iTopRight = iTopLeft+länge;
//		int jTopRight = jTopLeft;
		
//		float heightMiddle = (heights[iBottomLeft][jBottomLeft].val()+
//								heights[iBottomRight][jBottomRight].val()+
//								heights[iTopRight][jTopRight].val()+
//								heights[iTopLeft][jTopLeft].val())/4;
		
//		int halbeLänge = länge/2;
//		
//		int iMiddle = iTopLeft + halbeLänge;
//		int jMiddle = iTopLeft + halbeLänge;
		
//		heights[iMiddle][jMiddle] = new Selectable();
//		heights[iMiddle][jMiddle].setVal(heightMiddle);		
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
		
//		squareStep(iTopLeftOfSquare, jTopLeftOfSquare, iBottomLeftOfSquare, jBottomLeftOfSquare);
//		squareStep(iTopLeftOfSquare, jTopLeftOfSquare, iTopRightOfSquare, jTopRightOfSquare);
//		squareStep(iTopRightOfSquare, jTopRightOfSquare, iBottomRightOfSquare, jBottomRightOfSquare);
//		squareStep(iBottomLeftOfSquare, jBottomLeftOfSquare, iBottomRightOfSquare, jBottomRightOfSquare);
		
		
//		setDiamondHeight(iMiddle, jMiddle-distance, distance);
//		setDiamondHeight(iMiddle, jMiddle+distance, distance);
//		setDiamondHeight(iMiddle-distance, jMiddle, distance);
//		setDiamondHeight(iMiddle+distance, jMiddle, distance);
		
		 
//		squareStep(iTopLeftOfSquare, jTopLeftOfSquare, iMiddle, jMiddle, distance);
//		squareStep(iMiddle, jMiddle-distance,iMiddle+distance, jMiddle, distance );
//		squareStep(iMiddle, jMiddle, iBottomRightOfSquare, jBottomRightOfSquare, distance);
//		squareStep(iMiddle-distance, jMiddle, iMiddle, jMiddle+distance, distance);
		
//		diamondStep(i1,j1,iMiddle,jMiddle);
//		diamondStep(iMiddle,jMiddle-distance,iMiddle+distance,jMiddle);
//		diamondStep(iMiddle,jMiddle,i2,j2);
//		diamondStep(iMiddle-distance,jMiddle,iMiddle,jMiddle+distance);
	}
	
	private void setDiamondHeight(int iDiamond, int jDiamond, int distance) {
		
		float heightDiamond = (heights[iDiamond-distance][jDiamond-distance].val()+
				heights[iDiamond-distance][jDiamond+distance].val()+
				heights[iDiamond+distance][jDiamond-distance].val()+
				heights[iDiamond+distance][jDiamond+distance].val())/4;
		
		heights[iDiamond][jDiamond] = new Selectable();
		heights[iDiamond][jDiamond].setVal(heightDiamond);
		
	
	}
	
	private void setSquareHeight(int iSquare, int jSquare, int distance){
		
		int dividers = 0;
		float sum = 0;
		
		// finde Nachbar(en)
				
		if (iSquare>0){
			// finde linken Nachbar
			float heightLinkerNachbar = heights[iSquare-distance][jSquare].val();
			dividers++;
			sum += heightLinkerNachbar;
		}
		
		if (iSquare < this.size-1){
			// finde rechten Nachbar
			float heightRechterNachbar = heights[iSquare+distance][jSquare].val();
			dividers++;
			sum += heightRechterNachbar;
		}
		
		if (jSquare > 0){
			// finde oberen Nachba
			float heightObererNachbar = heights[iSquare][jSquare-distance].val();
			dividers ++;
			sum += heightObererNachbar;
		}
		
		if (jSquare < this.size-1){
			// finde unteren Nachbar
			float heightUntererNachbar = heights[iSquare][jSquare+distance].val();
			dividers++;
			sum+=  heightUntererNachbar;
		}
		
		// berechne Durchschnitt...
		float avg = sum/dividers;
		
		//...und setze Höhenwert
		heights[iSquare][jSquare] = new Selectable();
		heights[iSquare][jSquare].setVal(avg);
	}

	private void addVerticesAndTriangles(){
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> ind = new ArrayList<Integer>();
		
		for(int i = 0; i<this.size-1; i++){
			for(int j = 0; j<this.size-1;j++){
				int indTopLeft = addVertex(v, new Point3f(i,heights[i][j].val(),j));
				int indBottomLeft = addVertex(v, new Point3f(i,heights[i][j+1].val(), j+1));
				int indBottomRight = addVertex(v, new Point3f(i+1,heights[i+1][j+1].val(), j+1));
				int indTopRight = addVertex(v, new Point3f(i+1, heights[i+1][j].val(),j));
				
				addTriangle(ind, indTopLeft, indBottomLeft, indBottomRight);
				addTriangle(ind, indBottomLeft, indBottomRight, indTopRight);
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
		return heights[i][j].val();
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
