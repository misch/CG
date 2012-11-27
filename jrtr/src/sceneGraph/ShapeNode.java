package sceneGraph;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Shape;

public class ShapeNode extends Leaf {

	private Shape shape;
	
	public ShapeNode(Shape shape){
		this.shape = shape;
	}
	
	public Shape getShape() {
		return shape;
	}

	@Override
	public Matrix4f getTransformationMatrix() {
		return shape.getTransformation();
	}
	
	public void setTranslation(Vector3f trans){
		this.shape.getTransformation().setTranslation(trans);
	}
}
