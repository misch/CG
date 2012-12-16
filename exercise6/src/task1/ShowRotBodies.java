package task1;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.Cylinder;

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
			Camera 	camera = new Camera(new Vector3f(0,0,20), new Vector3f(0,0,0), new Vector3f(0,1,0));
			Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
			TransformGroup world = new TransformGroup();
			TransformGroup sphereGroup = new TransformGroup();
			
			Point2f[] tablePoints = {
					p(1,0),p(0.6,0.5),p(0.1,0.7),
					p(0.1,1),p(0.1,3),p(0.1,4),
					p(0.1,5),p(2,5),p(2.5,5),
					p(3,5),p(3.1,5),p(3.1,5.1),p(3,5.1),
					p(2.5,5.1),p(1.5,5.1),p(0.1,5.1)};
			
			Point2f[] sphere = {p(0,1),p(4d/6,1),p(4d/6,0),p(0,0)};
			
			Point2f[] bowl = {p(0,0),p(0.5,0),p(1,0.5),p(1,1)};

			RotationalBody rotBody = new RotationalBody(new BezierCurve(5,tablePoints,50),4);
			Shape rotShape = rotBody.getShape();
			rotShape.setMaterial(new Material("../jrtr/textures/wood.jpg",1));
			rotShape.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
			rotShape.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
			
			
			RotationalBody sphereBody = new RotationalBody(new BezierCurve(1,sphere,100),50);
			Shape sphereShape = sphereBody.getShape();
			sphereShape.setMaterial(new Material("../jrtr/textures/fussball2.jpg",1));
			sphereShape.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
			sphereShape.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
			sphereShape.getMaterial().setSpecularReflection(200);
			sphereShape.getMaterial().setPhongExponent(1000);
			
			Shape bowlBody = new RotationalBody(new BezierCurve(1,bowl,100),50).getShape();
			bowlBody.setMaterial(new Material("../jrtr/textures/wood.jpg",2));
			bowlBody.getMaterial().setFragmentShaderPath("..jrtr/shaders/phongWithTexture.frag");
			bowlBody.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
			bowlBody.getMaterial().setSpecularReflection(20);
			bowlBody.getMaterial().setPhongExponent(2000);
			
			TransformGroup lightPos = new TransformGroup();
			lightPos.setTranslation(new Vector3f(0,0,3));
			PointLight light = new PointLight(30, new Point3f(0,0,0));
			
			LightNode light1 = new LightNode(light);
			light1.setTranslation(new Vector3f(3,15,2));
			LightNode light2 = new LightNode(light);
			light2.setTranslation(new Vector3f(0,2,-5));
			LightNode light3 = new LightNode(light);
			light3.setTranslation(new Vector3f(-5,2,0));
			
			lightPos.addChild(light1,light2,light3);
			
			ShapeNode table1 = new ShapeNode(rotShape);
			ShapeNode sphereNode = new ShapeNode(sphereShape);
			ShapeNode bowlNode = new ShapeNode(bowlBody);
			sphereGroup.setTranslation(new Vector3f(4,0,0));
			sphereGroup.addChild(sphereNode);
			
			TransformGroup tableTop = new TransformGroup();
			tableTop.setTranslation(new Vector3f(0,5.1f,0));
			tableTop.addChild(bowlNode);
			

			// build graph	
			world.addChild(lightPos,table1,sphereGroup,tableTop);
			
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {rotShape,sphereShape,bowlBody};
			renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes);
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
	}
