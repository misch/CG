package task1;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

public class BezierCurve {
	private Point2f[] controlPoints;
	private int segments;
	private int roughness;
	
	public BezierCurve(
	int numberOfBezierSegments, 
	Point2f[] controlPointsInXYPlane, 
	int numberOfEvaluatedPointsOnCurve)
	{
		this.segments = numberOfBezierSegments;
		this.controlPoints = controlPointsInXYPlane;
		this.roughness = numberOfEvaluatedPointsOnCurve; // number of evaluated points on the curve
														 // (start and end point not included)
	}
	
	private Point2f[] approximateCurve(){
		float interpolationStep = 1/(roughness+1);
		deCasteljau(interpolationStep);
		return null;
	}

	private void deCasteljau(float interpolationStep) {
		Point2f q0 = lerp(interpolationStep,controlPoints[0],controlPoints[1]);
		//...
		
		
	}

	private Point2f lerp(float interpolationStep, Point2f point, Point2f nextPoint) {
		
		Vector2f direction = new Vector2f(nextPoint);
		direction.sub(point);
		float distance = interpolationStep * direction.length();
		Point2f finalPoint = new Point2f(point);
		direction.normalize();
		direction.scale(distance);
		finalPoint.add(direction);
		return finalPoint;
	}
}
