package task1;

import java.util.TimerTask;

import javax.vecmath.Matrix4f;

import jrtr.RenderPanel;
import jrtr.Shape;

/**
 * A timer task that generates an animation. This task triggers
 * the redrawing of the 3D scene every time it is executed.
 */
public class AnimationTask extends TimerTask
{
	private float angle;
	private Shape[] shapes;
	private RenderPanel renderPanel;
	
	public AnimationTask(float angle, Shape[] shapes, RenderPanel renderPanel){
		this.angle = 0.005f;
		this.shapes = shapes;
		this.renderPanel = renderPanel;
	}
	public void run()
	{
		// Update transformation
		for (int i = 0; i<shapes.length; i++){
			Matrix4f t = shapes[i].getTransformation();
			Matrix4f rotX = new Matrix4f();
			rotX.rotX(angle);
			Matrix4f rotY = new Matrix4f();
			rotY.rotY(angle);
			t.mul(rotX);
			t.mul(rotY);
			shapes[i].setTransformation(t);
		}
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
