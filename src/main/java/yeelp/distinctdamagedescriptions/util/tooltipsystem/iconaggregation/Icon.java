package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

/**
 * Container for DDD damage type icons
 * 
 * @author Yeelp
 *
 */
public class Icon {
	private final int x, y, u;

	/**
	 * Construct a new Icon
	 * 
	 * @param x x pos on screen
	 * @param y y pos on screen
	 * @param u u coord in U-V tex map coords
	 */
	public Icon(int x, int y, int u) {
		this.x = x;
		this.y = y;
		this.u = u;
	}

	/**
	 * Get this Icon's X value
	 * 
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get this Icon's Y value
	 * 
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Get this Icon's U value in tex coords
	 * 
	 * @return the u
	 */
	public int getU() {
		return this.u;
	}
}
