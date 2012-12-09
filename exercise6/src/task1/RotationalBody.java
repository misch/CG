package task1;

import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import ex1.AbstractSimpleShape;

public class RotationalBody extends AbstractSimpleShape{
	private BezierCurve curve;
	private float step;
	private final float PI = (float)Math.PI;
	private int numberOfAngleSteps;
	
	public RotationalBody(BezierCurve curve, int numberOfAngleStepsInRotation)
	{
		this.curve = curve;
		this.numberOfAngleSteps = numberOfAngleStepsInRotation;
		this.step = (float)(2*Math.PI/numberOfAngleStepsInRotation);
		
		setVertices();
		createTriangleMesh();
	}

	private void setVertices(){
		Matrix4f rot = new Matrix4f();
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> colors = new ArrayList<Float>();
		
		Point4f[] pointsToRotate = this.curve.getInterpolatedPoints();
		for (float angle = 0; angle<2*PI; angle+=this.step){
			for(int i = 0; i<pointsToRotate.length; i++){
				rot.rotY(angle);
				Point3f point = new Point3f(pointsToRotate[i].x, pointsToRotate[i].y,0);
				rot.transform(point);

				addVertex(vertices, point);
				addColor(colors, new Color3f((float)Math.random(),(float)Math.random(),(float)Math.random()));
			}
		}
		this.vertices = vertices;
		this.colors = colors;
	}
	
	private void createTriangleMesh(){
		ArrayList<Integer> ind = new ArrayList<Integer>();
	
		int vertN = this.vertices.size()/3;
		int heightVertN = vertN/numberOfAngleSteps; // Anzahl Vertices übereinander
		

		for (int i = 0; i < numberOfAngleSteps;i++){
			for (int j = 0; j < heightVertN-1; j++){
				int botLeft = i*(numberOfAngleSteps-(numberOfAngleSteps-3))+j;
				int topLeft = (botLeft+1)%vertN;
				int botRight = (botLeft + heightVertN)%vertN;
				int topRight = (botRight + 1)%vertN;
				addRectangle(ind, botLeft, topLeft, botRight, topRight);
			}			
		}
		this.indices = ind;
	}
	
	private void addRectangle(ArrayList<Integer> ind, int botLeft, int topLeft, int botRight, int topRight){
		addTriangle(ind,topLeft,botLeft,topRight);
		addTriangle(ind,topRight,botLeft,botRight);
	}
}
