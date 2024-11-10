package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Collection;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

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
	
	default boolean doesSelfReportErrors() {
		return false;
	}
	
	default Collection<DDDConfigReaderException> getSelfReportedErrors() {
		return ImmutableList.of();
	}
}
