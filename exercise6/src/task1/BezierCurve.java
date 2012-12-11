package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point4f;

import jogamp.graph.math.MathFloat;

public class BezierCurve {
	private Point2f[] controlPoints;
	private int segments;
	private int roughness;
	private Point4f[] interpolatedPoints;
	private Matrix4f bernstein = new Matrix4f(-1, 3,-3, 1,
											   3,-6, 3, 0,
											  -3, 3, 0, 0,
											   1, 0, 0, 0);
	private Matrix4f[] preComputed;
	
	public BezierCurve(
	int numberOfBezierSegments, 
	Point2f[] controlPointsInXYPlane, 
	int numberOfEvaluatedPointsOnCurve)
	{
		this.segments = numberOfBezierSegments;
		this.controlPoints = controlPointsInXYPlane;
		this.roughness = numberOfEvaluatedPointsOnCurve; // number of evaluated points on the curve
														 // (start and end point not included)
		this.preComputed = preComputeC();
		this.interpolatedPoints = approximateCurve();
	}
	
	public BezierCurve(){
		this(1,defaultControlPoints(),1);
	}
	
	private static Point2f[] defaultControlPoints(){
		Point2f[] controlPoints = {new Point2f(1,0), new Point2f(1,0.25f), new Point2f(1,0.75f), new Point2f(1,1)};
		return controlPoints;
	}
	
	private Point4f[] approximateCurve(){
		Point4f[] interpolatedPoints = new Point4f[roughness * segments + segments + 1];
		
		float interpolationStep = 1f/(roughness+1);
		interpolatedPoints[0] = new Point4f(controlPoints[0].x, controlPoints[0].y,0,0);
		for (int segment = 0; segment < this.segments; segment++){
//			interpolatedPoints[segment] = new Point4f(controlPoints[segment*3].x, controlPoints[segment*3].y,0,0);
			int counter = segment+1;
			for (float i = interpolationStep; i<=1; i+=interpolationStep){
				Point4f interpolated = new Point4f(pow(i,3),pow(i,2),i,1);
				this.preComputed[segment].transform(interpolated);
				interpolatedPoints[segment+counter] = new Point4f(interpolated);
				counter ++;
			}
		}
		
		return interpolatedPoints;
	}

	private Matrix4f[] preComputeC() {
		Matrix4f[] controlPointMatrices = new Matrix4f[this.segments];
		for (int j = 0; j < this.segments; j++){			
			Matrix4f controlMatrix = new Matrix4f();
			for (int i = 0; i<4; i++){
				controlMatrix.setColumn(i, controlPoints[j*3+i].x, controlPoints[j*3+i].y, 0,0);
			}
			
			controlMatrix.mul(this.bernstein);
			controlPointMatrices[j] = controlMatrix;
		}
		return controlPointMatrices;
	}
	
	private float pow(float x, float y){
		return MathFloat.pow(x,y);
	}

	public Point4f[] getInterpolatedPoints() {
		return interpolatedPoints;
	}
}
