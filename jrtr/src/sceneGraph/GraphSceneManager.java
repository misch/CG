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
			// If top stack element is a (transformation) group
			if (sceneGraphStack.peek().getChildren() != null){
				
				// pop
				Node node = sceneGraphStack.pop();
				
				for (Node child : node.getChildren()){
					
					// multiply transformation matrix of transformation group
					child.getTransformationMatrix().mul(node.getTransformationMatrix(), child.getTransformationMatrix());
					
					// push children onto stack
					sceneGraphStack.push(child);
				}
			}
			
			// If top stack element is a Leaf
			if (sceneGraphStack.peek().getChildren() == null){
				
				// pop
				Node node = sceneGraphStack.pop();
				
				// multiply its transformation matrix
				Matrix4f shapeMatrix = node.getShape().getTransformation();
				shapeMatrix.mul(node.getTransformationMatrix(),shapeMatrix);
			}
			
			ShapeNode returnNode = (ShapeNode) sceneGraphStack.pop();
			return new RenderItem(returnNode.getShape(), returnNode.getShape().getTransformation());
		}
	}
}
