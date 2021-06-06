package yeelp.distinctdamagedescriptions.util.lib;

/**
 * Simple exception for when certain action would result in class invariant
 * violations.
 * 
 * @author Yeelp
 *
 */
public class InvariantViolationException extends IllegalArgumentException {
	/**
	 * Construct a new InvariantViolationException with no error message.
	 */
	public InvariantViolationException() {
		super();
	}

	/**
	 * Construct a new InvariantViolationException with the specified error message.
	 * 
	 * @param msg error message.
	 */
	public InvariantViolationException(String msg) {
		super(msg);
	}
}
