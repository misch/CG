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

	/**
	 * Construct a camera with a default camera matrix. The camera
	 * matrix corresponds to the world-to-camera transform. This default
	 * matrix places the camera at (0,0,10) in world space, facing towards
	 * the origin (0,0,0) of world space, i.e., towards the negative z-axis.
	 */
	public Camera()
	{
		cameraMatrix = new Matrix4f();
		float f[] = {1.f, 0.f, 0.f, 0.f,
					 0.f, 1.f, 0.f, 0.f,
					 0.f, 0.f, 1.f, -10.f,
					 0.f, 0.f, 0.f, 1.f};
		cameraMatrix.set(f);
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
	}

	public void setLookAtPoint(Vector3f lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
	}

	public void setUpVector(Vector3f upVector) {
		this.upVector = upVector;
	}
	
}
