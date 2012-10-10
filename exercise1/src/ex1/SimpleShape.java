package ex1;
import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


import jrtr.Shape;
import jrtr.VertexData;


public abstract class SimpleShape implements IForm {
	protected ArrayList<Float> vertices, colors;
	protected ArrayList<Integer> indices;
	protected float u,v;
	private VertexData vertexData;
	protected Shape shape;
		
	public Shape getShape(){
		if (shape ==null)
			shape = new Shape(getVertexData());
		return shape;
	}
	
	public VertexData getVertexData(){
		vertexData = new VertexData(getVertices().length/3);
		vertexData.addElement(getColors(),VertexData.Semantic.COLOR, 3);
		vertexData.addElement(getVertices(), VertexData.Semantic.POSITION, 3);		
		vertexData.addIndices(getIndices());
		return vertexData;
	}
	
	protected float[] getVertices() {
		float[] v = new float[vertices.size()];
		
		for (int i = 0; i<v.length; i++){
			v[i] = vertices.get(i);
		}
		return v;
	}
	
		protected float[] getColors() {
		float[] c = new float[colors.size()];
		
		for (int i = 0; i<c.length; i++){
			c[i] = colors.get(i);
		}
		return c;
	}

	protected int[] getIndices() {
		int[] ind = new int[indices.size()];
		
		for (int i = 0; i<ind.length; i++){
			ind[i] = indices.get(i);
		}
		return ind;
	}
	
	protected int addVertex(ArrayList<Float> v, Point3f point) {
		int indexOfVertex;
		
		v.add(point.x);
		v.add(point.y);
		v.add(point.z);
		
		indexOfVertex = (v.size()-1)/3; 
		
		return indexOfVertex;
	}
	
	protected void addColor(ArrayList<Float> c, Color3f col){
		c.add(col.x);
		c.add(col.y);
		c.add(col.z);
	}
	
	protected void addTriangle(ArrayList<Integer> indices, int upperPoint, int lowerPoint, int nextUpperPoint) {
		indices.add(upperPoint);
		indices.add(lowerPoint);
		indices.add(nextUpperPoint);
	}
	
	
	protected abstract float x(float u, float v);
	protected abstract float y(float u, float v);
	protected abstract float z(float u, float v);
}