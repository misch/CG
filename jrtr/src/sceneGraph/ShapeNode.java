package sceneGraph;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public class ShapeNode extends Leaf {

	private Shape shape;
	
	public ShapeNode(Shape shape){
		this.shape = shape;
	}
	
	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public Matrix4f getTransformationMatrix() {
		return shape.getTransformation();
	}
}
