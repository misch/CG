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
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	private float count = 0;
	
	public RoboAnimation(float angle, TransformGroup[] transformGroups, RenderPanel renderPanel){
		this.angle = 0.01f;
		this.transformGroups = transformGroups;
		this.renderPanel = renderPanel;
	}
	public void run()
	{
		Matrix4f t = transformGroups[0].getTransformationMatrix();
		Matrix4f rot = new Matrix4f();
		rot.rotX(angle);
		
		count++;
		if (count > 70){
			angle *= -1;
			count = 0;
		}
	
		t.mul(rot);
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
