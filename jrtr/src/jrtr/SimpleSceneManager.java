package jrtr;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * A simple scene manager that stores objects in a linked list.
 */
public class SimpleSceneManager implements SceneManagerInterface {

	private LinkedList<Shape> shapes;
	private Camera camera;
	private Frustum frustum;
	private List<PointLight> lightSources;
	
	public SimpleSceneManager()
	{
		this(new Camera(), new Frustum());
	}
	
	public SimpleSceneManager(Camera camera, Frustum frustum)
	{
		this.camera = camera;
		this.frustum = frustum;
		this.shapes = new LinkedList<Shape>();
		this.lightSources = new LinkedList<PointLight>();
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public Frustum getFrustum()
	{
		return frustum;
	}
	
	public void addShape(Shape shape)
	{
		shapes.add(shape);
	}
	
	public SceneManagerIterator iterator()
	{
		return new SimpleSceneManagerItr(this);
	}
	
	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Iterator<PointLight> lightIterator()
	{
		return lightSources.iterator();
	}
	
	public void addLightSource(PointLight source){
		lightSources.add(source);
	}

	private class SimpleSceneManagerItr implements SceneManagerIterator {
		
		public SimpleSceneManagerItr(SimpleSceneManager sceneManager)
		{
			itr = sceneManager.shapes.listIterator(0);
		}
		
		public boolean hasNext()
		{
			return itr.hasNext();
		}
		
		public RenderItem next()
		{
			Shape shape = itr.next();
			// Here the transformation in the RenderItem is simply the 
			// transformation matrix of the shape. More sophisticated 
			// scene managers will set the transformation for the 
			// RenderItem differently.
			return new RenderItem(shape, shape.getTransformation());
		}
		
		ListIterator<Shape> itr;
	}
}
