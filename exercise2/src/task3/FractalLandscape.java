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
	private ArrayList<Float> v;
	private ArrayList<Integer> i;
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornerHeight
	 */
	public FractalLandscape(int size, float cornerHeight, float granularity){
		
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new Selectable[this.size][this.size];
		
		this.granularity = granularity;
			
		heights[0][0].setVal(cornerHeight);
		heights[size-1][0].setVal(cornerHeight);
		heights[0][size-1].setVal(cornerHeight);
		heights[size-1][size-1].setVal(cornerHeight);
		
		diamondSquareComputation();
	}
	
	private void diamondSquareComputation() {
				
		for (int i = 0; i<this.size; i++){
			for(int j = 0; j<this.size; j++){
				
			}
		}
		
	}
	
	private void squareStep(int width){
		for (int i = 0; i < this.size; i+=width)	// TODO: adjust boundaries
			for (int j = 0; j < this.size; j+=width){
				float height1 = heights[i][j].val();
				float height2 = heights[i+width][j].val();
				float height3 = heights[i][j+width].val();
				float height4 = heights[i+width][j+width].val();
			}
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
	
	private float getHeight(int i, int j){
		return heights[i][j].val();
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
