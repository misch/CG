package task1;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.Material;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import sceneGraph.GraphSceneManager;
import sceneGraph.LightNode;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
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
			Camera 	camera = new Camera(new Vector3f(0,0,10), new Vector3f(10,0,0), new Vector3f(0,1,0));
			Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
			TransformGroup world = new TransformGroup();
			TransformGroup sphereGroup = new TransformGroup();
			Point2f[] floorPoints = {p(0,0),p(10,0),p(50,0),p(100,0)};
			Shape floor = new RotationalBody(new BezierCurve(1,floorPoints,1),4).getShape();
			setTexAndShade(floor,"wood.jpg","phongWithTexture");
			
			float snareHeight = 2.5f,
				baseDrumRad = 1.5f,
				rideHeight = 3.5f,
				crashHeight = 4.5f,
				fatHiHatSuppHeight = 3;
			
			Shape 
				table = new Table(5,4).getShape(),
				sphere = new Sphere(100,50,1,new Point2f(0,1)).getShape(),
				snareSupp = new RotCylinder(50,50,0.05f,snareHeight).getShape(),
				snare = new RotCylinder(50,50,1,0.6f).getShape(),
				baseDrum = new RotCylinder(50,50,baseDrumRad,2.5f).getShape(),
				rideSupp = new RotCylinder(50,50,0.05f,rideHeight).getShape(),
				ride = new Pan(50,50,1.5f,0.3f).getShape(),
				crash = new Pan(50,50,1.3f,0.3f).getShape(),
				crashSupp = new RotCylinder(50,50,0.05f,crashHeight).getShape(),
				fatHiHatSupp = new RotCylinder(50,50,0.05f,fatHiHatSuppHeight).getShape(),
				thinHiHatSupp = new RotCylinder(50,50,0.02f,fatHiHatSuppHeight/3f).getShape(),
				hiHat = new Pan(50,50,1,0.3f).getShape(),
				tom = new RotCylinder(50,50,0.5f,2.5f).getShape();
			
			PointLight light = new PointLight(30, new Point3f(0,0,0));
			
			TransformGroup
				lightPos = new TransformGroup(),
				drumPos = new TransformGroup(),
				snarePos = new TransformGroup(),
				snareSuppPos = new TransformGroup(),
				basePos = new TransformGroup(),
				ridePos = new TransformGroup(),
				rideSuppPos = new TransformGroup(),
				crashPos = new TransformGroup(),
				crashSuppPos = new TransformGroup(),
				fatHiHatSuppPos = new TransformGroup(),
				thinHiHatSuppPos = new TransformGroup(),
				lowerHiHat = new TransformGroup(),
				upperHiHat = new TransformGroup();
			
			LightNode 
				light1 = new LightNode(light),
				light2 = new LightNode(light),
				light3 = new LightNode(light);
			
			light1.setTranslation(new Vector3f(14,5,2));
			light2.setTranslation(new Vector3f(0,2,-5));
			light3.setTranslation(new Vector3f(-5,2,0));
				
					
			// set materials
			setTexAndShade(table,"wood.jpg","phongWithTexture");
			
			setTexAndShade(sphere,"fussball2.jpg","phongWithTexture");
			sphere.getMaterial().setSpecularReflection(200);
			sphere.getMaterial().setPhongExponent(1000);
		
			setTexAndShade(snare, "drums2.png","bumpShader");
			snare.getMaterial().setBumpMapPath("../jrtr/textures/drum_bump.png");
			
			setTexAndShade(snareSupp, "silber.png", "phongWithTexture");
			snareSupp.getMaterial().setSpecularReflection(200);
			snareSupp.getMaterial().setPhongExponent(1000);
		
			setTexAndShade(baseDrum, "drums2.png","bumpShader");
			baseDrum.getMaterial().setBumpMapPath("../jrtr/textures/drum_bump.png");
			
			setTexAndShade(ride, "messing.jpg","bumpShader");
			ride.getMaterial().setBumpMapPath("../jrtr/textures/bump_test_4.png");
					
			setTexAndShade(rideSupp, "silber.png", "phongWithTexture");
			rideSupp.getMaterial().setSpecularReflection(200);
			rideSupp.getMaterial().setPhongExponent(1000);
			
			setTexAndShade(crash, "messing.jpg","phongWithTexture");
		
			setTexAndShade(crashSupp, "silber.png", "phongWithTexture");
		
			setTexAndShade(fatHiHatSupp, "silber.png", "phongWithTexture");
			setTexAndShade(thinHiHatSupp, "silber.png", "phongWithTexture");
			setTexAndShade(hiHat, "messing.jpg","phongWithTexture");
	
			
			// build scene graph
			lightPos.addChild(light1,light2,light3);
			snareSuppPos.addChild(new ShapeNode(snareSupp),snarePos);
			snarePos.addChild(new ShapeNode(snare));
			basePos.addChild(new ShapeNode(baseDrum));
			ridePos.addChild(new ShapeNode(ride));
			crashPos.addChild(new ShapeNode(crash));
			rideSuppPos.addChild(new ShapeNode(rideSupp),ridePos);
			crashSuppPos.addChild(new ShapeNode(crashSupp),crashPos);
			thinHiHatSuppPos.addChild(new ShapeNode(thinHiHatSupp),lowerHiHat);
			lowerHiHat.addChild(new ShapeNode(hiHat));
			fatHiHatSuppPos.addChild(thinHiHatSuppPos, new ShapeNode(fatHiHatSupp));
			upperHiHat.addChild(new ShapeNode(hiHat));
			thinHiHatSuppPos.addChild(upperHiHat);
			drumPos.addChild(snareSuppPos,basePos,rideSuppPos,crashSuppPos,fatHiHatSuppPos);	
			world.addChild(lightPos,drumPos, new ShapeNode(floor));
			
			// set initial transformations
			drumPos.setTranslation(new Vector3f(10,baseDrumRad,0));
			
			snarePos.setTranslation(new Vector3f(0,snareHeight,0));
			snarePos.setRotation(new Vector3f(0.5f,0,1), 0.5f);
			snareSuppPos.setTranslation(new Vector3f(-0.5f,-baseDrumRad,-2.5f));
			
			basePos.setRotation(new Vector3f(0,0,1), (float)(Math.PI/2));
			basePos.setTranslation(new Vector3f(2,0,0));
		
			ridePos.setTranslation(new Vector3f(0,rideHeight-0.3f,0));
			ridePos.setRotation(new Vector3f(1,0,1), 0);
			rideSuppPos.setTranslation(new Vector3f(-3,-baseDrumRad,3));
			
			crashPos.setTranslation(new Vector3f(0,crashHeight-0.3f,0));
			crashSuppPos.setTranslation(new Vector3f(1.5f,-baseDrumRad,-2));

			fatHiHatSuppPos.setTranslation(new Vector3f(-3,-baseDrumRad,-2));
			thinHiHatSuppPos.setTranslation(new Vector3f(0,fatHiHatSuppHeight,0));

			lowerHiHat.setTranslation(new Vector3f(0,0.45f,0));
			lowerHiHat.setRotation(new Vector3f(0,0,1), (float)Math.PI);
			upperHiHat.setTranslation(new Vector3f(0,0.6f,0));
			
		
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {table,sphere,snare,baseDrum,ride,snareSupp,rideSupp,crashSupp,crash,fatHiHatSupp,thinHiHatSupp,hiHat,floor};
			TransformGroup[] transformGroups = {};
			
			renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes,null);
			setupMainWindow(camera,"Rotational Body");
		}
		
		private static Point2f p(double x, double y){
			Point2f point = new Point2f((float)x,(float)y);
			return point;
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
		
		private static void setTexAndShade(Shape shape, String tex, String shader){
			shape.setMaterial(new Material("../jrtr/textures/"+tex,1));
			shape.getMaterial().setFragmentShaderPath("../jrtr/shaders/"+shader+".frag");
			shape.getMaterial().setVertexShaderPath("../jrtr/shaders/"+shader+".vert");
		}
	}
