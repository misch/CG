package task1;

import javax.vecmath.Point2f;

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
		this.roughness = numberOfEvaluatedPointsOnCurve;
	}
}
