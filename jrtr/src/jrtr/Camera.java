package jrtr;
import javax.vecmath.*;

/**
 * Stores the specification of a virtual camera. You will extend
 * this class to construct a 4x4 camera matrix, i.e., the world-to-
 * camera transform from intuitive parameters. 
 * 
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	private Vector3f centerOfProjection = new Vector3f();;
	private Vector3f lookAtPoint = new Vector3f();
	private Vector3f upVector = new Vector3f();
	private Vector3f xAxis,yAxis,zAxis;

	/**
	 * Construct a camera with a default camera matrix. The camera
	 * matrix corresponds to the world-to-camera transform. This default
	 * matrix places the camera at (0,0,10) in world space, facing towards
	 * the origin (0,0,0) of world space, i.e., towards the negative z-axis.
	 */
	
	public Camera(Vector3f centerOfProjection, Vector3f lookAtPoint, Vector3f upVector)
	{
		cameraMatrix = new Matrix4f();
		this.centerOfProjection = new Vector3f(centerOfProjection);
		this.lookAtPoint = new Vector3f(lookAtPoint);
		this.upVector = new Vector3f(upVector);
		
		computeCameraMatrix();	
	}
	
	public Camera(){
		this(new Vector3f(0,0,10), new Vector3f(0,0,0), new Vector3f(0,1,0));
	}
	private void computeCameraMatrix() {
		Vector3f zAxis = new Vector3f(this.centerOfProjection);
		Vector3f xAxis = new Vector3f();
		Vector3f yAxis = new Vector3f();
		
		zAxis.sub(lookAtPoint);
		zAxis.normalize();
		
		xAxis.cross(upVector,zAxis);
		xAxis.normalize();
		
		yAxis.cross(zAxis,xAxis);
		
		Vector4f translationVector = new Vector4f(centerOfProjection);
		translationVector.setW(1);
		
		this.setxAxis(xAxis);
		this.setyAxis(yAxis);
		this.setzAxis(zAxis);
		
		Matrix4f newCameraMatrix = new Matrix4f();
		newCameraMatrix.setColumn(0, new Vector4f(xAxis));
		newCameraMatrix.setColumn(1, new Vector4f(yAxis));
		newCameraMatrix.setColumn(2, new Vector4f(zAxis));
		newCameraMatrix.setColumn(3, translationVector);
		
		newCameraMatrix.invert();
		
		this.cameraMatrix.set(newCameraMatrix);
	}

	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For example, 
	 * this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix()
	{
		return cameraMatrix;
	}

	public Vector3f getCenterOfProjection() {
		return centerOfProjection;
	}

	public Vector3f getLookAtPoint() {
		return lookAtPoint;
	}

	public Vector3f getUpVector() {
		return upVector;
	}

	public void setCenterOfProjection(Vector3f centerOfProjection) {
		this.centerOfProjection = centerOfProjection;
		computeCameraMatrix();
	}

	public void setLookAtPoint(Vector3f lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
		computeCameraMatrix();
	}

	public void setUpVector(Vector3f upVector) {
		this.upVector = upVector;
		computeCameraMatrix();
	}

	public Vector3f getzAxis() {
		return zAxis;
	}

	public void setzAxis(Vector3f zAxis) {
		this.zAxis = zAxis;
	}

	public Vector3f getxAxis() {
		return xAxis;
	}

	public void setxAxis(Vector3f xAxis) {
		this.xAxis = xAxis;
	}

	public Vector3f getyAxis() {
		return yAxis;
	}

	public void setyAxis(Vector3f yAxis) {
		this.yAxis = yAxis;
	}
	
}
