package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Shape;

import ex1.AbstractSimpleShape;

/* Konzept:
 * 1. Berechne die Array-Grösse (2^n + 1, n Natürliche Zahl) und deklariere einen entsprechenden 2D-Array.
 * 2. Das 2D-Array soll mit null initialisiert werden (oder sonst einem Wert, der als "to ignore" betrachtet werden könnte).
 * 3. Die Ecken sollen mit einem Wert gefüllt werden (Random-Wert oder auch für den Anfang derselbe Wert für alle Ecken)
 * 4. Aus den bisher berechneten Punkten sollen nun 2 Dinge berechnet werden:
 * 		- Die Indizes der Punkte, die neu berechnet werden müssen (also Mittelpunkte)
 * 		- Der Wert der Punkte, die neu berechnet werden müssen
 * 		
 * 5. Schritt 4. so oft wiederholen, bis alle nötigen Punkte berechnet sind, d.h. 2^n mal.
 * 6. Jeden Punkt zu den Vertices hinzufügen mit Koordinaten x=i, y=Array[i][j], z=j
 * 	  gleichzeitig eine von z abhängige Farbe zuordnen.
 * 7. Dreiecke bilden
 * 8. VertexData heraufbeschwören
 */

//public class FractalLandscape extends AbstractSimpleShape{
public class FractalLandscape extends AbstractSimpleShape{
	private int size;
	private Selectable heights[][];
	private float granularity;
//	private ArrayList<Float> v = new ArrayList<Float>();
//	private ArrayList<Integer> ind = new ArrayList<Integer>();
	private int iTopLeft, iTopRight, iBottomLeft, iBottomRight;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
//		ArrayList<Float> v = new ArrayList<Float>();
//		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new Selectable[this.size][this.size];
		this.granularity = granularity;
			
		heights[0][0] = new Selectable();
		heights[0][0].setVal(0);
//		iTopLeft = addVertex(v, new Point3f(0,heights[0][0].val(),0)); // TODO: a global "addVertices"-Method which might accept some param to scale the distance between two points
		
		heights[this.size-1][0] = new Selectable();
		heights[this.size-1][0].setVal(0);
//		iTopRight = addVertex(v,new Point3f(this.size-1,heights[this.size-1][0].val(),0));
		
		heights[0][this.size-1] = new Selectable();
		heights[0][this.size-1].setVal(0);
//		iBottomLeft = addVertex(v,new Point3f(0,heights[0][this.size-1].val(),this.size-1));
		
		heights[this.size-1][this.size-1] = new Selectable();
		heights[this.size-1][this.size-1].setVal(cornerHeight);
//		iBottomRight = addVertex(v,new Point3f(this.size-1,heights[this.size-1][this.size-1].val(),this.size-1));
		
//		addTriangle(indices, iTopLeft,iBottomLeft,iTopRight);
//		addTriangle(indices, iBottomLeft,iTopRight,iBottomRight);
		squareStep(this.size);
		
//		this.vertices = v;
//		this.indices = ind;
		addVerticesAndTriangles();
		setColors();
		
	}
	
//	private void diamondSquareComputation() {
//				
//		for (int i = 0; i<this.size; i++){
//			for(int j = 0; j<this.size; j++){
//				
//			}
//		}
//		
//	}
	
//	private void squareStep(int width){
	private void squareStep(int iTopLeft, int jTopRight, länge){
		
		// gehe [länge] nach unten, um den punkt unten links zu finden
		// gehe [länge] nach rechts, um den punkt oben rechts zu finden
		// gehe [länge] nach unten und [länge] nach rechts, um den punkt unten rechts zu finden
		//
		// 
		// berechne den Durchschnitt der Höhenwerte
		// finde den mittelpunkt
		
		int iBottomLeft = i;
		int jBottomLeft = j+länge;
		
		int iBottomRight = i+länge;
		int jBottomRight = j+länge;
		
		int iTopRight = i+länge;
		int jTopRight = j;
		
		float heightMiddle = (heights[ibottomLeft][jBottomLeft].val()+
								heights[iBottomRight][jBottomRight].val()+
								heights[iTopRight][jTopRight].val()+
								heights[iTopLeft][jTopLeft].val())/4;
		
		
//		for (int i = 0; i < this.size; i+=width)
//			for (int j = 0; j < this.size; j+=width){
//				
//				float height1 = heights[i][j].val();
//				float height2 = heights[i+width-1][j].val();
//				float height3 = heights[i][j+width-1].val();
//				float height4 = heights[i+width-1][j+width-1].val();
//				
//				float avg = (height1+height2+height3+height4)/4;
//				int halfWidth = width/2;
//				
//				heights[i+halfWidth][j+halfWidth] = new Selectable();
//				heights[i+halfWidth][j+halfWidth].setVal(avg);
//				
//				diamondStep();
//				diamondStep();
//			}
	}
	
//	private void diamondStep(int[] left, int[] right){
	private void diamondStep(zwei gegenüberliegende Punkte){
		
		// finde Mittelpunkt
		// finde Nachbaren
		// berechne Durchschnitt und setze Höhenwert
		int halfWidth = width/2;	
		for (int i = halfWidth ;i<this.size; i+=width){
			for(int j = (i+halfWidth)%width; j < this.size; j+=width){
				float height1 = (heights[i-1][j] == null) ? null : heights[i-1][j].val();
				float height2 = (heights[i+1][j] == null) ? null : heights[i+1][j].val();
				float height3 = (heights[i][j-1] == null) ? null : heights[i][j-1].val();
				float height4 = (heights[i][j+1] == null) ? null : heights[i][j+1].val();
			}
		}
	
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
