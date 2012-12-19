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
		
			Shape table = new Table(5,4).getShape();
			setTexAndShade(table,"wood.jpg","phongWithTexture");
			
			Shape sphere = new Sphere(100,50,1,new Point2f(0,1)).getShape();
			setTexAndShade(sphere,"fussball2.jpg","phongWithTexture");
			sphere.getMaterial().setSpecularReflection(200);
			sphere.getMaterial().setPhongExponent(1000);
		
			Shape snare = new RotCylinder(50,50,1,0.6f).getShape();
			setTexAndShade(snare, "drums.png","phongWithTexture");
			float snareHeight = 2.5f;
			
			Shape snareSupp = new RotCylinder(50,50,0.05f,snareHeight).getShape();
			setTexAndShade(snareSupp, "silber.png", "phongWithTexture");
			snareSupp.getMaterial().setSpecularReflection(200);
			snareSupp.getMaterial().setPhongExponent(1000);
			
			float baseDrumRad = 1.5f;
			Shape baseDrum = new RotCylinder(50,50,baseDrumRad,2.5f).getShape();
			setTexAndShade(baseDrum, "drums.png","phongWithTexture");
			
			Shape ride = new Pan(50,50,1.5f,0.3f).getShape();
			setTexAndShade(ride, "messing.jpg","phongWithTexture");

			float rideHeight = 3.5f;
			Shape rideSupp = new RotCylinder(50,50,0.05f,rideHeight).getShape();
			setTexAndShade(rideSupp, "silber.png", "phongWithTexture");
			rideSupp.getMaterial().setSpecularReflection(200);
			rideSupp.getMaterial().setPhongExponent(1000);
			
			Shape crash = new Pan(50,50,1.3f,0.3f).getShape();
			setTexAndShade(crash, "messing.jpg","phongWithTexture");
			
			float crashHeight = 4.5f;
			Shape crashSupp = new RotCylinder(50,50,0.05f,crashHeight).getShape();
			setTexAndShade(crashSupp, "silber.png", "phongWithTexture");
			
			float fatHiHatSuppHeight = 3;
			Shape fatHiHatSupp = new RotCylinder(50,50,0.05f,fatHiHatSuppHeight).getShape();
			Shape thinHiHatSupp = new RotCylinder(50,50,0.02f,fatHiHatSuppHeight/4f).getShape();
			setTexAndShade(fatHiHatSupp, "silber.png", "phongWithTexture");
			setTexAndShade(thinHiHatSupp, "silber.png", "phongWithTexture");
			
			Shape hiHat = new Pan(50,50,1,0.3f).getShape();
			setTexAndShade(hiHat, "messing.jpg","phongWithTexture");
			
			TransformGroup lightPos = new TransformGroup();
			lightPos.setTranslation(new Vector3f(0,0,0));
			PointLight light = new PointLight(30, new Point3f(0,0,0));
			
			LightNode light1 = new LightNode(light);
			light1.setTranslation(new Vector3f(14,5,2));
			LightNode light2 = new LightNode(light);
			light2.setTranslation(new Vector3f(0,2,-5));
			LightNode light3 = new LightNode(light);
			light3.setTranslation(new Vector3f(-5,2,0));
			
			lightPos.addChild(light1,light2,light3);
			
			ShapeNode table1 = new ShapeNode(table);
			sphereGroup.setTranslation(new Vector3f(4,0,0));
			sphereGroup.addChild(new ShapeNode(sphere));
			
			TransformGroup tableTop = new TransformGroup();
			tableTop.setTranslation(new Vector3f(0,5.1f,0));
			
			TransformGroup drumPos = new TransformGroup();
			drumPos.setTranslation(new Vector3f(10,1,0));
			
			TransformGroup snarePos = new TransformGroup();
			snarePos.setTranslation(new Vector3f(0,snareHeight,0));
			snarePos.setRotation(new Vector3f(0.5f,0,1), 0.5f);
			
			TransformGroup snareSuppPos = new TransformGroup();
			snareSuppPos.setTranslation(new Vector3f(-0.5f,-baseDrumRad,-2.5f));
			snareSuppPos.addChild(new ShapeNode(snareSupp),snarePos);
			snarePos.addChild(new ShapeNode(snare));
				
			TransformGroup basePos = new TransformGroup();
			basePos.addChild(new ShapeNode(baseDrum));
			basePos.setRotation(new Vector3f(0,0,1), (float)(Math.PI/2));
			basePos.setTranslation(new Vector3f(2,0,0));
			
			TransformGroup ridePos = new TransformGroup();
			ridePos.setTranslation(new Vector3f(0,rideHeight-0.3f,0));
			ridePos.addChild(new ShapeNode(ride));
			ridePos.setRotation(new Vector3f(1,0,1), 0);
			
			TransformGroup rideSuppPos = new TransformGroup();
			rideSuppPos.setTranslation(new Vector3f(-3,-baseDrumRad,3));
			rideSuppPos.addChild(new ShapeNode(rideSupp),ridePos);
			
			TransformGroup crashPos = new TransformGroup();
			crashPos.setTranslation(new Vector3f(0,crashHeight-0.3f,0));
			crashPos.addChild(new ShapeNode(crash));
			
			TransformGroup crashSuppPos = new TransformGroup();
			crashSuppPos.setTranslation(new Vector3f(1.5f,-baseDrumRad,-2));
			crashSuppPos.addChild(new ShapeNode(crashSupp),crashPos);
			
			TransformGroup fatHiHatSuppPos = new TransformGroup();
			TransformGroup thinHiHatSuppPos = new TransformGroup();
			
			fatHiHatSuppPos.setTranslation(new Vector3f(-3,-baseDrumRad,-2));
			thinHiHatSuppPos.setTranslation(new Vector3f(0,fatHiHatSuppHeight,0));
			
			fatHiHatSuppPos.addChild(thinHiHatSuppPos, new ShapeNode(fatHiHatSupp));
			thinHiHatSuppPos.addChild(new ShapeNode(thinHiHatSupp));
			
			TransformGroup lowerHiHat = new TransformGroup();
			lowerHiHat.addChild(new ShapeNode(hiHat));
			lowerHiHat.setRotation(new Vector3f(0,0,1), (float)Math.PI);
			
			thinHiHatSuppPos.addChild(lowerHiHat);
			
			
			drumPos.addChild(snareSuppPos,basePos,rideSuppPos,crashSuppPos,fatHiHatSuppPos);
			// build graph	
			world.addChild(lightPos,table1,sphereGroup,tableTop,drumPos);
			
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {table,sphere,snare,baseDrum,ride,snareSupp,rideSupp,crashSupp,crash,fatHiHatSupp,thinHiHatSupp,hiHat};
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
