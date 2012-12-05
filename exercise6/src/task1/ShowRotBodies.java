package task1;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import sceneGraph.GraphSceneManager;
import sceneGraph.LightNode;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task2.SimpleRenderPanel;
import task3.LandscapeListener;
//import ex1.Cylinder;

public class ShowRotBodies {
		static RenderPanel renderPanel;
		static RenderContext renderContext;
		static GraphSceneManager sceneManager;
		static Shape shape;
		private static final Vector3f xAx = new Vector3f(1,0,0);
		private static final Vector3f zAx = new Vector3f(0,0,1);

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
	
//			ShapeNode	
//						corpus = new ShapeNode(new Cylinder(50,bodyHeight,bodyDiam).getShape()),
//						upperArm = new ShapeNode(new Cylinder(50,upperArmLength,armDiam).getShape()),
//						lowerArm = new ShapeNode(new Cylinder(50,lowerArmLength,armDiam).getShape()),
//						upperLeg = new ShapeNode(new Cylinder(50, upperLegLength, legDiam).getShape()),
//						lowerLeg = new ShapeNode(new Cylinder(50, lowerLegLength, legDiam).getShape()),
//						headShape = new ShapeNode(new Cylinder(50, headHeight, headDiam).getShape()),
//						floorShape = new ShapeNode(new Cylinder(50,0.2f,50).getShape());
					
			LightNode handLight = new LightNode(new PointLight(20,new Point3f(0,0,0), new Color3f(1,1,1)));
			handLight.setTranslation(new Vector3f(0,-0.3f,0));
			
		

			// build graph	
			
			
			Shape[] shapes = {};
			renderPanel = new SimpleRenderPanel(sceneManager,shapes);
			setupMainWindow(camera);
		}
		
		private static void setupMainWindow(Camera camera) {
			JFrame jframe = new JFrame("robo");
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
