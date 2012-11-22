package task1;



import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.Cylinder;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.Material;
import jrtr.ObjReader;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.VertexData;
import sceneGraph.GraphSceneManager;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task1.RoboRenderPanel;
import task3.LandscapeListener;

public class ShowRobot {
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
	
		TransformGroup 	world = new TransformGroup(),
						body = new TransformGroup(),
						leftShoulder = new TransformGroup(),
						rightShoulder = new TransformGroup(),
						leftUpperArm = new TransformGroup(),
						rightUpperArm = new TransformGroup(),
						leftLowerArm = new TransformGroup(),
						rightLowerArm = new TransformGroup(),
						leftElbow = new TransformGroup(),
						rightElbow = new TransformGroup(),
						leftHip = new TransformGroup(),
						rightHip = new TransformGroup(),
						leftUpperLeg = new TransformGroup(),
						rightUpperLeg = new TransformGroup(),
						leftKnee = new TransformGroup(),
						rightKnee = new TransformGroup(),
						leftLowerLeg = new TransformGroup(),
						rightLowerLeg = new TransformGroup(),
						head = new TransformGroup();
		
		float	armDiam = 0.15f,
				bodyDiam = 0.41f,
				bodyHeight = 2,
				upperArmLength = 1.2f,
				lowerArmLength = 1,
				upperLegLength = 1.2f,
				lowerLegLength = 1.2f,
				legDiam = 0.15f,
				headHeight = 0.5f,
				headDiam = 0.25f;
		
		ShapeNode	corpus = new ShapeNode(new Cylinder(50,bodyHeight,bodyDiam).getShape()),
					upperArm = new ShapeNode(new Cylinder(50, upperArmLength, armDiam).getShape()),
					lowerArm = new ShapeNode(new Cylinder(50, lowerArmLength, armDiam).getShape()),
					upperLeg = new ShapeNode(new Cylinder(50, upperLegLength, legDiam).getShape()),
					lowerLeg = new ShapeNode(new Cylinder(50, lowerLegLength, legDiam).getShape()),
					headShape = new ShapeNode(new Cylinder(50,headHeight, headDiam).getShape());
		
		body.setTranslation(new Vector3f(3,0,0));
		
		leftShoulder.setTranslation(new Vector3f(-(armDiam+bodyDiam),1.8f,0));
		rightShoulder.setTranslation(new Vector3f(armDiam+bodyDiam,1.8f,0));
		
		leftUpperArm.setTranslation(new Vector3f(0,-upperArmLength,0));
		
		Matrix4f rot = new Matrix4f();
		rot.rotX(1);
		leftElbow.setTransformation(rot);
		
		leftElbow.setTranslation(new Vector3f(0,-0.2f,0));
		leftLowerArm.setTranslation(new Vector3f(0,-lowerArmLength,0));
		
		rightUpperArm.setTranslation(new Vector3f(0,-upperArmLength,0));
		
		rightElbow.setTranslation(new Vector3f(0,-0.2f,0));
		rightLowerArm.setTranslation(new Vector3f(0,-lowerArmLength,0));		
		
		leftHip.setTranslation(new Vector3f(-(bodyDiam/2),0,0));
		rightHip.setTranslation(new Vector3f(bodyDiam/2,0,0));
		
		leftUpperLeg.setTranslation(new Vector3f(0,-upperLegLength,0));
		rightUpperLeg.setTranslation(new Vector3f(0,-upperLegLength,0));
		
		leftKnee.setTranslation(new Vector3f(0,-0.2f,0));
		rightKnee.setTranslation(new Vector3f(0,-0.2f,0));
		
		leftLowerLeg.setTranslation(new Vector3f(0,-lowerLegLength,0));
		rightLowerLeg.setTranslation(new Vector3f(0,-lowerLegLength,0));
		
		head.setTranslation(new Vector3f(0,bodyHeight,0));

		
		// build graph
		
		world.addChild(body);
		
		body.addChild(leftShoulder,rightShoulder,corpus, leftHip, rightHip, head);
		
		leftShoulder.addChild(leftUpperArm);
		rightShoulder.addChild(rightUpperArm);
		
		leftUpperArm.addChild(leftElbow,upperArm);
		leftElbow.addChild(leftLowerArm);
		leftLowerArm.addChild(lowerArm);
		
		rightUpperArm.addChild(rightElbow,upperArm);
		rightElbow.addChild(rightLowerArm);
		rightLowerArm.addChild(lowerArm);
		
		rightHip.addChild(rightUpperLeg);
		leftHip.addChild(leftUpperLeg);
		
		rightUpperLeg.addChild(rightKnee, upperLeg);
		leftUpperLeg.addChild(leftKnee, upperLeg);
		
		rightKnee.addChild(rightLowerLeg);
		leftKnee.addChild(leftLowerLeg);
		
		rightLowerLeg.addChild(lowerLeg);
		leftLowerLeg.addChild(lowerLeg);
		
		head.addChild(headShape);
		sceneManager = new GraphSceneManager(world,camera,frustum);
		
//		String tex2File = "../jrtr/textures/wood.jpg";
//		String vertShaderPath2 = "../jrtr/shaders/disco.vert";
//		String fragShaderPath2 = "../jrtr/shaders/disco.frag";
//		shape.setMaterial(new Material(tex2File,1));
//		shape.getMaterial().setFragmentShaderPath(fragShaderPath2);
//		shape.getMaterial().setVertexShaderPath(vertShaderPath2);
//		shape.getMaterial().setSpecularReflection(20);
//		shape.getMaterial().setPhongExponent(100);
		
//		translateShape(shape, new Vector3f(0,0,0));

//		sceneManager.addShape(shape);
		
		addLights();
		

		// Make a render panel. The init function of the renderPanel
		// will be called back for initialization.
		TransformGroup[] transformGroups = {leftShoulder,leftElbow,body,world};
		Shape[] shapes= {};
//		renderPanel = new RoboRenderPanel(sceneManager,shapes);
		renderPanel = new RoboRenderPanel(sceneManager,shapes,transformGroups);
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("robo");
		jframe.setSize(700, 700);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		
		LandscapeListener listener = new LandscapeListener(camera, renderPanel);
		
		renderPanel.getCanvas().addMouseListener(listener);
		renderPanel.getCanvas().addMouseMotionListener(listener);
		renderPanel.getCanvas().addKeyListener(listener);
		jframe.addKeyListener(listener);
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
	
	private static void addLights() {
//		sceneManager.addLightSource(new PointLight(80, new Point3f(10,3,0), new Color3f(1,1,0)));
//		sceneManager.addLightSource(new PointLight(80,new Point3f(0,5,10), new Color3f(0,0,1)));
	}

//	private static void translateShape(Shape shape, Vector3f vec) {
//		Matrix4f t = shape.getTransformation();		
//		Matrix4f translation = new Matrix4f();
//		translation.setIdentity();
//		translation.setTranslation(vec);
//	
//		t.mul(translation);
//	}
}
