package task1;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

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
		
		Point4f[] pointsToRotate = this.curve.getInterpolatedPoints();
		Vector4f[] tangents = this.curve.getTangents();
		for (float angle = 0; angle<2*PI; angle+=this.step){
			Color3f col = new Color3f((float)Math.random(),(float)Math.random(),(float)Math.random());
			for(int i = 0; i<pointsToRotate.length; i++){
				rot.rotY(angle);
				Point3f point = new Point3f(pointsToRotate[i].x, pointsToRotate[i].y,0);
				
				Vector3f tangent = new Vector3f(tangents[i].x,tangents[i].y,0);
				rot.transform(tangent);
				
				Vector3f normal = new Vector3f(-tangents[i].y,tangents[i].x,0);
				normal.normalize();
				
				rot.transform(point);
				rot.transform(normal);
				
				float texelX = (angle<PI)? angle/PI : (2*PI - angle)/PI; // to avoid interruptions in the texture
				addTexel(texelX, (float)i/pointsToRotate.length);
				addVertex(point);
				addColor(col);
				addNormal(normal);
				addTangent(tangent);
			}
		}
	}
	
	private void createTriangleMesh(){
		int vertN = this.vertices.size()/3;
		int heightVertN = vertN/numberOfAngleSteps; // Anzahl Vertices übereinander
		
		for (int i = 0; i<numberOfAngleSteps; i++){
			for (int j = i*heightVertN; j < (i+1)*heightVertN-1;j++){
				int botLeft = j;
				int topLeft = botLeft+1;
				int botRight = (botLeft+heightVertN)%vertN;
				int topRight = botRight+1;
				addRectangle(botLeft, topLeft, botRight, topRight);
			}			
		}
	}
		
	protected static Point2f p(double x, double y){
		Point2f point = new Point2f((float)x,(float)y);
		return point;
	}
}
