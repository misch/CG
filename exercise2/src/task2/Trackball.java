package task2;

import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.GLRenderPanel;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

public class Trackball {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	
	/**
	* An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to
	* provide a call-back function for initialization.
	*/
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 *
		 * @param r the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
		}		
	}
	
	
	/**
	* The main function opens a 3D rendering window, constructs a simple 3D
	* scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	*/
	public static void main(String[] args) throws IOException{	

//		Unfortunately showing the teapot does not work on every computer.
//		Problem: there are no colors added and not every machine handles the default case similarly:
//		There are machines that assign white and such that assign black (... which will of course look like nothing at all)
//
//		shape = new Shape(ObjReader.read("teapot.obj", 2));

		shape = new House().getShape();

		Camera camera = new Camera(new Vector3f(0,0,40), new Vector3f(0,0,0), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
		sceneManager = new SimpleSceneManager(camera, frustum);
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();

		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		TrackballListener ml = new TrackballListener(renderPanel,shape);
		renderPanel.getCanvas().addMouseListener(ml);
		renderPanel.getCanvas().addMouseMotionListener(ml);
		renderPanel.getCanvas().repaint();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}
}
