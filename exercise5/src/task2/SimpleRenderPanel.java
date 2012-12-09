package task2;

import java.util.Timer;

import sceneGraph.TransformGroup;

import jrtr.GLRenderPanel;
import jrtr.Material;
import jrtr.RenderContext;
import jrtr.SceneManagerInterface;
import jrtr.Shader;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.Texture;

/**
 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
 * provide a call-back function for initialization. 
 */ 
public class SimpleRenderPanel extends GLRenderPanel {
	private RenderContext renderContext;
	private SceneManagerInterface sceneManager;
	private Shape[] shapes;
	
	public SimpleRenderPanel(SceneManagerInterface sceneManager, Shape[] shapes){
		this.sceneManager = sceneManager;	
		this.shapes = shapes;
	}
	
	/**
	 * Initialization call-back. We initialize our renderer here.
	 * 
	 * @param r	the render context that is associated with this render panel
	 */
	public void init(RenderContext r)
	{
		this.renderContext = r;
		this.renderContext.setSceneManager(this.sceneManager);
		
		Texture texture = renderContext.makeTexture();
		Shader shader = renderContext.makeShader();
		
		String vertShaderPath = "../jrtr/shaders/default.vert";
		String fragShaderPath = "../jrtr/shaders/default.frag";
		String texturePath = "../jrtr/textures/metall.jpg";
		
		try{
			texture.load(texturePath);
			for (int i = 0; i< shapes.length; i++){
				shapes[i].setMaterial(new Material("",1));
//				shapes[i].getMaterial().setSpecularReflection(5);
//				shapes[i].getMaterial().setPhongExponent(500);
				shapes[i].getMaterial().setTexture(texture);
			}
		}
		catch(Exception e){
			System.out.print("Could not load a texture\n");
		}
			
		try{
			shader.load(vertShaderPath, fragShaderPath);
			for (int i = 0; i<shapes.length;i++){
				shapes[i].getMaterial().setShader(shader);
			}
		}
		catch (Exception e){
			System.out.println("Could not load shader");
			System.out.println(e.getMessage());
		}
	}
}
