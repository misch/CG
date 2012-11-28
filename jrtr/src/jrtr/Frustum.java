package jrtr;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;

/**
 * Stores the specification of a viewing frustum, or a viewing
 * volume. The viewing frustum is represented by a 4x4 projection
 * matrix. You will extend this class to construct the projection 
 * matrix from intuitive parameters.
 * <p>
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a frustum.
 */
public class Frustum {

	private Matrix4f projectionMatrix;
	private float nearPlane, farPlane, aspectRatio, verticalFieldOfView;
	private Vector3f[] normals = new Vector3f[6];
	private Point3f[] pointsOnPlane = new Point3f[6];
	
	/**
	 * Construct a default viewing frustum. The frustum is given by a 
	 * default 4x4 projection matrix.
	 */
	public Frustum()
	{
		projectionMatrix = new Matrix4f();
		float f[] = {2.f, 0.f, 0.f, 0.f, 
					 0.f, 2.f, 0.f, 0.f,
				     0.f, 0.f, -1.02f, -2.02f,
				     0.f, 0.f, -1.f, 0.f};
		projectionMatrix.set(f);
	}
	
	public Frustum(float nearPlane, float farPlane, float aspectRatio, float verticalFieldOfView){
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.aspectRatio = aspectRatio;
		this.verticalFieldOfView = verticalFieldOfView;
		
		computeProjMatrix();
	}
	
	private void computeNormals(){
		Vector3f[] normals = new Vector3f[6];
		
		normals[0] = new Vector3f(0,0,1);
	
		normals[1] = new Vector3f(0,1,0); // rotiere um -verticalFieldOfView/2 um die x-achse
		Matrix4f rot = new Matrix4f();
		rot.rotX(verticalFieldOfView/2);
		rot.transform(normals[1]);
		
		normals[2] = new Vector3f(0,-1,0); // rotiere um -verticalFieldOfView/2 um die x-achse 
		rot.rotX(-verticalFieldOfView/2);
		rot.transform(normals[2]);
		
		float horizontalFieldOfView = computeHFieldOfView(verticalFieldOfView, nearPlane, aspectRatio);
		
		normals[3] = new Vector3f(1,0,0); // rotiere um hFieldOfView/2 um die y-achse
		rot.rotY(horizontalFieldOfView/2);
		rot.transform(normals[3]);
		
		normals[4] = new Vector3f(-1,0,0); // rotiere um -hFieldOfView/2 um die y-achse
		rot.rotY(-horizontalFieldOfView/2);
		rot.transform(normals[4]);
		
		normals[5] = new Vector3f(0,0,-1);

		this.normals = normals;
	}
	
	private void pointsOnPlane(){
		Point3f[] pointsOnPlane = new Point3f[6];
		
		pointsOnPlane[0] = new Point3f(0,0,-nearPlane);
		pointsOnPlane[1] = new Point3f(0,0,-farPlane);
		pointsOnPlane[2] = new Point3f(0,0,0);
		pointsOnPlane[3] = new Point3f(0,0,0);
		pointsOnPlane[4] = new Point3f(0,0,0);
		pointsOnPlane[5] = new Point3f(0,0,0);
		
		this.pointsOnPlane = pointsOnPlane;
	}
	
	
	private float computeHFieldOfView(float verticalFieldOfView,
			float nearPlane, float aspectRatio) {
		
		float nearHalfHeight = (float)Math.tan(verticalFieldOfView/2) * nearPlane;
		float nearHalfWidth = aspectRatio * nearHalfHeight;
		
		float horizontalHalfFieldOfView = (float)Math.atan(nearHalfWidth/nearPlane);
		
		return horizontalHalfFieldOfView*2;
	}

	private void computeProjMatrix() {
		
		Matrix4f newProjMatrix = new Matrix4f();
		newProjMatrix.setZero();
		
		newProjMatrix.setM00((float)(1/(aspectRatio*Math.tan(verticalFieldOfView/2))));
		newProjMatrix.setM11((float)(1/Math.tan(verticalFieldOfView/2)));
		newProjMatrix.setM22((nearPlane + farPlane)/(nearPlane-farPlane));
		newProjMatrix.setM23(2*nearPlane*farPlane/(nearPlane-farPlane));
		newProjMatrix.setM32(-1);
		
		this.projectionMatrix = new Matrix4f(newProjMatrix);
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		computeProjMatrix();
	}

	public void setVerticalFieldOfView(float verticalFieldOfView) {
		this.verticalFieldOfView = verticalFieldOfView;
		computeProjMatrix();
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		computeProjMatrix();
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		computeProjMatrix();
	}

	public float getVerticalFieldOfView() {
		return verticalFieldOfView;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public float getFarPlane() {
		return farPlane;
	}

	/**
	 * Return the 4x4 projection matrix, which is used for example by 
	 * the renderer.
	 * 
	 * @return the 4x4 projection matrix
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
	
	public boolean isCompletelyOutside(BoundingSphere boundingSphere){
		
		// TODO: "Unreachable Code???" Maybe it's to late in the night... o.O
		
		for(int i = 0; 0 < 6; i++){
			float d = distanceFromPlane(normals[i], pointsOnPlane[i], boundingSphere.getCenter());
			
			if (d >= boundingSphere.getRadius()){
				return true;
			}
		}
		return false;
	}
	
	private float distanceFromPlane(Vector3f normal, Point3f pointOnPlane, Point3f centerOfSphere){
		float distance = 0;
		
		Vector3f x = new Vector3f(centerOfSphere);
		Vector3f p = new Vector3f(pointOnPlane);
		Vector3f n = new Vector3f(normal);
		
		x.sub(p);
		distance = x.dot(n);
		return distance;	
	}
}
