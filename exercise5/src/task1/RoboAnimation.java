package task1;

import java.util.TimerTask;

import javax.vecmath.Matrix4f;

import sceneGraph.TransformGroup;

import jrtr.RenderPanel;
import jrtr.Shape;

/**
 * A timer task that generates an animation. This task triggers
 * the redrawing of the 3D scene every time it is executed.
 */
public class RoboAnimation extends TimerTask
{
	private float angle;
//	private Shape[] shapes;
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	
//	public RoboAnimation(float angle, Shape[] shapes, RenderPanel renderPanel){
	public RoboAnimation(float angle, TransformGroup[] transformGroups, RenderPanel renderPanel){
		this.angle = 0.01f;
//		this.shapes = shapes;
		this.transformGroups = transformGroups;
		this.renderPanel = renderPanel;
	}
	public void run()
	{
		Matrix4f t = transformGroups[0].getTransformationMatrix();
		Matrix4f rot = new Matrix4f();
		rot.rotX(angle);
		t.mul(rot);
		
		Matrix4f f = transformGroups[1].getTransformationMatrix();
		f.mul(rot);
//		// Update transformation
//		for (int i = 0; i<shapes.length; i++){
//			Matrix4f t = shapes[i].getTransformation();
//			Matrix4f rotX = new Matrix4f();
//			rotX.rotX(angle);
//			Matrix4f rotY = new Matrix4f();
//			rotY.rotY(angle);
//			t.mul(rotX);
//			t.mul(rotY);
//			shapes[i].setTransformation(t);
//		}
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
