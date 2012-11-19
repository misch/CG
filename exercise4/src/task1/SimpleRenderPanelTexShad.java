package task1;

import java.util.Timer;

import jrtr.GLRenderPanel;
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
public class SimpleRenderPanelTexShad extends GLRenderPanel {
	private RenderContext renderContext;
//	private SimpleSceneManager sceneManager;
	private SceneManagerInterface sceneManager;
	private Shape[] shapes;
	
//	public SimpleRenderPanelTexShad(SimpleSceneManager sceneManager, Shape[] shapes){
	public SimpleRenderPanelTexShad(SceneManagerInterface sceneManager, Shape[] shapes){
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
		
		Texture[] textures = new Texture[shapes.length];
		Shader[] shaders = new Shader[shapes.length];
		
		for (int i = 0; i<shapes.length;i++){
			textures[i] = renderContext.makeTexture(); 
			shaders[i] = renderContext.makeShader();
			String vertShaderPath = shapes[i].getMaterial().getVertexShaderPath();
			String fragShaderPath = shapes[i].getMaterial().getFragmentShaderPath();
			
			try{
				textures[i].load(shapes[i].getMaterial().getTexFile());
				shapes[i].getMaterial().setTexture(textures[i]);
			}
			catch(Exception e){
				System.out.print("Could not load a texture\n");
			}
			
			try{
				shaders[i].load(vertShaderPath, fragShaderPath);
				shapes[i].getMaterial().setShader(shaders[i]);
			}
			catch (Exception e){
				System.out.println("Could not load shader");
				System.out.println(e.getMessage());
			}
		}	
		// Register a timer task
	    Timer timer = new Timer();
	    float angle = 0.005f;
	    timer.scheduleAtFixedRate(new AnimationTask(angle, shapes,this), 0, 10);
	}

}
