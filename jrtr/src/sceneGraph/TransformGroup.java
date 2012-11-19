package sceneGraph;

import javax.vecmath.Matrix4f;


public class TransformGroup extends Group {

	Matrix4f transformation;
	
	public TransformGroup(){
		Matrix4f id = new Matrix4f();
		id.setIdentity();
		this.transformation = id;
	}
	@Override
	public Matrix4f getTransformationMatrix() {
		return transformation;
	}

}
