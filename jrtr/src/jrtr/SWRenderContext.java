package jrtr;

import jrtr.RenderContext;

import java.awt.Color;
import java.awt.image.*;

import javax.media.opengl.GL3;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple4f;
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
//	private BufferedImage clearBuffer;
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
		
		float[] vertices = renderItem.getShape().getVertexData().getElements().getLast().getData();
		
		for (int i = 0; i<vertices.length; i+=3){
			Tuple4f vec = new Vector4f(vertices[i], vertices[i+1], vertices[i+2], 1);
			t.transform(vec);
			vec.scale(1/vec.w);
			if (vec.x > 0 && vec.x < colorBuffer.getWidth() && vec.y > 0 && vec.y < colorBuffer.getHeight()){
			colorBuffer.setRGB((int)(vec.x), colorBuffer.getHeight() - (int)(vec.y)-1, new Color(255,255,255).getRGB());
			}
		}
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
