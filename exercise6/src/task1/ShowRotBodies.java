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
			Camera 	camera = new Camera(new Vector3f(0,0,10), new Vector3f(0,0,0), new Vector3f(0,1,0));
			Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
			TransformGroup world = new TransformGroup();
			TransformGroup sphereGroup = new TransformGroup();
			
			Point2f[] bowl = {p(0,0),p(0.5,0),p(1,0.5),p(1,1)};

			Shape table = new Table(5,4).getShape();
			table.setMaterial(new Material("../jrtr/textures/wood.jpg",1));
			table.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
			table.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
			
			Shape sphere = new Sphere(100,50,1,new Point2f(0,1)).getShape();
			sphere.setMaterial(new Material("../jrtr/textures/fussball2.jpg",1));
			sphere.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
			sphere.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
			sphere.getMaterial().setSpecularReflection(200);
			sphere.getMaterial().setPhongExponent(1000);
			
			Shape bowlBody = new RotCylinder(1,50,1,0.5f).getShape();
//			Shape bowlBody = new RotationalBody(new BezierCurve(1,bowl,100),50).getShape();
			bowlBody.setMaterial(new Material("../jrtr/textures/wood.jpg",2));
			bowlBody.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
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
			
			ShapeNode table1 = new ShapeNode(table);
			ShapeNode sphereNode = new ShapeNode(sphere);
			ShapeNode bowlNode = new ShapeNode(bowlBody);
			sphereGroup.setTranslation(new Vector3f(4,0,0));
			sphereGroup.addChild(sphereNode);
			
			TransformGroup tableTop = new TransformGroup();
			tableTop.setTranslation(new Vector3f(0,5.1f,0));
			tableTop.addChild(bowlNode);
			
			TransformGroup drumPos = new TransformGroup();
			ShapeNode snare = new ShapeNode(bowlBody);
			drumPos.setTranslation(new Vector3f(10,0,0));
			TransformGroup snarePos = new TransformGroup();
			snarePos.setTranslation(new Vector3f(0,1,0));
			snarePos.setRotation(new Vector3f(0,0,1), 0.5f);
			snarePos.addChild(snare);
			drumPos.addChild(snarePos);
			
			TransformGroup basePos = new TransformGroup();
			basePos.addChild(new ShapeNode(bowlBody));
			basePos.setRotation(new Vector3f(0,0,1), (float)(Math.PI/2));
			drumPos.addChild(basePos);
			
			// build graph	
			world.addChild(lightPos,table1,sphereGroup,tableTop,drumPos);
			
			sceneManager = new GraphSceneManager(world,camera,frustum);
			Shape[] shapes = {table,sphere,bowlBody};
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
