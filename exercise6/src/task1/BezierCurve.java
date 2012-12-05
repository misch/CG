package task1;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import jogamp.graph.math.MathFloat;

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
		Point2f[] interpolatedPoints = new Point2f[roughness+segments+1]; 	// points on the curve
																			// + the end point of each segment (which is the start point of the next one)
																			// + the start point of the first segment
		
		interpolatedPoints[0] = controlPoints[0];																	
		float interpolationStep = 1/(roughness+1);
		int counter = 1;
		for (float i = interpolationStep; i<=1; i+=interpolationStep){
			if (counter < interpolatedPoints.length){
				interpolatedPoints[counter] = deCasteljau(i);
				counter++;
			}
			else{
				System.out.println("... This is not good! N-O-T  G-O-O-D-!-!-!");
			}
		}
		return interpolatedPoints;
	}

	private Point2f deCasteljau(float interpolationStep) {
		float[] bernstein = cubicBernstein(interpolationStep);
		
		Point2f interpolated = new Point2f();
		
		for (int j = 0; j < this.segments; j++){			
			for (int i = 0; i < 4; i++){
				Point2f controlPoint = new Point2f(controlPoints[j*3+i]);
				controlPoint.scale(bernstein[i]);
				interpolated.add(controlPoint);
			}
		}
		return interpolated;
	}
	
	private float[] cubicBernstein(float t){
		float[] cubicBernsteinPolynomials = new float[4];
		cubicBernsteinPolynomials[0] = pow(t,3) + 3*pow(t,2) - 3*t + 1;
		cubicBernsteinPolynomials[1] = 3*pow(t,3)-6*pow(t,2)+3*t;
		cubicBernsteinPolynomials[2] = -3*pow(t,3) + 3*pow(t,2);
		cubicBernsteinPolynomials[3] = pow(t,3);
		
		return cubicBernsteinPolynomials;
	}
	
	private float pow(float x, float y){
		return MathFloat.pow(x,y);
	}
}
