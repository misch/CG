package sceneGraph;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderItem;
import jrtr.SceneManagerInterface;
import jrtr.SceneManagerIterator;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

public class GraphSceneManager implements SceneManagerInterface {

	private Node root;
	private Camera camera;
	private Frustum frustum;
	
	
	public GraphSceneManager(Node root, Camera camera, Frustum frustum){
		this.root = root;
		this.camera = camera;
		this.frustum = frustum;
	}
	@Override
	public SceneManagerIterator iterator() {
		return new GraphSceneManagerItr(this);
	}

	@Override
	public Iterator<PointLight> lightIterator() {
		// TODO Auto-generated method stub
		return (Iterator<PointLight>) new Stack<PointLight>();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Frustum getFrustum() {
		return frustum;
	}
	
	private class GraphSceneManagerItr implements SceneManagerIterator {
		
		private Stack<Node> sceneGraphStack = new Stack<Node>();
		
		public GraphSceneManagerItr(GraphSceneManager sceneManager)
		{
			sceneGraphStack.push(sceneManager.root);
		}
		
		public boolean hasNext()
		{
			return !sceneGraphStack.isEmpty();
		}
		
		public RenderItem next()
		{
			while (sceneGraphStack.peek().getChildren() != null){
				
				Node node = sceneGraphStack.pop();
				
				for (Node child : node.getChildren()){
					child.getTransformationMatrix().mul(node.getTransformationMatrix(), child.getTransformationMatrix());
					sceneGraphStack.push(child);
				}
			}
			
			Node top = sceneGraphStack.pop();
			return new RenderItem(top.getShape(), top.getTransformationMatrix());
		}
	}
}
