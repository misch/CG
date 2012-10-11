package task3;

public class FractalLandscape {
	private int size;
	private float heights[][];
	
	/** The constructor has only one parameter "cornorHeight": all
	 * four corner will have the same height for now.
	 * @param size
	 * @param cornorHeight
	 */
	public FractalLandscape(int size, float cornorHeight){
		this.size = (int)Math.pow(2, size)+1;
		this.heights = new float[size][size];
		
		setUpCornors(cornorHeight);
	}

	private void setUpCornors(float cornorHeight) {
		heights[0][0] = cornorHeight;
		heights[size-1][0] = cornorHeight;
		heights[0][size-1] = cornorHeight;
		heights[size-1][size-1] = cornorHeight;
	}
}
