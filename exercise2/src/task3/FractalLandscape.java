package task3;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import ex1.AbstractSimpleShape;

public class FractalLandscape extends AbstractSimpleShape{
	private int size, cycles;
	private float heights[][];
	
	public FractalLandscape(int size, float[] cornerHeights, float roughness){
		
		this.cycles = size;
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[this.size][this.size];
			
		heights[0][0] = cornerHeights[0];
		heights[this.size-1][0]=cornerHeights[2];
		heights[0][this.size-1]=cornerHeights[1];
		heights[this.size-1][this.size-1]=cornerHeights[3];

		int width = this.size-1;
		float randomScale = this.size*roughness;
		for (int c=0; c<=cycles; c++){
			randomScale /=2;
			diamondStep(width, randomScale);
			squareStep(width, randomScale);
			width /=2;
		}
		
		for (int i = 0; i<this.size; i++){
			for (int j = 0; j<this.size; j++){
				if (heights[i][j] <= 0)
					heights[i][j] = 0;
			}
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
		
		for(int i = 0; i<this.size-1; i++){
			for(int j = 0; j<this.size-1;j++){
				int indTopLeft = addVertex(new Point3f(i,heights[i][j],j));
				addColor(getCol(heights[i][j]));
				addNormal(computeNormal(i,j));
				
				int indBottomLeft = addVertex(new Point3f(i,heights[i][j+1],j+1));
				addColor(getCol(heights[i][j+1]));
				addNormal(computeNormal(i,j+1));
				
				int indBottomRight = addVertex(new Point3f(i+1,heights[i+1][j+1], j+1));
				addColor(getCol(heights[i+1][j+1]));
				addNormal(computeNormal(i+1,j+1));
				
				int indTopRight = addVertex(new Point3f(i+1, heights[i+1][j],j));
				addColor(getCol(heights[i+1][j]));
				addNormal(computeNormal(i+1,j));
				
				addTriangle(indTopLeft, indBottomLeft, indTopRight);
				addTriangle(indTopRight, indBottomRight, indBottomLeft);
			}
		}
	}

	private Color3f getCol(float f) {
		Color3f col;
		col = new Color3f(1,1,1);
		
		if (f<=0){
			float blueVar = (float)(Math.random()*0.2);
			return col = new Color3f(0,0,1-blueVar);
		}
		
		if(f <= 50 + Math.random()*20)
			return col = new Color3f(0,0.8f-(float)Math.random()*0.3f,0);
		
		if (f> 50 - Math.random()*20){
			float greyVar = (float)(Math.random()*0.5);
			return col = new Color3f(1-greyVar,1-greyVar,1-greyVar);
		}
		
		
				
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
}
