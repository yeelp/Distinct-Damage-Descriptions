package yeelp.distinctdamagedescriptions.config.readers;

import com.google.common.base.Stopwatch;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;

/**
 * A basic config reader
 * 
 * @author Yeelp
 *
 */
public interface DDDConfigReader extends Runnable {
	void read();

	String getName();

	boolean shouldTime();

	default Thread toThread() {
		return new Thread(this, this.getName());
	}

	@Override
	default void run() {
		String msg;
		if(this.shouldTime()) {
			Stopwatch timer = Stopwatch.createStarted();
			this.read();
			timer.stop();
			msg = String.format("%s finished loading from config in %s", this.getName(), timer.toString());
		}
		else {
			this.read();
			msg = String.format("%s finished loading", this.getName());
		}
		DistinctDamageDescriptions.info(msg);
	}
}
