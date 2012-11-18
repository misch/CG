package sceneGraph;

import jrtr.Shape;

public class ShapeNode extends Leaf {

	private Shape shape;
	
	@Override
	public Shape getShape() {
		return shape;
	}

}
