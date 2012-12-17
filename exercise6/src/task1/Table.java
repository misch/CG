package task1;

import javax.vecmath.Point2f;

public class Table extends RotationalBody {
	
	public Table(int numberOfEvaluatedPointsOnCurve, int rotationSteps){
		super(createCurve(numberOfEvaluatedPointsOnCurve),rotationSteps);
	}
	
	private static BezierCurve createCurve(int evaluate){
		BezierCurve curve = new BezierCurve(5,setControlPoints(),evaluate);
		return curve;
	}
	
	private static Point2f[] setControlPoints(){
		Point2f[] controlPoints = {
				p(1,0),p(0.6,0.5),p(0.1,0.7),
				p(0.1,1),p(0.1,3),p(0.1,4),
				p(0.1,5),p(2,5),p(2.5,5),
				p(3,5),p(3.1,5),p(3.1,5.1),p(3,5.1),
				p(2.5,5.1),p(1.5,5.1),p(0.1,5.1)};
		return controlPoints;
	}
}
