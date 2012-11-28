package task2;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.ObjReader;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import sceneGraph.GraphSceneManager;
import sceneGraph.LightNode;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task1.RoboRenderPanel;
import task3.LandscapeListener;
import ex1.Cylinder;

public class ShowObjectCulling {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static GraphSceneManager sceneManager;
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
		
		Shape teapot = new Shape(ObjReader.read("teapot_tex.obj", 0.5f));
		
		LightNode light = new LightNode(new PointLight(5,new Point3f(0,0,0), new Color3f(1,1,1)));
		light.setTranslation(new Vector3f(0,2,0));
	
		world.addChild(light);
		
		for (int i = 0; i<10; i++){
			for (int j = 0; j < 10; j++){
				ShapeNode pot = new ShapeNode(teapot);
				pot.setTranslation(new Vector3f(i*2,0,j*2));
				world.addChild(pot);
			}
		}

		sceneManager = new GraphSceneManager(world,camera,frustum);

		// Make a render panel. The init function of the renderPanel
		// will be called back for initialization.
		TransformGroup[] transformGroups = {};
		Shape[] shapes = {teapot};
		renderPanel = new RoboRenderPanel(sceneManager,shapes,transformGroups);
		
		setupMainWindow(camera);
	}
	
	private static void setupMainWindow(Camera camera) {
		JFrame jframe = new JFrame("Teapots");
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
