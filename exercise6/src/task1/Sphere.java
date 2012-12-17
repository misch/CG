package task1;

import javax.vecmath.Point2f;

public class Sphere extends RotationalBody {
	
	public Sphere(int evalCurve, int rotationSteps, float radius, Point2f center){
		super(createCurve(evalCurve,radius,center),rotationSteps);
	}
	
	private static BezierCurve createCurve(int evaluate, float radius,Point2f center){
		BezierCurve curve = new BezierCurve(1,setControlPoints(radius,center),evaluate);
		return curve;
	}
	
	private static Point2f[] setControlPoints(float radius, Point2f center){
		Point2f[] controlPoints = {
				p(center.x,radius+center.y),
				p((8d/6)*radius+center.x,radius+center.y),
				p((8d/6)*radius+center.x,-radius+center.y),
				p(center.x,-radius+center.y)};
		return controlPoints;
	}
}
