package jrtr;
import javax.vecmath.*;

/**
 * Represents a 3D shape. The shape currently just consists
 * of its vertex data. It should later be extended to include
 * material properties, shaders, etc.
 */
public class Shape {

	private VertexData vertexData;
	private Matrix4f t;
	private Material material = new Material();
	private BoundingSphere boundingSphere;
	
	/**
	 * Make a shape from {@link VertexData}.
	 *  
	 * @param vertexData the vertices of the shape.
	 */
	public Shape(VertexData vertexData)
	{
		this.vertexData = vertexData;
		t = new Matrix4f();
		t.setIdentity();
		this.material = new Material();
		this.boundingSphere = new BoundingSphere(this);
	}
	
	public VertexData getVertexData()
	{
		return vertexData;
	}
	
	public void setTransformation(Matrix4f t)
	{
		this.t = t;
	}
	
	public Matrix4f getTransformation()
	{
		return t;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public Material getMaterial()
	{
		return this.material;
	}
	
	public BoundingSphere getBoundingSphere(){
		return this.boundingSphere;
	}

}
