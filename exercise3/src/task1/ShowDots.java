package task1;

import jrtr.*;

import javax.swing.*;
import java.io.IOException;
import javax.vecmath.*;
import ex1.Cylinder;

import java.util.Timer;
import java.util.TimerTask;

public class ShowDots {	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float angle;

	/**
	 * An extension of {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. 
	 */ 
	public final static class MyRenderPanel extends SWRenderPanel
	{
		public MyRenderPanel(boolean drawTriangles){
			super(drawTriangles);
		}
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
	
			// Register a timer task
		    Timer timer = new Timer();
		    angle = 0.01f;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}
	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Update transformation
    		Matrix4f t = shape.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(angle);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(angle);
    		t.mul(rotX);
    		t.mul(rotY);
    		shape.setTransformation(t);
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}
	
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		
		shape = new Cylinder(50,2,1).getShape();
//		shape = new Torus(80,2).getShape();
//		shape = new Cube().getShape();
		
		// Make a scene manager and add the object
		sceneManager = new SimpleSceneManager();
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new MyRenderPanel(false); // MyRenderPanel(boolean drawTriangles)
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas()); // put the canvas into a JFrame window
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}