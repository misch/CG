package task1;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.Cylinder;

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
import task1.RoboRenderPanel;
import task3.LandscapeListener;

public class ShowRobot {
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
	
		TransformGroup 	
						world = new TransformGroup(),
						floor = new TransformGroup(),
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
						head = new TransformGroup(),
						throat = new TransformGroup();
		
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
					upperArm = new ShapeNode(new Cylinder(50,upperArmLength,armDiam).getShape()),
					lowerArm = new ShapeNode(new Cylinder(50,lowerArmLength,armDiam).getShape()),
					upperLeg = new ShapeNode(new Cylinder(50, upperLegLength, legDiam).getShape()),
					lowerLeg = new ShapeNode(new Cylinder(50, lowerLegLength, legDiam).getShape()),
					headShape = new ShapeNode(new Cylinder(50, headHeight, headDiam).getShape()),
					floorShape = new ShapeNode(new Cylinder(50,0.2f,50).getShape());
				
		LightNode handLight = new LightNode(new PointLight(5,new Point3f(0,0,0), new Color3f(1,1,1)));
		handLight.setTranslation(new Vector3f(0,-0.3f,0));
		
		body.setTranslation(new Vector3f(3,0,0));
		body.setRotation(xAx, -0.15f);
		
		floor.setTranslation(new Vector3f(0,-(upperLegLength+lowerLegLength+0.25f),0));
		
		leftShoulder.setTranslation(new Vector3f(-(armDiam+bodyDiam),1.8f,0));
		
		rightShoulder.setTranslation(new Vector3f(armDiam+bodyDiam,1.8f,0));
		rightShoulder.setRotation(xAx,2*(float)Math.PI/3);
		
		leftUpperArm.setTranslation(new Vector3f(0,-upperArmLength,0));
		
		leftElbow.setRotation(xAx, 1);
		
		leftElbow.setTranslation(new Vector3f(0,-0.2f,0));
		leftLowerArm.setTranslation(new Vector3f(0,-lowerArmLength,0));
		
		rightUpperArm.setTranslation(new Vector3f(0,-upperArmLength,0));
		
		rightElbow.setTranslation(new Vector3f(0,-0.2f,0));
				
		rightElbow.setRotation(xAx, (float)Math.PI/3);
		rightElbow.setRotation(zAx, -0.5f);
		
		rightLowerArm.setTranslation(new Vector3f(0,-lowerArmLength,0));		
		
		leftHip.setTranslation(new Vector3f(-(bodyDiam/2),0,0));
		rightHip.setTranslation(new Vector3f(bodyDiam/2,0,0));
		
		leftUpperLeg.setTranslation(new Vector3f(0,-upperLegLength,0));
		rightUpperLeg.setTranslation(new Vector3f(0,-upperLegLength,0));
		
		leftKnee.setTranslation(new Vector3f(0,-0.2f,0));
		rightKnee.setTranslation(new Vector3f(0,-0.2f,0));
		
		leftLowerLeg.setTranslation(new Vector3f(0,-lowerLegLength,0));
		rightLowerLeg.setTranslation(new Vector3f(0,-lowerLegLength,0));
		
		throat.setTranslation(new Vector3f(0,bodyHeight,0));
		
		throat.addChild(head);

		head.setRotation(xAx,0.15f);

		// build graph	
		world.addChild(body,floor);
		
		floor.addChild(floorShape);
		
		body.addChild(leftShoulder,rightShoulder,corpus, leftHip, rightHip, throat);
		
		leftShoulder.addChild(leftUpperArm);
		rightShoulder.addChild(rightUpperArm);
		
		leftUpperArm.addChild(leftElbow,upperArm);
		leftElbow.addChild(leftLowerArm);
		leftLowerArm.addChild(lowerArm);
		
		rightUpperArm.addChild(rightElbow,upperArm);
		rightElbow.addChild(rightLowerArm);
		rightLowerArm.addChild(lowerArm,handLight);
		
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

		// Make a render panel. The init function of the renderPanel
		// will be called back for initialization.
		TransformGroup[] transformGroups = {leftShoulder,leftElbow,body,world,leftHip,rightHip,leftKnee,rightKnee,floor,rightElbow,rightShoulder};
		Shape[] shapes = {	corpus.getShape(),
							upperArm.getShape(), lowerArm.getShape(),
							upperLeg.getShape(), lowerLeg.getShape(),
							headShape.getShape(),
							floorShape.getShape()};
		renderPanel = new RoboRenderPanel(sceneManager,shapes,transformGroups);
		
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
