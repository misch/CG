package task1;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import sceneGraph.GraphSceneManager;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task2.SimpleRenderPanel;
import task3.LandscapeListener;

public class ShowRotBodies {
		static RenderPanel renderPanel;
		static RenderContext renderContext;
		private static GraphSceneManager sceneManager;
		static Shape shape;

		/**
		 * The main function opens a 3D rendering window, constructs a simple 3D
		 * scene, and starts a timer task to generate an animation.
		 * @throws IOException 
		 */
		public static void main(String[] args) throws IOException
		{						
			// Make a scene manager and add the object
			Camera 	camera = new Camera(new Vector3f(0,0,10), new Vector3f(0,0,0), new Vector3f(0,1,0));
			Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
			TransformGroup world = new TransformGroup();
			
//			Point2f[] controlPoints = {	new Point2f(1,-1), new Point2f(1,-0.75f), 
//										new Point2f(1,-0.25f), new Point2f(1,0),
//										new Point2f(1,0.25f), new Point2f(1,0.75f),
//										new Point2f(1,1)};
			Point2f[] controlPoints = {new Point2f(1,0),
					new Point2f(1,0.25f), new Point2f(1,0.75f),
					new Point2f(1,1)};

			
//			RotationalBody rotBody = new RotationalBody(new BezierCurve(2,controlPoints,1),5);
			RotationalBody rotBody = new RotationalBody(new BezierCurve(1,controlPoints,1),10);
			Shape rotShape = rotBody.getShape();
			
			ShapeNode cyl = new ShapeNode(rotShape);

			// build graph	
			world.addChild(cyl);
			
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {};
			renderPanel = new SimpleRenderPanel(sceneManager,shapes);
			setupMainWindow(camera,"Rotational Body");
		}
		
		private static void setupMainWindow(Camera camera, String name) {
			JFrame jframe = new JFrame(name);
			jframe.setSize(700, 700);
			jframe.setLocationRelativeTo(null); // center of screen
			jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window
			
			LandscapeListener listener = new LandscapeListener(camera, renderPanel);
			
			Component canvas = renderPanel.getCanvas();
			canvas.addMouseListener(listener);
			canvas.addMouseMotionListener(listener);
			canvas.addKeyListener(listener);
			   	    	    
		    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jframe.setVisible(true);	
		}
	}
