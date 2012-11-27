package sceneGraph;

import javax.vecmath.Matrix4f;
import jrtr.PointLight;

public class LightNode extends Leaf {

	private PointLight pointLight;
	private Matrix4f transformation;
	
	public LightNode(PointLight pointLight, Matrix4f transformation){
		this.pointLight = pointLight;
		this.transformation = transformation;
	}
	
	public LightNode(PointLight pointLight){
		this(pointLight, new Matrix4f());
		Matrix4f id = new Matrix4f();
		id.setIdentity();
		this.transformation = id;
	}
	
	@Override
	public Matrix4f getTransformationMatrix() {
		return transformation;
	}

	public PointLight getLight() {
		return this.pointLight;
	}

}
