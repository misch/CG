package task1;

import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

import jogamp.graph.math.MathFloat;

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
		rot.setIdentity();
		rot.rotY(this.step);
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> colors = new ArrayList<Float>();
		
		Point2f[] pointsToRotate = this.curve.getInterpolatedPoints();
		
		for (float angle = 0; angle<2*PI; angle+=this.step){
			for(int i = 0; i<pointsToRotate.length; i++){
				Point3f point = new Point3f(pointsToRotate[i].x, pointsToRotate[i].y,0);
				rot.transform(point);

				addVertex(vertices, point);
				addColor(colors, new Color3f(1,1,1));
			}
		}
		
		this.vertices = vertices;
		this.colors = colors;
	}
	
	private void createTriangleMesh(){
		ArrayList<Integer> ind = new ArrayList<Integer>();
		
		addRectangle(ind, 0,1,3,4);
		addRectangle(ind, 3,4,6,7);
		addRectangle(ind, 6,7,9,10);
		addRectangle(ind, 9,10,0,1);
		
		addRectangle(ind, 1,2,4,5);
		addRectangle(ind, 4,5,7,8);
		addRectangle(ind, 7,8,10,11);
		addRectangle(ind, 10,11,1,2);
		
//		addRectangle(ind, 2,3,6,7);
//		addRectangle(ind, 6,7,10,11);
//		addRectangle(ind, 10,11,14,15);
//		addRectangle(ind, 14,15,2,3);
		
		this.indices = ind;
	}
	
	private void addRectangle(ArrayList<Integer> ind, int topRight, int botRight, int topLeft, int botLeft){
		addTriangle(ind,topLeft,botLeft,topRight);
		addTriangle(ind,topRight,botLeft,botRight);
	}
	
	@Override
	protected float x(float u, float v) {return 0;}
	protected float y(float u, float v) {return 0;}
	protected float z(float u, float v) {return 0;}
}
