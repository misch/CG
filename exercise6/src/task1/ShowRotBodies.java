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
import jrtr.VertexData;
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
			Camera 	camera = new Camera(new Vector3f(10,2,10), new Vector3f(20,0,20), new Vector3f(0,1,0));
			Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
			TransformGroup world = new TransformGroup();
			TransformGroup sphereGroup = new TransformGroup();
			Point2f[] floorPoints = {p(0,0),p(10,0),p(20,0),p(50,0)};
//			Shape floor = new RotationalBody(new BezierCurve(1,floorPoints,1),4).getShape();
			Shape floor = new Shape(makePlane());
			setTexAndShade(floor,"sand.png","bumpShader");
			floor.getMaterial().setBumpMapPath("../jrtr/textures/terrain.png");
			
			float snareHeight = 2.2f,
				baseDrumRad = 1.3f,
				rideHeight = 3.5f,
				crashHeight = 4.5f,
				fatHiHatSuppHeight = 2.4f,
				bigTomHeight = 1;
			
			Shape 
				table = new Table(5,4).getShape(),
				sphere = new Sphere(100,50,1,new Point2f(0,1)).getShape(),
				snareSupp = new RotCylinder(50,50,0.05f,snareHeight).getShape(),
				snare = new RotCylinder(50,50,0.9f,0.4f).getShape(),
				baseDrum = new RotCylinder(50,50,baseDrumRad,2.5f).getShape(),
				rideSupp = new RotCylinder(50,50,0.05f,rideHeight).getShape(),
				ride = new Pan(50,50,1.5f,0.3f).getShape(),
				crash = new Pan(50,50,1.3f,0.3f).getShape(),
				crashSupp = new RotCylinder(50,50,0.05f,crashHeight).getShape(),
				fatHiHatSupp = new RotCylinder(50,50,0.05f,fatHiHatSuppHeight).getShape(),
				thinHiHatSupp = new RotCylinder(50,50,0.02f,fatHiHatSuppHeight/3f).getShape(),
				hiHat = new Pan(50,50,0.9f,0.3f).getShape(),
				littleTom = new RotCylinder(50,50,0.6f,0.7f).getShape(),
				bigTomSupp = new RotCylinder(50,50,0.05f,bigTomHeight).getShape(),
				bigTom = new RotCylinder(50,50,0.8f,1.2f).getShape(),
				moon = new Sphere(50,50,1,new Point2f(0,0)).getShape();
			
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
				upperHiHat = new TransformGroup(),
				littleToms = new TransformGroup(),
				tom1 = new TransformGroup(),
				tom2 = new TransformGroup(),
				bigTomSuppPos = new TransformGroup(),
				bigTomPos = new TransformGroup(),
				moonPos = new TransformGroup();
			
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
			
			setTexAndShade(littleTom, "drums2.png","bumpShader");
			littleTom.getMaterial().setBumpMapPath("../jrtr/textures/drum_bump.png");
			
			setTexAndShade(bigTom, "drums2.png","bumpShader");
			bigTom.getMaterial().setBumpMapPath("../jrtr/textures/drum_bump.png");
			
			setTexAndShade(bigTomSupp, "silber.png", "phongWithTexture");
			
			setTexAndShade(moon, "silber.png", "bumpShader");
			moon.getMaterial().setBumpMapPath("../jrtr/textures/terrain.png");
			
			// build scene graph
			lightPos.addChild(light1);
			snareSuppPos.addChild(new ShapeNode(snareSupp),snarePos);
			snarePos.addChild(new ShapeNode(snare));
			basePos.addChild(new ShapeNode(baseDrum), littleToms);
			ridePos.addChild(new ShapeNode(ride));
			crashPos.addChild(new ShapeNode(crash));
			rideSuppPos.addChild(new ShapeNode(rideSupp),ridePos);
			crashSuppPos.addChild(new ShapeNode(crashSupp),crashPos);
			thinHiHatSuppPos.addChild(new ShapeNode(thinHiHatSupp),lowerHiHat);
			lowerHiHat.addChild(new ShapeNode(hiHat));
			fatHiHatSuppPos.addChild(thinHiHatSuppPos, new ShapeNode(fatHiHatSupp));
			upperHiHat.addChild(new ShapeNode(hiHat));
			thinHiHatSuppPos.addChild(upperHiHat);
			drumPos.addChild(snareSuppPos,basePos,rideSuppPos,crashSuppPos,fatHiHatSuppPos,bigTomSuppPos);	
			world.addChild(lightPos,drumPos, new ShapeNode(floor),moonPos);
			littleToms.addChild(tom1,tom2);
			tom1.addChild(new ShapeNode(littleTom));
			tom2.addChild(new ShapeNode(littleTom));
			bigTomSuppPos.addChild(new ShapeNode(bigTomSupp),bigTomPos);
			bigTomPos.addChild(new ShapeNode(bigTom));
			moonPos.addChild(new ShapeNode(moon));
			
			// set initial transformations
			drumPos.setTranslation(new Vector3f(20,baseDrumRad,20));
			
			snarePos.setTranslation(new Vector3f(0,snareHeight,0));
			snarePos.setRotation(new Vector3f(0.5f,0,1), 0.5f);
			snareSuppPos.setTranslation(new Vector3f(-0.8f,-baseDrumRad,-2));
			
			basePos.setRotation(new Vector3f(0,0,1), (float)(Math.PI/2));
			basePos.setTranslation(new Vector3f(2,0,0));
		
			ridePos.setTranslation(new Vector3f(0,rideHeight-0.3f,0));
			ridePos.setRotation(new Vector3f(1,0,1), 0);
			rideSuppPos.setTranslation(new Vector3f(-0.75f,-baseDrumRad,2.6f));
			
			crashPos.setTranslation(new Vector3f(0,crashHeight-0.3f,0));
			crashSuppPos.setTranslation(new Vector3f(1.5f,-baseDrumRad,-2));

			fatHiHatSuppPos.setTranslation(new Vector3f(-2,-baseDrumRad,-2.3f));
			thinHiHatSuppPos.setTranslation(new Vector3f(0,fatHiHatSuppHeight+0.2f,0));

			lowerHiHat.setTranslation(new Vector3f(0,0.25f,0));
			lowerHiHat.setRotation(new Vector3f(0,0,1), (float)Math.PI);
			upperHiHat.setTranslation(new Vector3f(0,0.3f,0));
			
			littleToms.setTranslation(new Vector3f(baseDrumRad+0.5f,1.5f,0));
			littleToms.setRotation(new Vector3f(0,0,1), -(float)(Math.PI/2));
			
			tom1.setTranslation(new Vector3f(0,0,-0.8f));
			tom1.setRotation(new Vector3f(0.3f,0,1), 0.7f);
			tom2.setTranslation(new Vector3f(0,0,0.8f));
			tom2.setRotation(new Vector3f(-0.3f,0,1),0.7f);
			
			bigTomSuppPos.setTranslation(new Vector3f(-1.8f,-baseDrumRad,2.1f));
			bigTomPos.setTranslation(new Vector3f(0,bigTomHeight,0));
			bigTomPos.setRotation(new Vector3f(1,0,0),-0.3f);
			
			moonPos.setTranslation(new Vector3f(5,3,0));
		
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {table,sphere,snare,baseDrum,ride,snareSupp,rideSupp,crashSupp,crash,fatHiHatSupp,thinHiHatSupp,hiHat,floor,littleTom,bigTomSupp,bigTom,moon};
			TransformGroup[] transformGroups = {};
			
			renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes,null);
			setupMainWindow(camera,"Rotational Body",lightPos);
		}
		
		private static Point2f p(double x, double y){
			Point2f point = new Point2f((float)x,(float)y);
			return point;
		}
		
		private static void setupMainWindow(Camera camera, String name,TransformGroup light) {
			JFrame jframe = new JFrame(name);
			jframe.setSize(700, 700);
			jframe.setLocationRelativeTo(null); // center of screen
			jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window
			
//			LandscapeListener listener = new LandscapeListener(camera, renderPanel);
			CamAndLightListener listener = new CamAndLightListener(camera,renderPanel,light);
			
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
		
		private static VertexData makePlane(){
			
			float v[] = { 	50,0,50, 		50,0,0, 	0,0,0, 	0,0,50};
			
			float c[] = {	1,0,0,	1,1,0, 	1,0,0, 	1,1,0}; 
		
			float normals[] = {0,1,0,  0,1,0,  0,1,0,  0,1,0};
			
			int indices[] = {0,2,3,  0,1,2};
			
			float t[] = {1,0,  1,1,  0,1,  0,0};
			
			float tan[] = {0,0,1,  0,0,1,  0,0,1,  0,0,1};
			
			// Set up the vertex data
			VertexData vertexData = new VertexData(4);

			// Specify the elements of the vertex data:
			// - one element for vertex positions
			vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
			// - one element for vertex colors
			vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
			// - one element for vertex normals
			vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
			
			vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
			
			vertexData.addElement(tan,  VertexData.Semantic.TANGENT, 3);
			
			vertexData.addIndices(indices);

			return vertexData;
		}

	}
