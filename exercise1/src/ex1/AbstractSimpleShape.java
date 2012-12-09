package ex1;

import java.awt.Point;
import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Shape;
import jrtr.VertexData;


public abstract class AbstractSimpleShape implements IForm {
	protected ArrayList<Float> vertices;
	protected ArrayList<Float> colors = new ArrayList<Float>(); 
	protected ArrayList<Float> normals = new ArrayList<Float>(); 
	protected ArrayList<Integer> indices;
	protected ArrayList<Float> texels = new ArrayList<Float>();
	
	protected float u,v;
	protected VertexData vertexData;
	protected Shape shape;
		
	public Shape getShape(){
		if (shape ==null)
			shape = new Shape(getVertexData());
		return shape;
	}
	
	public VertexData getVertexData(){
		vertexData = new VertexData(getVertices().length/3);
		vertexData.addElement(getVertices(), VertexData.Semantic.POSITION, 3);
		
		if (this.colors.size()>0)
			vertexData.addElement(getColors(),VertexData.Semantic.COLOR, 3);
		if (this.normals.size() > 0)	
			vertexData.addElement(getNormals(), VertexData.Semantic.NORMAL,3);
		if (this.texels.size() > 0)
			vertexData.addElement(getTexels(),  VertexData.Semantic.TEXCOORD, 2);
				
		
		vertexData.addIndices(getIndices());
		return vertexData;
	}
	
	private float[] getTexels() {
		float[] t = new float[texels.size()];
		
		for (int i = 0; i<t.length; i++){
			t[i] = texels.get(i);
		}
		return t;
	}

	protected float[] getVertices() {
		float[] v = new float[vertices.size()];
		
		for (int i = 0; i<v.length; i++){
			v[i] = vertices.get(i);
		}
		return v;
	}
	
	protected float[] getNormals(){
		float[] n = new float[normals.size()];
		
		for (int i = 0; i< n.length; i++){
			n[i] = normals.get(i);
		}
		return n;
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
	
	protected void addNormal(ArrayList<Float> n, Vector3f normal) {
	n.add(normal.x);
	n.add(normal.y);
	n.add(normal.z);
}
	
	protected void addTexel(ArrayList<Float> t, float x, float y){
		t.add(x);
		t.add(y);
	}
	
	
	protected float x(float u, float v){return 0;}
	protected float y(float u, float v){return 0;}
	protected float z(float u, float v){return 0;}
}