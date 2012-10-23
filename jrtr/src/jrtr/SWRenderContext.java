package jrtr;

import jrtr.RenderContext;
import jrtr.VertexData.VertexElement;

import java.awt.Color;
import java.awt.image.*;
import java.util.LinkedList;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;


/**
 * A skeleton for a software renderer. It works in combination with
 * {@link SWRenderPanel}, which displays the output image. In project 3 
 * you will implement your own rasterizer in this class.
 * <p>
 * To use the software renderer, you will simply replace {@link GLRenderPanel} 
 * with {@link SWRenderPanel} in the user application.
 */
public class SWRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private BufferedImage colorBuffer;
	private Matrix4f viewPortMatrix;
	private Raster clear;
		
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This is called by the SWRenderPanel to render the scene to the 
	 * software frame buffer.
	 */
	public void display()
	{
		if(sceneManager == null) return;
		
		beginFrame();
	
		SceneManagerIterator iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			draw(iterator.next());
		}		
		
		endFrame();
	}

	/**
	 * This is called by the {@link SWJPanel} to obtain the color buffer that
	 * will be displayed.
	 */
	public BufferedImage getColorBuffer()
	{
		return colorBuffer;
	}
	
	/**
	 * Set a new viewport size. The render context will also need to store
	 * a viewport matrix, which you need to reset here. 
	 */
	public void setViewportSize(int width, int height)
	{
		viewPortMatrix = new Matrix4f();
		viewPortMatrix.setZero();
		viewPortMatrix.setM00(width/2f);
		viewPortMatrix.setM11(height/2f);
		viewPortMatrix.setM22(1/2f);
		viewPortMatrix.setM33(1f);
		viewPortMatrix.setM03(width/2f);
		viewPortMatrix.setM13(height/2f);
		viewPortMatrix.setM23(1/2f);

		
		colorBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage clearBuffer = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
		this.clear = clearBuffer.getRaster();
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		colorBuffer.setData(clear);
	}
	
	private void endFrame()
	{		
	}
	
	/**
	 * The main rendering method. You will need to implement this to draw
	 * 3D objects.
	 */
	private void draw(RenderItem renderItem)
	{
		Matrix4f t = new Matrix4f(renderItem.getT());
		t.mul(sceneManager.getCamera().getCameraMatrix(),t);
		t.mul(sceneManager.getFrustum().getProjectionMatrix(),t);
		t.mul(viewPortMatrix, t);
		
//		drawDots(renderItem.getShape(), t);
		drawTriangles(renderItem.getShape(), t);
		
	}
	private void drawTriangles(Shape shape, Matrix4f t) {		
		VertexData vertexData = shape.getVertexData();
//		LinkedList<VertexData.VertexElement>;
		int indices[] = vertexData.getIndices();
		float[][] colors;
		
		// Skeleton code to assemble triangle data
		int k = 0; // index of triangle vertex, k is 0,1, or 2
		
		
		// Loop over all vertex indices
		for(int j=0; j<indices.length; j++)
		{
			// Loop over all attributes of current vertex
			for(VertexElement vertexElement: vertexData.getElements()){
				if(vertexElement.getSemantic() == VertexData.Semantic.POSITION){
					Vector4f p = new Vector4f(vertexElement.getData()[j*3], vertexElement.getData()[j*3+1], vertexElement.getData()[j*3+2],1);
					t.transform(p);		
					float[][] positions = new float[3][4];
					positions[k][0] = p.x;
					positions[k][1] = p.y;
					positions[k][2] = p.z;
					positions[k][3] = p.w;
					k++;
				}
				else if (vertexElement.getSemantic() == VertexData.Semantic.COLOR){
					// TODO
				}
				else if (vertexElement.getSemantic() == VertexData.Semantic.NORMAL){
					//TODO
				}
			
				// Draw triangle as soon as we collected the data for 3 vertices
				if(k == 3)
				{
					// Draw the triangle with the collected three vertex positions, etc.
					// rasterizeTriangle(positions, colors, normals, ...);
					k = 0;
				}
			}
		}
				
			
//		//		int[] indices = shape.getVertexData().getIndices();
//		
//		for (int i = 0; i<indices.length; i+=9){
//			Tuple4f vert1 = new Vector4f(indices[i], indices[i+1], indices[i+2], 1);
//			Tuple4f vert2 = new Vector4f(indices[i+3], indices[i+4], indices[i+5], 1);
//			Tuple4f vert3 = new Vector4f(indices[i+6], indices[i+7], indices[i+8], 1);
//			
//			t.transform(vert1);
//			t.transform(vert2);
//			t.transform(vert3);
//			
//			// Homogeneous 2D coordinates
//			Vector3f homogeneousVert1 = homog2DCoord(vert1);
//			Vector3f homogeneousVert2 = homog2DCoord(vert2);
//			Vector3f homogeneousVert3 = homog2DCoord(vert3);
//			
//			Matrix3f edgeFuncCoeff = computeEdgeFuncCoeff(homogeneousVert1, homogeneousVert2, homogeneousVert3);
//			
//			
//			// Homogeneous division
////			vec.scale(1/vec.w);
//			
////			if (vec.x > 0 && vec.x < colorBuffer.getWidth() && vec.y > 0 && vec.y < colorBuffer.getHeight()){
////			colorBuffer.setRGB((int)(vec.x), colorBuffer.getHeight() - (int)(vec.y)-1, new Color(255,255,255).getRGB());
////			}
//		}
		
	}
	
	private boolean isInTriangle(Matrix3f edgeFuncCoeff, Vector3f vec){
		boolean isInTriangle = true;
		float x = vec.x;
		float y = vec.y;
		float w = vec.z;
		
		for (int i = 0; i<3; i++){
			Vector3f edgeCoeff = new Vector3f();
			edgeFuncCoeff.getColumn(i, edgeCoeff);
			
			float sum = edgeCoeff.x*(x/w)+edgeCoeff.y*(y/w)+edgeCoeff.z;
			isInTriangle = (sum>0);
		}
		return isInTriangle;
	}
	
	private Matrix3f computeEdgeFuncCoeff(Vector3f homogeneousVert1,
			Vector3f homogeneousVert2, Vector3f homogeneousVert3) {
		Matrix3f edgeFuncCoeff = new Matrix3f();
		
		edgeFuncCoeff.setRow(0, homogeneousVert1);
		edgeFuncCoeff.setRow(1, homogeneousVert2);
		edgeFuncCoeff.setRow(2, homogeneousVert3);
		
		edgeFuncCoeff.invert();
		return edgeFuncCoeff;
	}

	private void drawDots(Shape shape, Matrix4f t) {
		float[] vertices = shape.getVertexData().getElements().getLast().getData();
		
		for (int i = 0; i<vertices.length; i+=3){
			Tuple4f vec = new Vector4f(vertices[i], vertices[i+1], vertices[i+2], 1);
			t.transform(vec);
			
			// Homogeneous division
			vec.scale(1/vec.w);
			
			if (vec.x > 0 && vec.x < colorBuffer.getWidth() && vec.y > 0 && vec.y < colorBuffer.getHeight()){
			colorBuffer.setRGB((int)(vec.x), colorBuffer.getHeight() - (int)(vec.y)-1, new Color(255,255,255).getRGB());
			}
		}
		
	}

	private Vector3f homog2DCoord(Tuple4f vec) {
		Vector3f homogeneous = new Vector3f(vec.x, vec.y, vec.w);
		return homogeneous;
	}

	// Edge functions
	// Parameter:
	//	int x, y (pixel coordiantes),
	//	float w (z-buffer)
	private int alpha(int x, int y, float w){
		return 0;
	}
	
	
	
	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public Shader makeShader()	
	{
		return new SWShader();
	}

	/**
	 * Does nothing. We will not implement textures for the software renderer.
	 */
	public Texture makeTexture()
	{
		return new SWTexture();
	}
}
